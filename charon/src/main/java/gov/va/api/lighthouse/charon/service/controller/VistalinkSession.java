package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import java.util.stream.Collectors;

public interface VistalinkSession extends AutoCloseable {
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
