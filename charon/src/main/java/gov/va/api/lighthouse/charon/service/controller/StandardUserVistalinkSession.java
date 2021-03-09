package gov.va.api.lighthouse.charon.service.controller;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpecImpl;
import gov.va.med.vistalink.adapter.spi.EMAdapterEnvironment;
import gov.va.med.vistalink.adapter.spi.VistaLinkConnectionRequestInfo;
import gov.va.med.vistalink.adapter.spi.VistaLinkJ2SEConnSpec;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnection;
import gov.va.med.vistalink.security.m.KernelSecurityHandshake;
import gov.va.med.vistalink.security.m.KernelSecurityHandshakeManaged;
import gov.va.med.vistalink.security.m.SecurityResponseFactory;
import javax.resource.spi.ConnectionRequestInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StandardUserVistalinkSession implements VistalinkSession {

  @Getter private final String accessCode;
  @Getter private final String verifyCode;
  @Getter private final ConnectionDetails connectionDetails;

  private final SecurityResponseFactory securityResponseFactory;

  private transient CharonVistaLinkManagedConnectionFactory connectionFactory;
  private transient VistaLinkManagedConnection managedConnection;
  private transient VistaLinkConnection connection;

  @Builder
  private StandardUserVistalinkSession(
      @NonNull String accessCode,
      @NonNull String verifyCode,
      @NonNull ConnectionDetails connectionDetails) {
    this.accessCode = accessCode;
    this.verifyCode = verifyCode;
    this.connectionDetails = connectionDetails;
    this.securityResponseFactory = new SecurityResponseFactory();
  }

  @Override
  public void close() {
    closeConnection();
    closeManagedConnection();
    closeConnectionFactory();
  }

  @SneakyThrows
  private void closeConnection() {
    if (connection == null) {
      return;
    }
    try {
      KernelSecurityHandshake.doLogout(connection, securityResponseFactory);
    } catch (Exception e) {
      log.error("Failed to logout ({})", hashCode(), e);
    }
    try {
      connection.close();
    } catch (Exception e) {
      log.warn("Failed to closeconnection ({})", hashCode(), e);
    }
    connection = null;
  }

  private void closeConnectionFactory() {
    connectionFactory = null;
  }

  private void closeManagedConnection() {
    if (managedConnection == null) {
      return;
    }
    try {
      managedConnection.cleanup();
    } catch (Exception e) {
      log.warn("Failed to clean up managed connection ({})", hashCode(), e);
    }
    managedConnection = null;
  }

  @Override
  public VistaLinkConnection connection() {
    if (connection == null) {
      connection = createConnectionAndLogon();
    }
    return connection;
  }

  private CharonVistaLinkManagedConnectionFactory connectionFactory() {
    if (connectionFactory == null) {
      connectionFactory = new CharonVistaLinkManagedConnectionFactory();
      connectionFactory.setNonManagedHostPort(connectionDetails().port());
      connectionFactory.setNonManagedHostIPAddress(connectionDetails().host());
      connectionFactory.setAdapterEnvironment(EMAdapterEnvironment.J2SE);
    }
    return connectionFactory;
  }

  @SneakyThrows
  private VistaLinkConnection createConnectionAndLogon() {
    KernelSecurityHandshakeManaged.doSetupAndGetIntroText(
        managedConnection(), securityResponseFactory, false, true, "");

    VistaLinkConnectionSpecImpl connectionSpec =
        new VistaLinkJ2SEConnSpec(connectionDetails().divisionIen());
    ConnectionRequestInfo connectionRequest = new VistaLinkConnectionRequestInfo(connectionSpec);
    VistaLinkConnection connection =
        (VistaLinkConnection) managedConnection().getConnection(null, connectionRequest);

    KernelSecurityHandshake.doAVLogon(
        connection, securityResponseFactory, accessCode(), verifyCode(), false);

    return connection;
  }

  @SneakyThrows
  private VistaLinkManagedConnection managedConnection() {
    if (managedConnection == null) {
      managedConnection = new CharonVistaLinkManagedConnection(connectionFactory(), hashCode());
    }
    return managedConnection;
  }
}
