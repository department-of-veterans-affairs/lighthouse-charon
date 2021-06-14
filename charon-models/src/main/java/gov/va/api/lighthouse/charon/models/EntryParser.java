package gov.va.api.lighthouse.charon.models;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

public interface EntryParser<E> {

  /** Defines how to parse a single entry. */
  E parseLine(String entry);

  /** Parse several entries from a new-line delimited string. */
  default List<E> parseNewLineDelimited(String nldString) {
    if (nldString == null) {
      throw new IllegalArgumentException(
          "Cannot parse a new line delimited string with value: null");
    }
    return Arrays.stream(nldString.split("\\n", -1)).map(this::parseLine).collect(toList());
  }
}
