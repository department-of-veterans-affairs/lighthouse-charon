#! /usr/bin/env bash

usage() {
  cat <<EOF

$0 [options]

Generate configurations for local development.

Options
     --debug               Enable debugging
 -h, --help                Print this help and exit.
     --secrets-conf <file> The configuration file with secrets!

Secrets Configuration
 - VISTA_APP_PROXY_USER
 - VISTA_APP_PROXY_ACCESS_CODE
 - VISTA_APP_PROXY_VERIFY_CODE


 These may optionally be provided
 - IDS_ENCODING_KEY
 - IDS_PATIENT_ID_PATTERN
 - WEB_EXCEPTION_KEY

$1
EOF
  exit 1
}

REPO=$(cd $(dirname $0)/../.. && pwd)
SECRETS="$REPO/secrets.conf"
PROFILE=dev
MARKER=$(date +%s)
ARGS=$(getopt -n $(basename ${0}) \
    -l "debug,help,secrets-conf:" \
  -o "h" -- "$@")
[ $? != 0 ] && usage
eval set -- "$ARGS"
while true
do
  case "$1" in
    --debug) set -x ;;
    -h|--help) usage "halp! what this do?" ;;
    --secrets-conf) SECRETS="$2" ;;
    --) shift;break ;;
  esac
  shift;
done

echo "Loading secrets: $SECRETS"
[ ! -f "$SECRETS" ] && usage "File not found: $SECRETS"
. $SECRETS
MISSING_SECRETS=false
[ -z "$WEB_EXCEPTION_KEY" ] && WEB_EXCEPTION_KEY="-shanktopus-for-the-win-"
[ -z "$VISTA_APP_PROXY_USER" ] && echo "Missing configuration: VISTA_APP_PROXY_USER" && MISSING_SECRETS=true
[ -z "$VISTA_APP_PROXY_ACCESS_CODE" ] && echo "Missing configuration: VISTA_APP_PROXY_ACCESS_CODE" && MISSING_SECRETS=true
[ -z "$VISTA_APP_PROXY_VERIFY_CODE" ] && echo "Missing configuration: VISTA_APP_PROXY_VERIFY_CODE" && MISSING_SECRETS=true
[ $MISSING_SECRETS == true ] && usage "Missing configuration secrets, please update $SECRETS"

makeConfig() {
  local project="$1"
  local profile="$2"
  local target="$REPO/$project/config/application-${profile}.properties"
  [ -f "$target" ] && mv -v $target $target.$MARKER
  grep -E '(.*= *unset)' "$REPO/$project/src/main/resources/application.properties" \
    > "$target"
}

addValue() {
  local project="$1"
  local profile="$2"
  local key="$3"
  local value="$4"
  local target="$REPO/$project/config/application-${profile}.properties"
  local escapedValue=$(echo $value | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')
  echo "$key=$escapedValue" >> $target
}

configValue() {
  local project="$1"
  local profile="$2"
  local key="$3"
  local value="$4"
  local target="$REPO/$project/config/application-${profile}.properties"
  local escapedValue=$(echo $value | sed -e 's/\\/\\\\/g; s/\//\\\//g; s/&/\\\&/g')
  sed -i "s/^$key=.*/$key=$escapedValue/" $target
}

checkForUnsetValues() {
  local project="$1"
  local profile="$2"
  local target="$REPO/$project/config/application-${profile}.properties"
  echo "checking $target"
  grep -E '(.*= *unset)' "$target"
  [ $? == 0 ] && echo "Failed to populate all unset values" && exit 1
  diff -q $target $target.$MARKER
  [ $? == 0 ] && rm -v $target.$MARKER
}

comment() {
  local project="$1"
  local profile="$2"
  local target="$REPO/$project/config/application-${profile}.properties"
  cat >> $target
}

ALT_IDS=(
  1337@666:1@673
  666@666:1@673
  1@666:1@673
)

altIds() { echo "$(IFS=",";echo "${ALT_IDS[*]}")"; }

makeConfig charon $PROFILE
addValue charon $PROFILE vistalink.configuration "config/vistalink-$PROFILE.properties"
configValue charon $PROFILE charon.rpc-principals.file "config/principals-$PROFILE.json"
configValue charon $PROFILE charon.rpc.client-keys "disabled"
configValue charon $PROFILE clinical-authorization-status.access-code "$VISTA_APP_PROXY_ACCESS_CODE"
configValue charon $PROFILE clinical-authorization-status.verify-code "$VISTA_APP_PROXY_VERIFY_CODE"
configValue charon $PROFILE clinical-authorization-status.application-proxy-user "$VISTA_APP_PROXY_USER"
configValue charon $PROFILE charon.public-web-exception-key "$WEB_EXCEPTION_KEY"
addValue charon $PROFILE alternate-authorization-status-ids.enabled true
addValue charon $PROFILE alternate-authorization-status-ids.ids "$(altIds)"

checkForUnsetValues charon $PROFILE

cat > $REPO/charon/config/vistalink-$PROFILE.properties <<EOF
673=localhost:18673:673:America/New_York
EOF
cat > $REPO/charon/config/principals-$PROFILE.json << EOF
{
    "entries" : [
        {
            "rpcNames" : [
                "LHS CHECK OPTION ACCESS"
            ],
            "applicationProxyUser" : "$VISTA_APP_PROXY_USER",
            "codes" : [
                {
                    "sites" : [
                        "673"
                    ],
                    "accessCode" : "$VISTA_APP_PROXY_ACCESS_CODE",
                    "verifyCode" : "$VISTA_APP_PROXY_VERIFY_CODE"
                }
            ]
        }
    ]
}
EOF
