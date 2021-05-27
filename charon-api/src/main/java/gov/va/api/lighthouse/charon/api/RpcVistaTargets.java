package gov.va.api.lighthouse.charon.api;

import static io.micrometer.core.instrument.util.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;

/** Contains input controlling the targets of an rpc request. */
@Data
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RpcVistaTargets {
  @Schema(
      format = "ICN",
      description = "If specified, consult MPI to determine VistA sites relevant to the patient. ")
  private String forPatient;

  @Schema(
      description = "If specified, invoke RPC at these VistA sites",
      format = "site or host:port:divisionIen:timezoneId",
      example = "673 or 10.10.10.10:18673:673:America/New_York")
  private List<String> include;

  @Schema(description = "If specified, do not invoke RPC at these VistA sites", format = "site")
  private List<String> exclude;

  /** Lazy initializer. */
  public List<String> exclude() {
    if (exclude == null) {
      exclude = new ArrayList<>();
    }
    return exclude;
  }

  /** Lazy initializer. */
  public List<String> include() {
    if (include == null) {
      include = new ArrayList<>();
    }
    return include;
  }

  @SuppressWarnings("unused")
  @JsonIgnore
  @AssertTrue
  private boolean isAtLeastOneTargetSpecified() {
    return isNotBlank(forPatient) || !include().isEmpty();
  }
}
