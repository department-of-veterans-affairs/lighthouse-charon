package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.stream.Collectors;

public interface VistalinkSession extends AutoCloseable {

  /**
   * Attempt to generate a connection indentifier from the given details. This will attempt to
   * generate a number that includes the connection name, assuming the name includes numeric values.
   * For example, if the name is "673" the identifier will be 673. If the name is "673a" then the
   * identifier will be 67397. The non-digit characters are converted to their ascii numberic
   * values, e.g. a => 97. If this fails, the hashCode of the details will be used.
   */
  static long connectionIdentifier(ConnectionDetails cd) {
    String maybeLong =
        cd.name()
            .chars()
            .mapToObj(
                i -> {
                  char c = (char) i;
                  return Character.isDigit(c) ? String.valueOf(c) : String.valueOf(i);
                })
            .collect(Collectors.joining());
    long definitelyLong;
    try {
      definitelyLong = Long.parseLong(maybeLong);
    } catch (NumberFormatException e) {
      definitelyLong = cd.hashCode();
    }
    return definitelyLong;
  }

  @Override
  void close();

  VistaLinkConnection connection();
}
