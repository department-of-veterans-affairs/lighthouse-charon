package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.models.fileman.FilemanCoordinates;
import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class GetInsSerializer {

  public IblhsAmcmsGetIns.Response.Results deserialize(Collection<GetInsEntry> entries) {
    GetInsSerializeConfig config = GetInsSerializeConfigFactory.create();
    entries.forEach(
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
