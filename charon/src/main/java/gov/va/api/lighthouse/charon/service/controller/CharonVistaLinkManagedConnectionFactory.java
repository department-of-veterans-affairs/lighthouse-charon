package gov.va.api.lighthouse.charon.service.controller;

import static gov.va.api.lighthouse.charon.service.controller.CharonVistaLinkManagedConnection.socketTimeout;

import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory;
import java.io.Serial;

/**
 * This class elevates permissions on the standard XOB VistaLink provided classes so that they can
 * be used outside of an EE context.
 */
public class CharonVistaLinkManagedConnectionFactory extends VistaLinkManagedConnectionFactory {

  @Serial private static final long serialVersionUID = 7622503603723838369L;

  @Override
  public long getSocketTimeOut() {
    return socketTimeout() + 3;
  }

  @Override
  public void setPrimaryStation(String primaryStation) {
    super.setPrimaryStation(primaryStation);
  }
}
