package gov.va.api.lighthouse.charon.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;

/** Provides helper methods for searching all know RpcPrincipals. */
@Builder
@AllArgsConstructor
public class RpcPrincipals {
  RpcPrincipalConfig config;

  /** Return the principal for a given RPC name at a given vista site. */
  public RpcPrincipal findPrincipal(String rpcName, String site) {
    Optional<RpcPrincipalConfig.PrincipalEntry> entry =
        config.rpcPrincipals().stream()
            .filter(principalEntry -> principalEntry.rpcNames().contains(rpcName))
            .findFirst();
    if (entry.isEmpty()) {
      return null;
    }
    var code =
        entry.get().codes().stream().filter(codes -> codes.sites().contains(site)).findFirst();
    if (code.isEmpty()) {
      return null;
    }
    return RpcPrincipal.builder()
        .applicationProxyUser(entry.get().applicationProxyUser())
        .accessCode(code.get().accessCode())
        .verifyCode(code.get().verifyCode())
        .build();
  }

  /** Find all principals for a given RPC name. */
  public List<RpcPrincipal> findPrincipals(String rpcName) {
    Optional<RpcPrincipalConfig.PrincipalEntry> entry =
        config.rpcPrincipals().stream()
            .filter(principalEntry -> principalEntry.rpcNames().contains(rpcName))
            .findFirst();
    if (entry.isEmpty()) {
      return null;
    }
    return entry.get().codes().stream()
        .map(
            c ->
                RpcPrincipal.builder()
                    .applicationProxyUser(entry.get().applicationProxyUser())
                    .accessCode(c.accessCode())
                    .verifyCode(c.verifyCode())
                    .build())
        .collect(Collectors.toList());
  }
}
