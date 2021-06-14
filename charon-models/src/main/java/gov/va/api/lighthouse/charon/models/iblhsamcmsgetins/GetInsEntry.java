package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Models the fields expected in the caret separated string from the IBLHS AMCMS GET INS RPC.
 *
 * <p>Expected string representation:
 *
 * <p>File#^Internal Entry Number^Field#^External format value^Internal format value
 */
@Value
@Builder
public class GetInsEntry {
  @NonNull String fileNumber;

  String ien;

  @NonNull String fieldNumber;

  String externalValueRepresentation;

  String internalValueRepresentation;
}
