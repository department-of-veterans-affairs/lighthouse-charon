package gov.va.api.lighthouse.charon.service.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("alternate-authorization-status-ids")
@Data
@Accessors(fluent = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlternateAuthorizationStatusIdProperties {

    /**
     * If enabled, the AlternateAuthorizationStatusIdMajig will inspect incoming ids to swap.
     */
    private boolean enabled;

    /**
     * Id mappings, Map of Public Id pair -> Alternate Id pair.
     * Ex. "duz123@444 : duz987:222".
     */
    private Map<String, String> id;
}
