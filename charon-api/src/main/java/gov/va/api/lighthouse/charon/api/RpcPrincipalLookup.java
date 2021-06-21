package gov.va.api.lighthouse.charon.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;

/** Provides helper methods for searching all know RpcPrincipals. */
@Builder
@AllArgsConstructor
public class RpcPrincipalLookup {
  RpcPrincipals rpcPrincipals;

  /** Find all principals for a given RPC name. */
  public List<RpcPrincipal> findAllPrincipalsByName(String rpcName) {
    var entries = findEntriesByName(rpcName);
    if (entries.isEmpty()) {
      return List.of();
    }
    List<RpcPrincipal> principals = new ArrayList<>();
    for (RpcPrincipals.PrincipalEntry e : entries) {
      String apu = e.applicationProxyUser();
      for (RpcPrincipals.Codes c : e.codes()) {
        principals.add(
            RpcPrincipal.builder()
                .applicationProxyUser(apu)
                .verifyCode(c.verifyCode())
                .accessCode(c.accessCode())
                .build());
      }
    }
    return principals;
  }

  private List<RpcPrincipals.PrincipalEntry> findEntriesByName(String rpcName) {
    return rpcPrincipals.entries().stream()
        .filter(principalEntry -> principalEntry.rpcNames().contains(rpcName))
        .collect(Collectors.toList());
  }

  /** Return the principal for a given RPC name at a given vista site. */
  public Optional<RpcPrincipal> findPrincipalByNameAndSite(String rpcName, String site) {
    var entries = findEntriesByName(rpcName);
    if (entries.isEmpty()) {
      return Optional.empty();
    }
    for (RpcPrincipals.PrincipalEntry e : entries) {
      for (RpcPrincipals.Codes c : e.codes()) {
        if (c.sites().contains(site)) {
          return Optional.of(
              RpcPrincipal.builder()
                  .applicationProxyUser(e.applicationProxyUser())
                  .accessCode(c.accessCode())
                  .verifyCode(c.verifyCode())
                  .build());
        }
      }
    }
    return Optional.empty();
  }
}
