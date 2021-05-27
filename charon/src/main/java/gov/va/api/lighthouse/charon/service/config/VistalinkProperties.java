package gov.va.api.lighthouse.charon.service.config;

import static java.util.stream.Collectors.toSet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import gov.va.api.lighthouse.charon.service.controller.VistaLinkExceptions.UnknownVista;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Value;

/** Contains connection information about all known vista sites. */
@Builder
@Value
@Schema
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class VistalinkProperties {
  @Schema List<ConnectionDetails> vistas;

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
