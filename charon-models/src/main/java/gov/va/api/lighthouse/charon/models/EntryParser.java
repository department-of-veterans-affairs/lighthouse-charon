package gov.va.api.lighthouse.charon.models;

public interface EntryParser<E> {

  /** Defines how to parse a single entry. */
  E parseLine(String entry);
}
