package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.models.fileman.InsuranceCompanyFile;

public class GetInsSerializerConfigFactory {

    public static GetInsResponseSerializerConfig create() {
        return GetInsResponseSerializerConfig.builder()
                .insuranceCompany(InsuranceCompanyFile.empty())
                .build();
    }

}
