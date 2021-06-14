package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import gov.va.api.lighthouse.charon.models.EntryParser;
import java.util.Arrays;
import java.util.List;
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

  /** Parse several entries from a new-line delimited string. */
  public List<GetInsEntry> parseNewLineDelimited(String nldString) {
    if (nldString == null) {
      throw new IllegalArgumentException(
          "Cannot parse a new line delimited string with value: null");
    }
    return Arrays.stream(nldString.split("\\n", -1)).map(this::parseLine).collect(toList());
  }
}
