package gov.va.api.lighthouse.charon.service.config;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/** Defines a pairing of a users DUZ at a specific Vista site. */
@Value
@Builder
public class AuthorizationId {

  @NonNull String duz;

  @NonNull String site;

  /** Return a new authorization ID from a duz@site formatted string, e.g. 123456@517. */
  public static AuthorizationId of(String duzAtSite) {
    int at = duzAtSite.indexOf('@');
    /*  The @ separator is missing, at the beginning, or at the end of the word */
    if (at < 1 || at == duzAtSite.length() - 1) {
      throw badFormat();
    }
    String duz = duzAtSite.substring(0, at);
    String site = duzAtSite.substring(at + 1);
    return AuthorizationId.builder().duz(duz).site(site).build();
  }

  private static IllegalArgumentException badFormat() {
    return new IllegalArgumentException("Expected format duz@site, e.g. 31337@673");
  }

  @Override
  public String toString() {
    return duz + "@" + site;
  }
}
