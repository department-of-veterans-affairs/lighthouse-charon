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
  ARGS=$(getopt -n $(basename ${0}) \
    -l "${longOpts}" \
    -o "${shortOpts}" \
    -- "$@")
  eval set -- "${ARGS}"
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
  
  generateRpcModel > ${WORKING_DIR}/src/main/java/gov/va/api/lighthouse/charon/models/iblhsamcmsgetins/GetInsRpcResults.java
  generateGetInsConfig > ${WORKING_DIR}/src/main/java/gov/va/api/lighthouse/charon/models/iblhsamcmsgetins/GetInsResponseSerializerConfig.java
}

awkFunctions() {
  cat <<EOF
function toCamelCase(value) {
  gsub("[^A-Za-z0-9 ]", "", value)
  split(value, words, " ")
  wordOut = ""
  for (i = 1; i <= length(words); i++) {
    wordOut = wordOut toupper(substr(words[i], 1, 1)) tolower(substr(words[i], 2))
  }
  return wordOut
}

function prefix(fileNumber) {
  switch(fileNumber) {
    case "2.312":
      # INSURANCE TYPE
      return "insType"
    case "2.322":
      # ELIGIBILITY/BENEFIT
      return "elBenefit"
    case "355.3":
      # GROUP INSURANCE PLAN
      return  "groupPlan"
    case "355.32":
      # PLAN COVERAGE LIMITATIONS
      return "limitations"
    case "36":
      # INSURANCE COMPANY
      return "insCo"
  }
}
EOF
}

generateGetInsConfig() {
  cat <<EOF
package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.models.FilemanCoordinates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Serialization configuration for IBLHS AMCMS GET INS RPC results.
 * Generated using insuranceElementList.xlsx version ${VERSION}.
 */
@Data
@Builder
@AllArgsConstructor
public class GetInsResponseSerializerConfig {

    private GetInsRpcResults results;

    private final Map<FilemanCoordinates, Consumer<GetInsEntry>> filemanToJava = createMappings();

    private Map<FilemanCoordinates, Consumer<GetInsEntry>> createMappings() {
        Map<FilemanCoordinates, Consumer<GetInsEntry>> mappings = new HashMap<>();
$(awk -F, -i <(awkFunctions) -v last="$(cat ${CSV} | wc -l)" '
  /^\S/ {
    if ( $14$16 != "" && toupper($10) != "DO NOT USE") {
      printf "mappings.put(FilemanCoordinates.of(\"%s\", \"%s\"), e -> results.%s%s(e));\n", $2, $4, prefix($2), toCamelCase($5)
    }
  }' ${CSV})
        return Map.copyOf(mappings);
      
    }
}
EOF
}

generateRpcModel() {
  cat <<EOF
package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * Response model for the IBLHS AMCMS GET INS RPC.
 * Generated using insuranceElementList.xlsx version ${VERSION}.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(staticName = "empty")
public class GetInsRpcResults {
$(awk -F',' -i <(awkFunctions) '
  /^\S/ {
    if ( $14$16 != "" && toupper($10) != "DO NOT USE") {
      printf "/** %s %s. */\n", NR, $16  
      printf "private GetInsEntry %s%s;\n", prefix($2), toCamelCase($5)
    }
  }' ${CSV})
}
EOF
}

init
main $@

