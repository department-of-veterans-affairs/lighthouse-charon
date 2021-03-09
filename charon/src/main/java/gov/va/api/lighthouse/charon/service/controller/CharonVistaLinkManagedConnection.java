package gov.va.api.lighthouse.charon.service.controller;

import gov.va.med.vistalink.adapter.cci.VistaLinkResourceException;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnection;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory;

/**
 * This class elevates permissions on the standard XOB VistaLink provided classes so that they can
 * be used outside of an EE context.
 */
public class CharonVistaLinkManagedConnection extends VistaLinkManagedConnection {

  public CharonVistaLinkManagedConnection(
      VistaLinkManagedConnectionFactory mcf, long distinguishedIdentifier)
      throws VistaLinkResourceException {
    super(mcf, distinguishedIdentifier);
  }
}
