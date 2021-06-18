package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import gov.va.api.lighthouse.charon.models.FilemanCoordinates;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Serialization configuration for IBLHS AMCMS GET INS RPC results. Generated using
 * insuranceElementList.xlsx version 183.0.
 */
@Data
@Builder
@AllArgsConstructor
public class GetInsResponseSerializerConfig {
  private final Map<FilemanCoordinates, Consumer<GetInsEntry>> filemanToJava = createMappings();

  private GetInsRpcResults results;

  private Map<FilemanCoordinates, Consumer<GetInsEntry>> createMappings() {
    Map<FilemanCoordinates, Consumer<GetInsEntry>> mappings = new HashMap<>();
    mappings.put(FilemanCoordinates.of("2.312", ".01"), e -> results.insTypeInsuranceType(e));
    mappings.put(FilemanCoordinates.of("2.312", ".18"), e -> results.insTypeGroupPlan(e));
    mappings.put(
        FilemanCoordinates.of("2.312", ".2"), e -> results.insTypeCoordinationOfBenefits(e));
    mappings.put(FilemanCoordinates.of("2.312", "1.03"), e -> results.insTypeDateLastVerified(e));
    mappings.put(FilemanCoordinates.of("2.312", "2.01"), e -> results.insTypeSendBillToEmployer(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "2.015"), e -> results.insTypeSubscribersEmployerName(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "2.02"), e -> results.insTypeEmployerClaimsStreetAddress(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "2.03"), e -> results.insTypeEmployClaimStAddressLine2(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "2.04"), e -> results.insTypeEmployClaimStAddressLine3(e));
    mappings.put(FilemanCoordinates.of("2.312", "2.05"), e -> results.insTypeEmployerClaimsCity(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "2.06"), e -> results.insTypeEmployerClaimsState(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "2.07"), e -> results.insTypeEmployerClaimsZipCode(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "2.08"), e -> results.insTypeEmployerClaimsPhone(e));
    mappings.put(FilemanCoordinates.of("2.312", "2.1"), e -> results.insTypeEsghp(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "3"), e -> results.insTypeInsuranceExpirationDate(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.01"), e -> results.insTypeInsuredsDob(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.02"), e -> results.insTypeInsuredsBranch(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.03"), e -> results.insTypeInsuredsRank(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "3.04"), e -> results.insTypeStopPolicyFromBilling(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.05"), e -> results.insTypeInsuredsSsn(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.06"), e -> results.insTypeInsuredsStreet1(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.07"), e -> results.insTypeInsuredsStreet2(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.08"), e -> results.insTypeInsuredsCity(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.09"), e -> results.insTypeInsuredsState(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.1"), e -> results.insTypeInsuredsZip(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.11"), e -> results.insTypeInsuredsPhone(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.12"), e -> results.insTypeInsuredsSex(e));
    mappings.put(FilemanCoordinates.of("2.312", "3.13"), e -> results.insTypeInsuredsCountry(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "3.14"), e -> results.insTypeInsuredsCountrySubdivision(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "4.01"), e -> results.insTypePrimaryCareProvider(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "4.02"), e -> results.insTypePrimaryProviderPhone(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "4.03"), e -> results.insTypePtRelationshipHipaa(e));
    mappings.put(FilemanCoordinates.of("2.312", "4.06"), e -> results.insTypePharmacyPersonCode(e));
    mappings.put(FilemanCoordinates.of("2.312", "5.01"), e -> results.insTypePatientId(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.02"), e -> results.insTypeSubscribersSecQualifier1(e));
    mappings.put(FilemanCoordinates.of("2.312", "5.03"), e -> results.insTypeSubscribersSecId1(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.04"), e -> results.insTypeSubscribersSecQualifier2(e));
    mappings.put(FilemanCoordinates.of("2.312", "5.05"), e -> results.insTypeSubscribersSecId2(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.06"), e -> results.insTypeSubscribersSecQualifier3(e));
    mappings.put(FilemanCoordinates.of("2.312", "5.07"), e -> results.insTypeSubscribersSecId3(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.08"), e -> results.insTypePatientsSecQualifier1(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.09"), e -> results.insTypePatientsSecondaryId1(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.1"), e -> results.insTypePatientsSecQualifier2(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.11"), e -> results.insTypePatientsSecondaryId2(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.12"), e -> results.insTypePatientsSecQualifier3(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "5.13"), e -> results.insTypePatientsSecondaryId3(e));
    mappings.put(FilemanCoordinates.of("2.312", "6"), e -> results.insTypeWhoseInsurance(e));
    mappings.put(FilemanCoordinates.of("2.312", "7.01"), e -> results.insTypeNameOfInsured(e));
    mappings.put(FilemanCoordinates.of("2.312", "7.02"), e -> results.insTypeSubscriberId(e));
    mappings.put(FilemanCoordinates.of("2.312", "8"), e -> results.insTypeEffectiveDateOfPolicy(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "8.02"), e -> results.insTypeRequestedServiceType(e));
    mappings.put(
        FilemanCoordinates.of("2.312", "16"), e -> results.insTypePtRelationshipToInsured(e));
    mappings.put(FilemanCoordinates.of("2.312", "60"), e -> results.insTypeEligibilitybenefit(e));
    mappings.put(
        FilemanCoordinates.of("2.322", ".02"), e -> results.elBenefitEligibilitybenefitInfo(e));
    mappings.put(FilemanCoordinates.of("2.322", ".03"), e -> results.elBenefitCoverageLevel(e));
    mappings.put(FilemanCoordinates.of("36", ".01"), e -> results.insCoName(e));
    mappings.put(FilemanCoordinates.of("36", ".06"), e -> results.insCoAllowMultipleBedsections(e));
    mappings.put(
        FilemanCoordinates.of("36", ".07"), e -> results.insCoDifferentRevenueCodesToUse(e));
    mappings.put(FilemanCoordinates.of("36", ".08"), e -> results.insCoOneOptVisitOnBillOnly(e));
    mappings.put(FilemanCoordinates.of("36", ".09"), e -> results.insCoAmbulatorySurgRevCode(e));
    mappings.put(FilemanCoordinates.of("36", ".111"), e -> results.insCoStreetAddressLine1(e));
    mappings.put(FilemanCoordinates.of("36", ".112"), e -> results.insCoStreetAddressLine2(e));
    mappings.put(FilemanCoordinates.of("36", ".113"), e -> results.insCoStreetAddressLine3(e));
    mappings.put(FilemanCoordinates.of("36", ".114"), e -> results.insCoCity(e));
    mappings.put(FilemanCoordinates.of("36", ".115"), e -> results.insCoState(e));
    mappings.put(FilemanCoordinates.of("36", ".116"), e -> results.insCoZipCode(e));
    mappings.put(FilemanCoordinates.of("36", ".117"), e -> results.insCoBillingCompanyName(e));
    mappings.put(FilemanCoordinates.of("36", ".119"), e -> results.insCoFaxNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".12"), e -> results.insCoFilingTimeFrame(e));
    mappings.put(
        FilemanCoordinates.of("36", ".121"), e -> results.insCoClaimsInptStreetAddress1(e));
    mappings.put(
        FilemanCoordinates.of("36", ".122"), e -> results.insCoClaimsInptStreetAddress2(e));
    mappings.put(
        FilemanCoordinates.of("36", ".123"), e -> results.insCoClaimsInptStreetAddress3(e));
    mappings.put(FilemanCoordinates.of("36", ".124"), e -> results.insCoClaimsInptProcessCity(e));
    mappings.put(FilemanCoordinates.of("36", ".125"), e -> results.insCoClaimsInptProcessState(e));
    mappings.put(FilemanCoordinates.of("36", ".126"), e -> results.insCoClaimsInptProcessZip(e));
    mappings.put(FilemanCoordinates.of("36", ".127"), e -> results.insCoClaimsInptCompanyName(e));
    mappings.put(
        FilemanCoordinates.of("36", ".128"), e -> results.insCoAnotherCoProcessIpClaims(e));
    mappings.put(FilemanCoordinates.of("36", ".129"), e -> results.insCoClaimsInptFax(e));
    mappings.put(FilemanCoordinates.of("36", ".13"), e -> results.insCoTypeOfCoverage(e));
    mappings.put(FilemanCoordinates.of("36", ".131"), e -> results.insCoPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".1311"), e -> results.insCoClaimsRxPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".132"), e -> results.insCoBillingPhoneNumber(e));
    mappings.put(
        FilemanCoordinates.of("36", ".133"), e -> results.insCoPrecertificationPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".134"), e -> results.insCoVerificationPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".135"), e -> results.insCoClaimsInptPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".136"), e -> results.insCoClaimsOptPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".137"), e -> results.insCoAppealsPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".138"), e -> results.insCoInquiryPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".139"), e -> results.insCoPrecertCompanyName(e));
    mappings.put(FilemanCoordinates.of("36", ".141"), e -> results.insCoAppealsAddressStLine1(e));
    mappings.put(FilemanCoordinates.of("36", ".142"), e -> results.insCoAppealsAddressStLine2(e));
    mappings.put(FilemanCoordinates.of("36", ".143"), e -> results.insCoAppealsAddressStLine3(e));
    mappings.put(FilemanCoordinates.of("36", ".144"), e -> results.insCoAppealsAddressCity(e));
    mappings.put(FilemanCoordinates.of("36", ".145"), e -> results.insCoAppealsAddressState(e));
    mappings.put(FilemanCoordinates.of("36", ".146"), e -> results.insCoAppealsAddressZip(e));
    mappings.put(FilemanCoordinates.of("36", ".147"), e -> results.insCoAppealsCompanyName(e));
    mappings.put(FilemanCoordinates.of("36", ".148"), e -> results.insCoAnotherCoProcessAppeals(e));
    mappings.put(FilemanCoordinates.of("36", ".149"), e -> results.insCoAppealsFax(e));
    mappings.put(
        FilemanCoordinates.of("36", ".15"), e -> results.insCoPrescriptionRefillRevCode(e));
    mappings.put(FilemanCoordinates.of("36", ".151"), e -> results.insCoInquiryAddressStLine1(e));
    mappings.put(FilemanCoordinates.of("36", ".152"), e -> results.insCoInquiryAddressStLine2(e));
    mappings.put(FilemanCoordinates.of("36", ".153"), e -> results.insCoInquiryAddressStLine3(e));
    mappings.put(FilemanCoordinates.of("36", ".154"), e -> results.insCoInquiryAddressCity(e));
    mappings.put(FilemanCoordinates.of("36", ".155"), e -> results.insCoInquiryAddressState(e));
    mappings.put(FilemanCoordinates.of("36", ".156"), e -> results.insCoInquiryAddressZipCode(e));
    mappings.put(FilemanCoordinates.of("36", ".157"), e -> results.insCoInquiryCompanyName(e));
    mappings.put(
        FilemanCoordinates.of("36", ".158"), e -> results.insCoAnotherCoProcessInquiries(e));
    mappings.put(FilemanCoordinates.of("36", ".159"), e -> results.insCoInquiryFax(e));
    mappings.put(FilemanCoordinates.of("36", ".161"), e -> results.insCoClaimsOptStreetAddress1(e));
    mappings.put(FilemanCoordinates.of("36", ".162"), e -> results.insCoClaimsOptStreetAddress2(e));
    mappings.put(FilemanCoordinates.of("36", ".163"), e -> results.insCoClaimsOptStreetAddress3(e));
    mappings.put(FilemanCoordinates.of("36", ".164"), e -> results.insCoClaimsOptProcessCity(e));
    mappings.put(FilemanCoordinates.of("36", ".165"), e -> results.insCoClaimsOptProcessState(e));
    mappings.put(FilemanCoordinates.of("36", ".166"), e -> results.insCoClaimsOptProcessZip(e));
    mappings.put(FilemanCoordinates.of("36", ".167"), e -> results.insCoClaimsOptCompanyName(e));
    mappings.put(
        FilemanCoordinates.of("36", ".168"), e -> results.insCoAnotherCoProcessOpClaims(e));
    mappings.put(FilemanCoordinates.of("36", ".169"), e -> results.insCoClaimsOptFax(e));
    mappings.put(
        FilemanCoordinates.of("36", ".17"), e -> results.insCoProfessionalProviderNumber(e));
    mappings.put(
        FilemanCoordinates.of("36", ".178"), e -> results.insCoAnotherCoProcessPrecerts(e));
    mappings.put(FilemanCoordinates.of("36", ".18"), e -> results.insCoStandardFtf(e));
    mappings.put(FilemanCoordinates.of("36", ".181"), e -> results.insCoClaimsRxStreetAddress1(e));
    mappings.put(FilemanCoordinates.of("36", ".182"), e -> results.insCoClaimsRxStreetAddress2(e));
    mappings.put(FilemanCoordinates.of("36", ".183"), e -> results.insCoClaimsRxStreetAddress3(e));
    mappings.put(FilemanCoordinates.of("36", ".184"), e -> results.insCoClaimsRxCity(e));
    mappings.put(FilemanCoordinates.of("36", ".185"), e -> results.insCoClaimsRxState(e));
    mappings.put(FilemanCoordinates.of("36", ".186"), e -> results.insCoClaimsRxZip(e));
    mappings.put(FilemanCoordinates.of("36", ".187"), e -> results.insCoClaimsRxCompanyName(e));
    mappings.put(
        FilemanCoordinates.of("36", ".188"), e -> results.insCoAnotherCoProcessRxClaims(e));
    mappings.put(FilemanCoordinates.of("36", ".189"), e -> results.insCoClaimsRxFax(e));
    mappings.put(FilemanCoordinates.of("36", ".19"), e -> results.insCoStandardFtfValue(e));
    mappings.put(FilemanCoordinates.of("36", ".191"), e -> results.insCoClaimsDentalStreetAddr1(e));
    mappings.put(
        FilemanCoordinates.of("36", ".1911"), e -> results.insCoClaimsDentalPhoneNumber(e));
    mappings.put(FilemanCoordinates.of("36", ".192"), e -> results.insCoClaimsDentalStreetAddr2(e));
    mappings.put(FilemanCoordinates.of("36", ".193"), e -> results.insCoBlank(e));
    mappings.put(FilemanCoordinates.of("36", ".194"), e -> results.insCoClaimsDentalProcessCity(e));
    mappings.put(
        FilemanCoordinates.of("36", ".195"), e -> results.insCoClaimsDentalProcessState(e));
    mappings.put(FilemanCoordinates.of("36", ".196"), e -> results.insCoClaimsDentalProcessZip(e));
    mappings.put(FilemanCoordinates.of("36", ".197"), e -> results.insCoClaimsDentalCompanyName(e));
    mappings.put(FilemanCoordinates.of("36", ".198"), e -> results.insCoAnotherCoProcDentClaims(e));
    mappings.put(FilemanCoordinates.of("36", ".199"), e -> results.insCoClaimsDentalFax(e));
    mappings.put(FilemanCoordinates.of("36", "1"), e -> results.insCoReimburse(e));
    mappings.put(FilemanCoordinates.of("36", "2"), e -> results.insCoSignatureRequiredOnBill(e));
    mappings.put(FilemanCoordinates.of("36", "3.01"), e -> results.insCoTransmitElectronically(e));
    mappings.put(FilemanCoordinates.of("36", "3.02"), e -> results.insCoEdiIdNumberProf(e));
    mappings.put(FilemanCoordinates.of("36", "3.03"), e -> results.insCoBinNumber(e));
    mappings.put(FilemanCoordinates.of("36", "3.04"), e -> results.insCoEdiIdNumberInst(e));
    mappings.put(FilemanCoordinates.of("36", "3.09"), e -> results.insCoElectronicInsuranceType(e));
    mappings.put(FilemanCoordinates.of("36", "3.1"), e -> results.insCoPayer(e));
    mappings.put(FilemanCoordinates.of("36", "3.15"), e -> results.insCoEdiIdNumberDental(e));
    mappings.put(
        FilemanCoordinates.of("36", "4.01"), e -> results.insCoPerfProvSecondIdType1500(e));
    mappings.put(FilemanCoordinates.of("36", "4.02"), e -> results.insCoPerfProvSecondIdTypeUb(e));
    mappings.put(FilemanCoordinates.of("36", "4.03"), e -> results.insCoSecondaryIdRequirements(e));
    mappings.put(FilemanCoordinates.of("36", "4.04"), e -> results.insCoRefProvSecIdDefCms1500(e));
    mappings.put(FilemanCoordinates.of("36", "4.05"), e -> results.insCoRefProvSecIdReqOnClaims(e));
    mappings.put(FilemanCoordinates.of("36", "4.06"), e -> results.insCoAttrendIdBillSecIdProf(e));
    mappings.put(FilemanCoordinates.of("36", "4.08"), e -> results.insCoAttrendIdBillSecIdInst(e));
    mappings.put(FilemanCoordinates.of("36", "6.01"), e -> results.insCoEdiInstSecondaryIdQual1(e));
    mappings.put(FilemanCoordinates.of("36", "6.02"), e -> results.insCoEdiInstSecondaryId1(e));
    mappings.put(FilemanCoordinates.of("36", "6.03"), e -> results.insCoEdiInstSecondaryIdQual2(e));
    mappings.put(FilemanCoordinates.of("36", "6.04"), e -> results.insCoEdiInstSecondaryId2(e));
    mappings.put(FilemanCoordinates.of("36", "6.05"), e -> results.insCoEdiProfSecondaryIdQual1(e));
    mappings.put(FilemanCoordinates.of("36", "6.06"), e -> results.insCoEdiProfSecondaryId1(e));
    mappings.put(FilemanCoordinates.of("36", "6.07"), e -> results.insCoEdiProfSecondaryIdQual2(e));
    mappings.put(FilemanCoordinates.of("36", "6.08"), e -> results.insCoEdiProfSecondaryId2(e));
    mappings.put(FilemanCoordinates.of("36", "6.09"), e -> results.insCoPrintSectertAutoClaims(e));
    mappings.put(FilemanCoordinates.of("36", "6.1"), e -> results.insCoPrintSecMedClaimsWoMra(e));
    mappings.put(FilemanCoordinates.of("36", "7.01"), e -> results.insCoEdiUmo278Id(e));
    mappings.put(FilemanCoordinates.of("36", "17"), e -> results.insCo277ediIdNumber(e));
    mappings.put(FilemanCoordinates.of("355.3", ".01"), e -> results.groupPlanInsuranceCompany(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".02"), e -> results.groupPlanIsThisAGroupPolicy(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".05"),
        e -> results.groupPlanIsUtilizationReviewRequired(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".06"), e -> results.groupPlanIsPrecertificationRequired(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".07"),
        e -> results.groupPlanExcludePreexistingCondition(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".08"), e -> results.groupPlanBenefitsAssignable(e));
    mappings.put(FilemanCoordinates.of("355.3", ".09"), e -> results.groupPlanTypeOfPlan(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".12"),
        e -> results.groupPlanAmbulatoryCareCertification(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".13"), e -> results.groupPlanPlanFilingTimeFrame(e));
    mappings.put(FilemanCoordinates.of("355.3", ".14"), e -> results.groupPlanPlanCategory(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".15"), e -> results.groupPlanElectronicPlanType(e));
    mappings.put(FilemanCoordinates.of("355.3", ".16"), e -> results.groupPlanPlanStandardFtf(e));
    mappings.put(
        FilemanCoordinates.of("355.3", ".17"), e -> results.groupPlanPlanStandardFtfValue(e));
    mappings.put(FilemanCoordinates.of("355.3", "1.01"), e -> results.groupPlanDateEntered(e));
    mappings.put(FilemanCoordinates.of("355.3", "1.02"), e -> results.groupPlanEnteredBy(e));
    mappings.put(FilemanCoordinates.of("355.3", "1.03"), e -> results.groupPlanDateLastVerified(e));
    mappings.put(FilemanCoordinates.of("355.3", "1.04"), e -> results.groupPlanVerifiedBy(e));
    mappings.put(FilemanCoordinates.of("355.3", "1.05"), e -> results.groupPlanDateLastEdited(e));
    mappings.put(FilemanCoordinates.of("355.3", "1.06"), e -> results.groupPlanLastEditedBy(e));
    mappings.put(FilemanCoordinates.of("355.3", "2.01"), e -> results.groupPlanGroupName(e));
    mappings.put(FilemanCoordinates.of("355.3", "2.02"), e -> results.groupPlanGroupNumber(e));
    mappings.put(FilemanCoordinates.of("355.3", "6.01"), e -> results.groupPlanPlanId(e));
    mappings.put(
        FilemanCoordinates.of("355.3", "6.02"),
        e -> results.groupPlanBankingIdentificationNumber(e));
    mappings.put(
        FilemanCoordinates.of("355.3", "6.03"), e -> results.groupPlanProcessorControlNumberPcn(e));
    mappings.put(FilemanCoordinates.of("355.32", ".01"), e -> results.limitationsPlan(e));
    mappings.put(
        FilemanCoordinates.of("355.32", ".02"), e -> results.limitationsCoverageCategory(e));
    mappings.put(FilemanCoordinates.of("355.32", ".03"), e -> results.limitationsEffectiveDate(e));
    mappings.put(FilemanCoordinates.of("355.32", ".04"), e -> results.limitationsCoverageStatus(e));
    mappings.put(FilemanCoordinates.of("355.32", "1.01"), e -> results.limitationsDateEntered(e));
    mappings.put(FilemanCoordinates.of("355.32", "1.02"), e -> results.limitationsEnteredBy(e));
    mappings.put(
        FilemanCoordinates.of("355.32", "1.03"), e -> results.limitationsDateLastEdited(e));
    mappings.put(FilemanCoordinates.of("355.32", "1.04"), e -> results.limitationsLastEditedBy(e));
    mappings.put(
        FilemanCoordinates.of("355.32", "2"), e -> results.limitationsLimitationComment(e));
    return Map.copyOf(mappings);
  }
}
