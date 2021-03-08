#!/usr/bin/env bash
set -euo pipefail
# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

init() {
  test -n "${CLIENT_KEY}"
  test -n "${K8S_ENVIRONMENT}"
  test -n "${VISTA_ACCESS_CODE}"
  test -n "${VISTA_VERIFY_CODE}"

  if [ -z "${SENTINEL_BASE_DIR:-}" ]; then SENTINEL_BASE_DIR=/sentinel; fi
  cd $SENTINEL_BASE_DIR

  SYSTEM_PROPERTIES=()

  if [ -z "${SENTINEL_ENV:-}" ]; then SENTINEL_ENV=$K8S_ENVIRONMENT; fi
  if [ -z "${CHARON_URL:-}" ]; then CHARON_URL=https://$K8S_LOAD_BALANCER; fi
  if [ -z "${VISTA_AVAILABLE:-}" ]; then VISTA_AVAILABLE="true"; fi
}

main() {
  addToSystemProperties "sentinel" "${SENTINEL_ENV}"
  addToSystemProperties "sentinel.charon.url" "${CHARON_URL}"
  addToSystemProperties "client-key" "${CLIENT_KEY}"
  addToSystemProperties "vista.access-code" "${VISTA_ACCESS_CODE}"
  addToSystemProperties "vista.verify-code" "${VISTA_VERIFY_CODE}"
  addToSystemProperties "vista.is-available" "${VISTA_AVAILABLE}"

  java-tests \
    --module-name "charon-tests" \
    --regression-test-pattern ".*IT\$" \
    --smoke-test-pattern ".*PingIT\$" \
    ${SYSTEM_PROPERTIES[@]} \
    $@ \
    2>&1 | grep -v "WARNING: "

  exit $?
}

# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

addToSystemProperties() {
  SYSTEM_PROPERTIES+=("-D$1=$2")
}

# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

init
main $@
