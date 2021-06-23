package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
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

  /**
   * Turn a collection if things in to a bigger collection of Strings and verify that each string is
   * unique.
   */
  private static <T> boolean isUnique(Iterable<T> items, Function<T, Iterable<String>> valuesOf) {
    Set<String> uniqueValues = new HashSet<>();
    for (T item : items) {
      for (String value : valuesOf.apply(item)) {
        if (!uniqueValues.add(value)) {
          return false;
        }
      }
    }
    return true;
  }

  /** Lazy getter. */
  public List<PrincipalEntry> entries() {
    if (entries == null) {
      entries = new ArrayList<>();
    }
    return entries;
  }

  @AssertTrue(message = "RPC names must be unique across entries. ")
  @SuppressWarnings("unused")
  boolean isEachRpcNameUnique() {
    return isUnique(entries(), PrincipalEntry::rpcNames);
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
      return isUnique(codes(), Codes::sites);
    }

    /** Lazy getter. */
    public List<String> rpcNames() {
      if (rpcNames == null) {
        rpcNames = new ArrayList<>();
      }
      return rpcNames;
    }
  }
}
