package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Contains all known principal information. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RpcPrincipals {
  @NotEmpty private List<PrincipalEntry> entries;

  /** Lazy getter. */
  public List<PrincipalEntry> entries() {
    if (entries == null) {
      entries = new ArrayList<>();
    }
    return entries;
  }

  @AssertTrue(message = "RPC names must be unique across entries. ")
  @SuppressWarnings("unused")
  boolean isRpcNamesUnique() {
    Set<String> names = new HashSet<>();
    for (PrincipalEntry e : entries()) {
      for (String s : e.rpcNames()) {
        if (names.isEmpty()) {
          names.add(s);
        } else if (names.contains(s)) {
          return false;
        } else {
          names.add(s);
        }
      }
    }
    return true;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class PrincipalEntry {
    @NotEmpty private List<String> rpcNames;

    @NotNull @NotBlank private String applicationProxyUser;

    @NotEmpty private List<Codes> codes;

    /** Lazy getter. */
    public List<Codes> codes() {
      if (codes == null) {
        codes = new ArrayList<>();
      }
      return codes;
    }

    @AssertTrue(message = "Sites must be unique across codes per entry. ")
    @SuppressWarnings("unused")
    boolean isSitesUniqueWithinCodes() {
      Set<String> sites = new HashSet<>();
      for (Codes c : codes()) {
        for (String s : c.sites()) {
          if (sites.isEmpty()) {
            sites.add(s);
          } else if (sites.contains(s)) {
            return false;
          } else {
            sites.add(s);
          }
        }
      }
      return true;
    }

    /** Lazy getter. */
    public List<String> rpcNames() {
      if (rpcNames == null) {
        rpcNames = new ArrayList<>();
      }
      return rpcNames;
    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public static class Codes {
    @NotEmpty private List<String> sites;

    @NotBlank private String accessCode;

    @NotBlank private String verifyCode;

    /** Lazy getter. */
    public List<String> sites() {
      if (sites == null) {
        sites = new ArrayList<>();
      }
      return sites;
    }
  }
}
