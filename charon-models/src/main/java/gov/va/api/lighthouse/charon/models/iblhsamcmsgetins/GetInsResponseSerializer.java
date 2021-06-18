package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.models.FilemanCoordinates;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "create")
public class GetInsResponseSerializer {

  /** Deserialize a GET INS RPC response string. */
  public GetInsRpcResults deserialize(String getInsEntries) {
    GetInsResponseSerializerConfig config = GetInsResponseSerializerConfigFactory.create();
    GetInsEntryParser.create()
        .parseNewLineDelimited(getInsEntries)
        .forEach(
            entry ->
                config
                    .filemanToJava()
                    .getOrDefault(
                        FilemanCoordinates.of(entry.fileNumber(), entry.fieldNumber()), e -> {})
                    .accept(entry));
    return config.results();
  }
}
