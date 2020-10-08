#!/usr/bin/env bash

set -o pipefail

#============================================================

usage() {
  cat <<EOF
Usage:

  Commands
    list-tests
    regression-test
    smoke-test
    test [--trust <host>] [-Dkey=value] <pattern> [...]

  Example:
    test --trust example.amazonaws.com -Dclient-key=12345 ".*ReadIT"

  Docker Run Examples:
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

#============================================================

init() {
  [ -z "$SENTINEL_BASE_DIR" ] && SENTINEL_BASE_DIR=/sentinel
  cd $SENTINEL_BASE_DIR

  MODULE_PREFIX=lighthouse
  TEST_MODULE_NAME=vistalink-tests

  # Environment variables that are required to run
  REQUIRED_ENV_VARIABLES=(
    "CLIENT_KEY" \
    "K8S_ENVIRONMENT" \
    "K8S_LOAD_BALANCER" \
    "SENTINEL_ENV"
  )

  REGRESSION_TEST_PATTERN=".*IT\$"
  SMOKE_TEST_PATTERN=".*IT\$"

  #
  # Assume defaults for service locations to be on the load balancer.
  #
  if [ -z "$SENTINEL_ENV" ]; then SENTINEL_ENV=$K8S_ENVIRONMENT; fi
  if [ -z "$VISTALINK_URL" ]; then VISTALINK_URL=https://$K8S_LOAD_BALANCER; fi

  SYSTEM_PROPERTIES=()
}

main() {
  init
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
    lt|list-tests) listTests;;
    s|smoke-test) doTest "smoke-test";;
    r|regression-test) doTest "regression-test";;
    *) usage "Unknown command: $COMMAND";;
  esac
}

#============================================================

addSystemProperty() {
  local property=${1:-}
  local value=${2:-}
  [ -n "$value" ] && SYSTEM_PROPERTIES+=("-D$property=$value")
}

doTest() {
  local testType="${1:-}"
  [ -z "${testType}" ] && echo "Failed to determine test type." && exit 1
  echo "Running ${testType}"
  setupForAutomation
  java-tests \
    ${SYSTEM_PROPERTIES[@]} \
    --module-name "${TEST_MODULE_NAME}" \
    --regression-test-pattern "${REGRESSION_TEST_PATTERN:-.*}" \
    --smoke-test-pattern "${SMOKE_TEST_PATTERN:-.*}" \
    run-tests \
    ${testType}

  [ "$?" != "0" ] && exit 1
}

listTests() {
  java-tests --module-name="${TEST_MODULE_NAME}" list
}

runTestForPattern() {
  local pattern="$@"
  [ -z "${pattern}" ] && pattern=.*IT\$
  echo "Executing tests for pattern: ${pattern}"
  java-tests \
    ${SYSTEM_PROPERTIES[@]} \
    --module-name "${TEST_MODULE_NAME}" \
    --test-pattern "${pattern}" \
    run

  [ "$?" != "0" ] && exit 1
}

setupForAutomation() {
  for param in ${REQUIRED_ENV_VARIABLES[@]}; do
    [ -z ${!param} ] && usage "Variable $param must be specified."
  done

  trustServer $K8S_LOAD_BALANCER

  addSystemProperty client-key "$CLIENT_KEY"
  addSystemProperty sentinel "$SENTINEL_ENV"
}

trustServer() {
  local host=$1
  echo "Trusting ${host}"
  add-cert-to-jdk ${host}
}

#============================================================

main $@

exit 0
