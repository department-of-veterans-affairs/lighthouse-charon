package gov.va.api.lighthouse.charon.service.controller;

import static java.util.stream.Collectors.toList;

import gov.va.api.lighthouse.charon.api.RpcVistaTargets;
import gov.va.api.lighthouse.charon.service.config.ConnectionDetails;
import gov.va.api.lighthouse.charon.service.config.VistalinkProperties;
import gov.va.api.lighthouse.charon.service.config.VistalinkPropertiesConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class NameResolution {
  @Getter private final VistalinkProperties properties;
  private final Function<RpcVistaTargets, Collection<String>> additionalCandidates;

  /**
   * Resolve the targets by applying the following rules.
   * <li>Include values are checked to see if they are names or specifications. Names are validated
   *     to be known. Specifications are parsed.
   * <li>Exclude values are validated to be known.
   * <li>Included names and additional candidate names are combined as a set of overall candidates.
   * <li>Any excluded names are removed the list.
   * <li>Any included full specifications are added.
   */
  public List<ConnectionDetails> resolve(RpcVistaTargets rpcVistaTargets) {
    List<ConnectionDetails> explicitlyDefined = new ArrayList<>();
    Set<String> vistas = new HashSet<>();

    if (!rpcVistaTargets.include().isEmpty()) {
      List<String> namedVistas = new ArrayList<>(rpcVistaTargets.include().size());
      for (String included : rpcVistaTargets.include()) {
        var details = VistalinkPropertiesConfig.parse(included);
        if (details.isEmpty()) {
          namedVistas.add(included);
        } else {
          explicitlyDefined.add(details.get());
        }
      }

      properties.checkKnownNames(namedVistas);
      vistas.addAll(namedVistas);
    }

    vistas.addAll(additionalCandidates.apply(rpcVistaTargets));

    if (!rpcVistaTargets.exclude().isEmpty()) {
      properties.checkKnownNames(rpcVistaTargets.exclude());
      vistas.removeAll(rpcVistaTargets.exclude());
    }

    var knownVistas = properties.names();
    vistas.removeIf(s -> !knownVistas.contains(s));
    log.info("Known Vistas: {}", knownVistas);
    return Stream.concat(
            explicitlyDefined.stream(),
            properties().vistas().stream().filter(c -> vistas.contains(c.name())))
        .collect(toList());
  }
}
