package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.models.fileman.InsuranceCompanyFile;

public class GetInsSerializeConfigFactory {

    public static GetInsSerializeConfig create() {
        return GetInsSerializeConfig.builder()
                .insuranceCompany(InsuranceCompanyFile.empty())
                .build();
    }

}
