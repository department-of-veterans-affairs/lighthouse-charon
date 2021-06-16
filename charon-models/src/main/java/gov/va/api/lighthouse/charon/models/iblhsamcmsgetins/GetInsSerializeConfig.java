package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.models.fileman.FilemanCoordinates;
import gov.va.api.lighthouse.charon.models.fileman.InsuranceCompanyFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.function.Consumer;

@Data
@Builder
@AllArgsConstructor
public class GetInsSerializeConfig {

    private InsuranceCompanyFile insuranceCompany;

    private final Map<FilemanCoordinates, Consumer<GetInsEntry>> filemanToJava = createMappings();

    private Map<FilemanCoordinates, Consumer<GetInsEntry>> createMappings() {
        return Map.of(
                FilemanCoordinates.of("36", ".01"), e -> insuranceCompany.name(e)
        );
    }

}
