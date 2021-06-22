package gov.va.api.lighthouse.charon.api;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;

/** Provides helper methods for searching all know RpcPrincipals. */
@Builder
@AllArgsConstructor(staticName = "of")
public class RpcPrincipalLookup {
  RpcPrincipals rpcPrincipals;

  /** Find all principals for a given RPC name. */
  public Map<String, RpcPrincipal> findByName(String rpcName) {
    var entries = findEntriesByName(rpcName);
    if (entries.isEmpty()) {
      return Map.of();
    }
    Map<String, RpcPrincipal> principals = new HashMap<>();
    for (RpcPrincipals.PrincipalEntry e : entries) {
      String apu = e.applicationProxyUser();
      for (RpcPrincipals.Codes c : e.codes()) {
        for (String s : c.sites()) {
          principals.put(
              s,
              RpcPrincipal.builder()
                  .applicationProxyUser(apu)
                  .verifyCode(c.verifyCode())
                  .accessCode(c.accessCode())
                  .build());
        }
      }
    }
    return principals;
  }

  /** Return the principal for a given RPC name at a given vista site. */
  public Optional<RpcPrincipal> findByNameAndSite(String rpcName, String site) {
    if (isBlank(rpcName) || isBlank(site)) {
      return Optional.empty();
    }
    return Optional.ofNullable(findByName(rpcName).get(site));
  }

  private List<RpcPrincipals.PrincipalEntry> findEntriesByName(String rpcName) {
    return rpcPrincipals.entries().stream()
        .filter(principalEntry -> principalEntry.rpcNames().contains(rpcName))
        .collect(Collectors.toList());
  }
}
