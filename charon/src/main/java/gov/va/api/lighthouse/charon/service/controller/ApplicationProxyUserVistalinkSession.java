package gov.va.api.lighthouse.charon.service.controller;

import static gov.va.api.lighthouse.charon.service.controller.VistalinkSession.connectionIdentifier;

import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.med.vistalink.adapter.cci.VistaLinkAppProxyConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpecImpl;
import gov.va.med.vistalink.adapter.spi.EMAdapterEnvironment;
import gov.va.med.vistalink.adapter.spi.VistaLinkConnectionRequestInfo;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnection;
import gov.va.med.vistalink.security.m.KernelSecurityHandshake;
import gov.va.med.vistalink.security.m.SecurityResponseFactory;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationProxyUserVistalinkSession implements VistalinkSession {

  @Getter private final String applicationProxyUser;
  @Getter private final String accessCode;
  @Getter private final String verifyCode;
  @Getter private final ConnectionDetails connectionDetails;

  private transient CharonVistaLinkManagedConnectionFactory connectionFactory;
  private transient VistaLinkManagedConnection managedConnection;
  private transient VistaLinkConnection connection;

  @Builder
  private ApplicationProxyUserVistalinkSession(
      @NonNull String applicationProxyUser,
      @NonNull String accessCode,
      @NonNull String verifyCode,
      @NonNull ConnectionDetails connectionDetails) {
    this.applicationProxyUser = applicationProxyUser;
    this.accessCode = accessCode;
    this.verifyCode = verifyCode;
    this.connectionDetails = connectionDetails;
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
      KernelSecurityHandshake.doLogout(connection, new SecurityResponseFactory());
    } catch (Exception e) {
      log.error("Failed to logout ({})", hashCode(), e);
    }
    try {
      connection.close();
    } catch (ResourceException e) {
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
    } catch (ResourceException e) {
      log.warn("Failed to clean up managed connection ({})", hashCode(), e);
    }
    try {
      managedConnection.destroy();
    } catch (ResourceException e) {
      log.warn("Failed to destroy managed connection ({})", hashCode(), e);
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
      connectionFactory.setPrimaryStation(connectionDetails.divisionIen());
      connectionFactory.setNonManagedHostPort(connectionDetails().port());
      connectionFactory.setNonManagedHostIPAddress(connectionDetails().host());
      connectionFactory.setNonManagedAccessCode(accessCode());
      connectionFactory.setNonManagedVerifyCode(verifyCode());
      connectionFactory.setAdapterEnvironment(EMAdapterEnvironment.J2EE);
    }
    return connectionFactory;
  }

  @SneakyThrows
  private VistaLinkConnection createConnectionAndLogon() {
    VistaLinkConnectionSpecImpl connectionSpec =
        new VistaLinkAppProxyConnectionSpec(
            connectionDetails().divisionIen(), applicationProxyUser());
    ConnectionRequestInfo connectionRequest = new VistaLinkConnectionRequestInfo(connectionSpec);
    VistaLinkConnection connection =
        (VistaLinkConnection) managedConnection().getConnection(null, connectionRequest);
    return connection;
  }

  @SneakyThrows
  private VistaLinkManagedConnection managedConnection() {
    if (managedConnection == null) {
      managedConnection =
          new CharonVistaLinkManagedConnection(
              connectionFactory(), connectionIdentifier(connectionDetails));
    }
    return managedConnection;
  }
}
