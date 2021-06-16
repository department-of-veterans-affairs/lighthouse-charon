package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.models.fileman.FilemanCoordinates;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "create")
public class GetInsResponseSerializer {

  public IblhsAmcmsGetIns.Response.Results deserialize(String getInsEntries) {
    GetInsResponseSerializerConfig config = GetInsSerializerConfigFactory.create();
    GetInsEntryParser.create()
        .parseNewLineDelimited(getInsEntries)
        .forEach(
            entry ->
                config
                    .filemanToJava()
                    .get(FilemanCoordinates.of(entry.fileNumber(), entry.fieldNumber()))
                    .accept(entry));
    return IblhsAmcmsGetIns.Response.Results.builder()
        .insuranceCompany(config.insuranceCompany())
        .build();
  }
}
