package gov.va.api.lighthouse.charon.service.controller;

import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;

public interface VistalinkSession extends AutoCloseable {
  @Override
  void close();

  VistaLinkConnection connection();
}
