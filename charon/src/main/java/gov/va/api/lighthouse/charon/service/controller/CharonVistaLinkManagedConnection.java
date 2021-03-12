package gov.va.api.lighthouse.charon.service.controller;

import gov.va.med.vistalink.adapter.cci.VistaLinkResourceException;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnection;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory;
import lombok.SneakyThrows;

/**
 * This class elevates permissions on the standard XOB VistaLink provided classes so that they can
 * be used outside of an EE context.
 */
public class CharonVistaLinkManagedConnection extends VistaLinkManagedConnection {

  @SneakyThrows
  public CharonVistaLinkManagedConnection(
      VistaLinkManagedConnectionFactory mcf, long distinguishedIdentifier)
      throws VistaLinkResourceException {
    super(mcf, distinguishedIdentifier);
    this.setSocketTimeOut(socketTimeout() + 1);
    this.getSocketConnection().setTransferTimeOut(socketTimeout() + 2);
  }

  /**
   * Charon-level socket time out that is used to override the Vistalink defaults. This is used by
   * adding a small number to it to help identify _which_ timeout is being used by VL since there
   * are so many, e.g., socketTimeout() + 2.
   */
  public static int socketTimeout() {
    return 30000;
  }
}
