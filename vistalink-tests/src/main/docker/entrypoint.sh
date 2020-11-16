#!/usr/bin/env bash

set -euo pipefail

if [ -z "${SENTINEL_BASE_DIR:-}" ]; then SENTINEL_BASE_DIR=/sentinel; fi
cd $SENTINEL_BASE_DIR

test -n "${K8S_ENVIRONMENT}"
test -n "${CLIENT_KEY}"
if [ -z "${SENTINEL_ENV:-}" ]; then SENTINEL_ENV=$K8S_ENVIRONMENT; fi
if [ -z "${VISTALINK_URL:-}" ]; then VISTALINK_URL=https://$K8S_LOAD_BALANCER; fi

java-tests \
  --module-name "vistalink-tests" \
  --regression-test-pattern ".*IT\$" \
  --smoke-test-pattern ".*PingIT\$" \
  -Dsentinel="$SENTINEL_ENV" \
  -Dsentinel.vistalink.url=$VISTALINK_URL \
  -Dvista.access-code="$VISTA_ACCESS_CODE" \
  -Dvista.verify-code="$VISTA_VERIFY_CODE" \
  -Dclient-key="${CLIENT_KEY}" \
  $@ \
  2>&1 | grep -v "WARNING: "

exit $?
