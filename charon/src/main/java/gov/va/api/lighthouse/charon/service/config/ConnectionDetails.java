package gov.va.api.lighthouse.charon.service.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

/** Data needed for connecting to a vista instance. */
@Value
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ConnectionDetails {
  @Schema(description = "Charon name of the Vista, usually the site.", example = "673")
  String name;

  @Schema(description = "The host name or IP address")
  String host;

  @Schema(description = "The Vista port")
  int port;

  @Schema(description = "The Division IEN, which is usually the site")
  String divisionIen;

  @Schema(format = "timezone ID", example = "America/New_York")
  String timezone;
}
