#!/usr/bin/env bash

set -o pipefail

[ -z "$SENTINEL_BASE_DIR" ] && SENTINEL_BASE_DIR=/sentinel
cd $SENTINEL_BASE_DIR

MODULE_PREFIX=lighthouse
TEST_MODULE_NAME=vistalink-tests
TESTS_JAR=$(find -maxdepth 1 -name "${TEST_MODULE_NAME}-*-tests.jar")
MAIN_JAR=$(find -maxdepth 1 -name "${TEST_MODULE_NAME}-*.jar" -a -not -name "${TEST_MODULE_NAME}-*-tests.jar")

# Environment variables that are required to run
REQUIRED_ENV_VARIABLES=(
  "CLIENT_KEY" \
  "K8S_ENVIRONMENT" \
  "K8S_LOAD_BALANCER" \
  "SENTINEL_ENV"
)

#
# Assume defaults for service locations to be on the load balancer.
#
if [ -z "$SENTINEL_ENV" ]; then SENTINEL_ENV=$K8S_ENVIRONMENT; fi
if [ -z "$VISTALINK_URL" ]; then VISTALINK_URL=https://$K8S_LOAD_BALANCER; fi

SYSTEM_PROPERTIES=()

#============================================================

if [ ! -f "$MAIN_JAR" ]; then echo "Cannot find main jar: $MAIN_JAR"; exit 1; fi
if [ ! -f "$TESTS_JAR" ]; then echo "Cannot find tests jar: $TESTS_JAR"; exit 1; fi

usage() {
cat <<EOF
Commands
  list-tests
  regression-test
  smoke-test
  test [--trust <host>] [-Dkey=value] <pattern> [...]

Example
  test --trust example.amazonaws.com -Dclient-key=12345 ".*ReadIT"

Docker Run Examples
  docker run --rm --init --network=host \
--env-file qa.testvars --env K8S_LOAD_BALANCER=example.com --env K8S_ENVIRONMENT=qa \
vasdvp/${MODULE_PREFIX}-${TEST_MODULE_NAME}:latest smoke-test

  docker run --rm --init --network=host \
--env-file lab.testvars --env K8S_LOAD_BALANCER=example.com --env K8S_ENVIRONMENT=lab \
vasdvp/${MODULE_PREFIX}-${TEST_MODULE_NAME}:1.0.210 regression-test

$1
EOF
exit 1
}

trustServer() {
  local host=$1
  curl -sk https://$host > /dev/null 2>&1
  [ $? == 6 ] && return
  echo "Trusting $host"
  local cacertsDir="$JAVA_HOME/jre/lib/security/cacerts"
  [ -f "$JAVA_HOME/lib/security/cacerts" ] && cacertsDir="$JAVA_HOME/lib/security/cacerts"
  keytool -printcert -rfc -sslserver $host > $host.pem
  keytool \
    -importcert \
    -file $host.pem \
    -alias $host \
    -keystore $cacertsDir \
    -storepass changeit \
    -noprompt
}

doTest() {
  local pattern="$@"
  [ -z "$pattern" ] && pattern=.*IT\$
  echo "Executing tests for pattern: $pattern"

  local noise="org.junit"
  noise+="|groovy.lang.Meta"
  noise+="|io.restassured.filter"
  noise+="|io.restassured.internal"
  noise+="|java.lang.reflect"
  noise+="|java.net"
  noise+="|org.apache.http"
  noise+="|org.codehaus.groovy"
  noise+="|sun.reflect"

  java \
    ${SYSTEM_PROPERTIES[@]} \
    -jar junit-platform-console-standalone.jar \
    --scan-classpath \
    -cp "$MAIN_JAR" -cp "$TESTS_JAR" \
    --include-classname=$pattern \
    --disable-ansi-colors \
    --fail-if-no-tests \
    | grep -vE "^	at ($noise)"

  # Exit on failure otherwise let other actions run.
  [ $? != 0 ] && exit 1
}

doListTests() {
  jar -tf $TESTS_JAR \
    | grep -E '(IT|Test)\.class' \
    | sed 's/\.class//' \
    | tr / . \
    | sort
}

doSmokeTest() {
  setupForAutomation
  doTest
}

doRegressionTest() {
  setupForAutomation
  doTest
}

setupForAutomation() {
  for param in ${REQUIRED_ENV_VARIABLES[@]}; do
    [ -z ${!param} ] && usage "Variable $param must be specified."
  done

  trustServer $K8S_LOAD_BALANCER

  addSystemProperty client-key "$CLIENT_KEY"
  addSystemProperty sentinel "$SENTINEL_ENV"
}

addSystemProperty() {
  local property=${1:-}
  local value=${2:-}
  [ -n "$value" ] && SYSTEM_PROPERTIES+=("-D$property=$value")
}

ARGS=$(getopt -n $(basename ${0}) \
    -l "debug,help,trust:" \
    -o "D:h" -- "$@")
[ $? != 0 ] && usage
eval set -- "$ARGS"
while true
do
  case "$1" in
    -D) SYSTEM_PROPERTIES+=("-D$2");;
    --debug) set -x;;
    -h|--help) usage "halp! what this do?";;
    --trust) trustServer $2;;
    --) shift;break;;
  esac
  shift;
done

[ $# == 0 ] && usage "No command specified"
COMMAND=$1
shift

case "$COMMAND" in
  t|test) doTest $@;;
  lt|list-tests) doListTests;;
  s|smoke-test) doSmokeTest;;
  r|regression-test) doRegressionTest;;
  *) usage "Unknown command: $COMMAND";;
esac

exit 0
