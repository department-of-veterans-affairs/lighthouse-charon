package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import static org.apache.commons.lang3.StringUtils.trimToNull;

import gov.va.api.lighthouse.charon.models.EntryParser;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "create")
public class GetInsEntryParser implements EntryParser<GetInsEntry> {

  @Override
  public GetInsEntry parseLine(String entry) {
    if (entry == null) {
      throw new IllegalArgumentException("Cannot parse null entry.");
    }
    String[] caretSeparated = entry.split("\\^", -1);
    if (caretSeparated.length != 5) {
      throw new IllegalArgumentException(
          "Invalid Number of fields. Expected 5, got " + caretSeparated.length);
    }
    return GetInsEntry.builder()
        .fileNumber(trimToNull(caretSeparated[0]))
        .ien(trimToNull(caretSeparated[1]))
        .fieldNumber(trimToNull(caretSeparated[2]))
        .externalValueRepresentation(trimToNull(caretSeparated[3]))
        .internalValueRepresentation(trimToNull(caretSeparated[4]))
        .build();
  }
}
