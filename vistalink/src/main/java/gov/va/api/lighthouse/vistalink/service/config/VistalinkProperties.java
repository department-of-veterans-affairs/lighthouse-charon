package gov.va.api.lighthouse.vistalink.service.config;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class VistalinkProperties {
  @Singular List<ConnectionDetails> vistas;

  /** Return a set of names for all known vista instances. */
  public Set<String> names() {
    return vistas.stream().map(ConnectionDetails::name).collect(toSet());
  }

  /** Always return a list of vistas. This is potentially empty. */
  public List<ConnectionDetails> vistas() {
    if (vistas == null) {
      return List.of();
    }
    return vistas;
  }
}
