package gov.va.api.lighthouse.charon.service.config;

import static java.util.stream.Collectors.toSet;

import gov.va.api.lighthouse.charon.service.controller.VistaLinkExceptions.UnknownVista;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class VistalinkProperties {
  @Singular List<ConnectionDetails> vistas;

  /** Thrown a UnknownVista exception if any of the candidate names are unknown. */
  public void checkKnownNames(List<String> candidateNames) {
    if (candidateNames == null || candidateNames.isEmpty()) {
      return;
    }
    var knownVistas = names();
    var unknownVistas =
        candidateNames.stream()
            .filter(include -> !knownVistas.contains(include))
            .collect(Collectors.toList());
    if (!unknownVistas.isEmpty()) {
      throw new UnknownVista(unknownVistas.toString());
    }
  }

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
