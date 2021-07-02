#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat 1>&2 <<EOF 
Usage:
    ${0} [options]

Options:
    -f|--file     CSV file
    -h|--help     Display this menu
    -v|--version  Version of the CSV file
Examples:
    ${0} -f my-awesome.csv

${1:-OOF}
EOF
exit 1
}

init() {
  WORKING_DIR="$(readlink -f $(dirname ${0})/../..)"
}

main() {
  local longOpts='debug,file:,help,version:'
  local shortOpts='f:hv:'
  local args
  if ! args=$(getopt -n $(basename ${0}) \
    -l "${longOpts}" \
    -o "${shortOpts}" \
    -- "$@"); then usage "Ew, David!"; fi
  eval set -- "${args}"
  while true
  do
    case "${1}" in
      --debug) set -x;;
      -f|--file) CSV="${2}";;
      -h|--help) usage "Just don't. Don't even. I can't.";;
      -v|--version) VERSION="${2}";;
      --) shift; break;;
    esac
    shift
  done

  if [ -z "${CSV:-}" ]; then usage "-f|--file is a required option."; fi
  if [ -z "${VERSION:-}" ]; then usage "-v|--version is a required option."; fi
  
  while read filename
  do
    generateFilemanConstants "${filename}"
  done < <(cat ${CSV} | cut -d, -f 3 | grep -E '^[A-Z]{2}' | sort -u)
}

awkFunctions() {
  cat <<EOF
function toCamelCase(value) {
  gsub("[/]", " ", value)
  gsub("[^A-Za-z0-9 ]", "", value)
  split(value, words, " ")
  wordOut = ""
  for (i = 1; i <= length(words); i++) {
    wordOut = wordOut toupper(substr(words[i], 1, 1)) tolower(substr(words[i], 2))
  }
  return wordOut
}

function upperCaseWithUnderscores(value) {
  gsub("[^A-Za-z0-9]", "_", value)
  gsub("[_]+", "_", value)
  if (match(value, "^[0-9]") != 0) {
    return "N" toupper(value)
  }
  return toupper(value)
}
EOF
}

generateFilemanConstants() {
  local filename="${1:-}"
  local classname="$(echo ${filename} | awk -i <(awkFunctions) '{ print toCamelCase($0) }')"
  cat > ${WORKING_DIR}/src/main/java/gov/va/api/lighthouse/charon/models/lhslighthouserpcgateway/${classname}.java <<EOF
package gov.va.api.lighthouse.charon.models.lhslighthouserpcgateway;

import javax.annotation.processing.Generated;

@Generated(
    value = "charon-models/src/scripts/generate-fileman-constants.sh",
    date = "$(date --utc +%FT%XZ)",
    comments = "Generated using insuranceElementList.xlsx version ${VERSION}.")
public class ${classname} {
$(awk -F, -i <(awkFunctions) -v filename="${filename}" '
    BEGIN { filePrinted = "false" }
    /^\S/ {
      if (match($3, filename) == 0) {
        next
      }
      if (filePrinted == "false" && $2 != "File") {
        printf "public static final String FILE_NUMBER = \"%s\";\n", $2
        filePrinted = "true" 
      }
      if ( $14$16 != "" && toupper($10) != "DO NOT USE") {
        printf "public static final String %s = \"#%s\";\n", upperCaseWithUnderscores($5), $4
      }
    }' ${CSV})
}
EOF
}

init
main $@
