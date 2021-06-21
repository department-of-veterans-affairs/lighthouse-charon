package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Response model for the IBLHS AMCMS GET INS RPC. */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(staticName = "empty")
@Generated(
    value = "charon-models/src/scripts/generate-get-insurance-models.sh",
    date = "2021-06-21T15:07:18Z",
    comments = "Generated using insuranceElementList.xlsx version 183.0.")
public class GetInsRpcResults {
  /** 2 "Coverage.payor ,payor.type = ins". */
  private GetInsEntry insTypeInsuranceType;
  /** 3 "Coverage.class.value ,Coverage.class.type = group". */
  private GetInsEntry insTypeGroupPlan;
  /** 4 Coverage.order. */
  private GetInsEntry insTypeCoordinationOfBenefits;
  /** 8 computed. */
  private GetInsEntry insTypeDateLastVerified;
  /** 17 Coverage.extension. */
  private GetInsEntry insTypeSendBillToEmployer;
  /** 18 Coverage.policyHolder.name. */
  private GetInsEntry insTypeSubscribersEmployerName;
  /** 19 Coverage.policyHolder.address.line. */
  private GetInsEntry insTypeEmployerClaimsStreetAddress;
  /** 20 Coverage.policyHolder.address.line. */
  private GetInsEntry insTypeEmployClaimStAddressLine2;
  /** 21 Coverage.policyHolder.address.line. */
  private GetInsEntry insTypeEmployClaimStAddressLine3;
  /** 22 Coverage.policyHolder.address.city. */
  private GetInsEntry insTypeEmployerClaimsCity;
  /** 23 Coverage.policyHolder.address.state. */
  private GetInsEntry insTypeEmployerClaimsState;
  /** 24 Coverage.policyHolder.address.postalCode. */
  private GetInsEntry insTypeEmployerClaimsZipCode;
  /** 25 Coverage.policyHolder.telecom. */
  private GetInsEntry insTypeEmployerClaimsPhone;
  /** 26 Coverage.class.type = Group (). */
  private GetInsEntry insTypeEsghp;
  /** 29 . */
  private GetInsEntry insTypeInsuranceExpirationDate;
  /** 30 "Coverage.beneficiary.birthDate ,Coverage.subscriber.birthDate". */
  private GetInsEntry insTypeInsuredsDob;
  /** 31 ODH - or VistA lookup?. */
  private GetInsEntry insTypeInsuredsBranch;
  /** 32 ODH - or VistA lookup?. */
  private GetInsEntry insTypeInsuredsRank;
  /** 33 Coverage.extension. */
  private GetInsEntry insTypeStopPolicyFromBilling;
  /** 34 "Coverage.beneficiary.identifier ,Coverage.subscriber.identifier". */
  private GetInsEntry insTypeInsuredsSsn;
  /** 35 "Coverage.beneficiary.address.line ,Coverage.subscriber.address.line". */
  private GetInsEntry insTypeInsuredsStreet1;
  /** 36 "Coverage.beneficiary.address.line ,Coverage.subscriber.address.line". */
  private GetInsEntry insTypeInsuredsStreet2;
  /** 37 "Coverage.beneficiary.address.city ,Coverage.subscriber.address.city". */
  private GetInsEntry insTypeInsuredsCity;
  /** 38 "Coverage.beneficiary.address.state ,Coverage.subscriber.address.state". */
  private GetInsEntry insTypeInsuredsState;
  /** 39 "Coverage.beneficiary.address.postalCode ,Coverage.subscriber.address.postalCode". */
  private GetInsEntry insTypeInsuredsZip;
  /** 40 "Coverage.beneficiary.telecom.value ,Coverage.subscriber.telecom.value ,system = phone". */
  private GetInsEntry insTypeInsuredsPhone;
  /** 41 "Coverage.beneficiary.gender ,Coverage.subscriber.gender". */
  private GetInsEntry insTypeInsuredsSex;
  /** 42 "Coverage.beneficiary.address.country ,Coverage.subscriber.address.country". */
  private GetInsEntry insTypeInsuredsCountry;
  /** 43 "Coverage.beneficiary.address.state ,Coverage.subscriber.address.state". */
  private GetInsEntry insTypeInsuredsCountrySubdivision;
  /** 44 Coverage.beneficiary.generalPractitioner.name. */
  private GetInsEntry insTypePrimaryCareProvider;
  /** 45 Coverage.beneficiary.generalPractitioner.telecom. */
  private GetInsEntry insTypePrimaryProviderPhone;
  /** 46 Coverage.relationship. */
  private GetInsEntry insTypePtRelationshipHipaa;
  /** 49 . */
  private GetInsEntry insTypePharmacyPersonCode;
  /**
   * 50 "Coverage.beneficiary.identifier.value ,Coverage.beneficiary.identifier.type.coding.code=MB
   * ,".
   */
  private GetInsEntry insTypePatientId;
  /**
   * 51 "Coverage.beneficiary.identifier.type.coding.code
   * ,Coverage.subscriber.identifier.type.coding.code".
   */
  private GetInsEntry insTypeSubscribersSecQualifier1;
  /**
   * 52 "Coverage.beneficiary.identifier.value ,Coverage.subscriber.identifier.value
   * ,identifier.system = urn:oid:2.16.840.1.113883.3.8901.3.280312.58003".
   */
  private GetInsEntry insTypeSubscribersSecId1;
  /**
   * 53 "Coverage.beneficiary.identifier.type.coding.code
   * ,Coverage.subscriber.identifier.type.coding.code".
   */
  private GetInsEntry insTypeSubscribersSecQualifier2;
  /**
   * 54 "Coverage.beneficiary.identifier.value ,Coverage.subscriber.identifier.value
   * ,identifier.system = urn:oid:2.16.840.1.113883.3.8901.3.280312.58005".
   */
  private GetInsEntry insTypeSubscribersSecId2;
  /**
   * 55 "Coverage.beneficiary.identifier.type.coding.code
   * ,Coverage.subscriber.identifier.type.coding.code".
   */
  private GetInsEntry insTypeSubscribersSecQualifier3;
  /**
   * 56 "Coverage.beneficiary.identifier.value ,Coverage.subscriber.identifier.value
   * ,identifier.system = urn:oid:2.16.840.1.113883.3.8901.3.280312.58007".
   */
  private GetInsEntry insTypeSubscribersSecId3;
  /**
   * 57 "Coverage.beneficiary.identifier.type.coding.code
   * ,Coverage.subscriber.identifier.type.coding.code".
   */
  private GetInsEntry insTypePatientsSecQualifier1;
  /**
   * 58 "Coverage.beneficiary.identifier.value ,Coverage.beneficiary.identifier.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.280312.58009".
   */
  private GetInsEntry insTypePatientsSecondaryId1;
  /**
   * 59 "Coverage.beneficiary.identifier.type.coding.code
   * ,Coverage.subscriber.identifier.type.coding.code".
   */
  private GetInsEntry insTypePatientsSecQualifier2;
  /**
   * 60 "Coverage.beneficiary.identifier.value ,Coverage.beneficiary.identifier.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.280312.58011".
   */
  private GetInsEntry insTypePatientsSecondaryId2;
  /**
   * 61 "Coverage.beneficiary.identifier.type.coding.code
   * ,Coverage.subscriber.identifier.type.coding.code".
   */
  private GetInsEntry insTypePatientsSecQualifier3;
  /**
   * 62 "Coverage.beneficiary.identifier.value ,Coverage.beneficiary.identifier.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.280312.58013".
   */
  private GetInsEntry insTypePatientsSecondaryId3;
  /** 63 computed. */
  private GetInsEntry insTypeWhoseInsurance;
  /** 64 "Coverage.beneficiary.name ,Coverage.subscriber.name". */
  private GetInsEntry insTypeNameOfInsured;
  /** 65 Coverage.subscriberId. */
  private GetInsEntry insTypeSubscriberId;
  /** 68 Coverage.period.start. */
  private GetInsEntry insTypeEffectiveDateOfPolicy;
  /**
   * 70 "insurance.item.category ,insurance.item.category.coding.system =
   * http://terminology.hl7.org/CodeSystem/ex-benefitcategory".
   */
  private GetInsEntry insTypeRequestedServiceType;
  /** 83 computed. */
  private GetInsEntry insTypePtRelationshipToInsured;
  /** 87 . */
  private GetInsEntry insTypeEligibilitybenefit;
  /** 89 "active. */
  private GetInsEntry elBenefitEligibilitybenefitInfo;
  /** 90 unk. */
  private GetInsEntry elBenefitCoverageLevel;
  /** 132 "Organization.name ,Organization.type = ins". */
  private GetInsEntry insCoName;
  /** 134 Organization.extension. */
  private GetInsEntry insCoAllowMultipleBedsections;
  /** 135 Organization.extension. */
  private GetInsEntry insCoDifferentRevenueCodesToUse;
  /** 136 Organization.extension. */
  private GetInsEntry insCoOneOptVisitOnBillOnly;
  /** 137 Organization.extension. */
  private GetInsEntry insCoAmbulatorySurgRevCode;
  /** 140 Organization.address.line. */
  private GetInsEntry insCoStreetAddressLine1;
  /** 141 Organization.address.line. */
  private GetInsEntry insCoStreetAddressLine2;
  /** 142 Organization.address.line. */
  private GetInsEntry insCoStreetAddressLine3;
  /** 143 Organization.address.city. */
  private GetInsEntry insCoCity;
  /** 144 Organization.address.state. */
  private GetInsEntry insCoState;
  /** 145 Organization.address.postalCode. */
  private GetInsEntry insCoZipCode;
  /**
   * 146 "Organization.contact.extension.display ,Organization.contact.extension.url =
   * via-intermediary".
   */
  private GetInsEntry insCoBillingCompanyName;
  /** 147 Organization.contact.telecom; system=fax. */
  private GetInsEntry insCoFaxNumber;
  /** 148 Organization.extension. */
  private GetInsEntry insCoFilingTimeFrame;
  /**
   * 149 "Organization.contact.address.line ,Organization.contact.purpose.coding.code = INPTCLAIMS".
   */
  private GetInsEntry insCoClaimsInptStreetAddress1;
  /** 150 Organization.contact.address.line. */
  private GetInsEntry insCoClaimsInptStreetAddress2;
  /** 151 Organization.contact.address.line. */
  private GetInsEntry insCoClaimsInptStreetAddress3;
  /** 152 Organization.contact.address.city. */
  private GetInsEntry insCoClaimsInptProcessCity;
  /** 153 Organization.contact.address.state. */
  private GetInsEntry insCoClaimsInptProcessState;
  /** 154 Organization.contact.address.postalCode. */
  private GetInsEntry insCoClaimsInptProcessZip;
  /**
   * 155 "Organization.contact.extension.display ,Organization.contact.extension.url =
   * via-intermediary".
   */
  private GetInsEntry insCoClaimsInptCompanyName;
  /** 156 Organization.extension. */
  private GetInsEntry insCoAnotherCoProcessIpClaims;
  /** 157 "Organization.contact.telecom.value ,Organization.contact.telecom.system = fax". */
  private GetInsEntry insCoClaimsInptFax;
  /** 158 Organization.extension. */
  private GetInsEntry insCoTypeOfCoverage;
  /** 159 "Organization.telecom ,system=phone". */
  private GetInsEntry insCoPhoneNumber;
  /**
   * 160 "Organization.contact.telecom.value ,Organization.contact.telecom.system=phone
   * ,Organization.contact.purpose = RXCLAIMS".
   */
  private GetInsEntry insCoClaimsRxPhoneNumber;
  /**
   * 161 "Organization.contact.telecom.value ,Organization.contact.telecom.system=phone
   * ,Organization.contact.purpose = BILL".
   */
  private GetInsEntry insCoBillingPhoneNumber;
  /** 162 "Organization.contact.telecom.value ,Organization.contact.telecom.system=phone". */
  private GetInsEntry insCoPrecertificationPhoneNumber;
  /**
   * 163 "Organization.contact.telecom.value ,Organization.contact.telecom.system=phone
   * ,Organization.contact.purpose = VERIFY".
   */
  private GetInsEntry insCoVerificationPhoneNumber;
  /** 164 "Organization.contact.telecom.value ,Organization.contact.telecom.system = phone". */
  private GetInsEntry insCoClaimsInptPhoneNumber;
  /** 165 "Organization.contact.telecom.value ,Organization.contact.telecom.system=phone". */
  private GetInsEntry insCoClaimsOptPhoneNumber;
  /** 166 "Organization.contact.telecom.value ,Organization.contact.telecom.system = phone". */
  private GetInsEntry insCoAppealsPhoneNumber;
  /** 167 "Organization.contact.telecom.value ,Organization.contact.telecom.system=phone". */
  private GetInsEntry insCoInquiryPhoneNumber;
  /**
   * 168 "Organization.contact.extension.display ,Organization.contact.extension.url =
   * via-intermediary ,Organization.contact.purpose = PRECERT".
   */
  private GetInsEntry insCoPrecertCompanyName;
  /** 169 "Organization.contact.address.line ,Organization.contact.purpose = APPEAL". */
  private GetInsEntry insCoAppealsAddressStLine1;
  /** 170 Organization.contact.address.line. */
  private GetInsEntry insCoAppealsAddressStLine2;
  /** 171 Organization.contact.address.line. */
  private GetInsEntry insCoAppealsAddressStLine3;
  /** 172 Organization.contact.address.city. */
  private GetInsEntry insCoAppealsAddressCity;
  /** 173 Organization.contact.address.state. */
  private GetInsEntry insCoAppealsAddressState;
  /** 174 Organization.contact.address.postalCode. */
  private GetInsEntry insCoAppealsAddressZip;
  /**
   * 175 "Organization.contact.extension.display ,Organization.contact.extension.url =
   * via-intermediary".
   */
  private GetInsEntry insCoAppealsCompanyName;
  /** 176 Organization.extension. */
  private GetInsEntry insCoAnotherCoProcessAppeals;
  /** 177 "Organization.contact.telecom.value ,Organization.contact.telecom.system = fax". */
  private GetInsEntry insCoAppealsFax;
  /** 178 . */
  private GetInsEntry insCoPrescriptionRefillRevCode;
  /** 179 "Organization.contact.address.line ,Organization.contact.purpose = INQUIRY". */
  private GetInsEntry insCoInquiryAddressStLine1;
  /** 180 Organization.contact.address.line. */
  private GetInsEntry insCoInquiryAddressStLine2;
  /** 181 Organization.contact.address.line. */
  private GetInsEntry insCoInquiryAddressStLine3;
  /** 182 Organization.contact.address.city. */
  private GetInsEntry insCoInquiryAddressCity;
  /** 183 Organization.contact.address.state. */
  private GetInsEntry insCoInquiryAddressState;
  /** 184 Organization.contact.address.postalCode. */
  private GetInsEntry insCoInquiryAddressZipCode;
  /**
   * 185 "Organization.contact.extension.display ,Organization.contact.extension.url =
   * via-intermediary".
   */
  private GetInsEntry insCoInquiryCompanyName;
  /** 186 Organization.extension. */
  private GetInsEntry insCoAnotherCoProcessInquiries;
  /** 187 "Organization.contact.telecom.value ,Organization.contact.telecom.system=fax". */
  private GetInsEntry insCoInquiryFax;
  /** 189 "Organization.contact.address.line ,Organization.contact.purpose = OUTPTCLAIMS". */
  private GetInsEntry insCoClaimsOptStreetAddress1;
  /** 190 Organization.contact.address.line. */
  private GetInsEntry insCoClaimsOptStreetAddress2;
  /** 191 Organization.contact.address.line. */
  private GetInsEntry insCoClaimsOptStreetAddress3;
  /** 192 Organization.contact.address.city. */
  private GetInsEntry insCoClaimsOptProcessCity;
  /** 193 Organization.contact.address.state. */
  private GetInsEntry insCoClaimsOptProcessState;
  /** 194 Organization.contact.address.postalCode. */
  private GetInsEntry insCoClaimsOptProcessZip;
  /**
   * 195 "Organization.contact.extension.display ,Organization.contact.extension.url =
   * via-intermediary".
   */
  private GetInsEntry insCoClaimsOptCompanyName;
  /** 196 Organization.extension. */
  private GetInsEntry insCoAnotherCoProcessOpClaims;
  /** 197 "Organization.contact.telecom.value ,Organization.contact.telecom.system=fax". */
  private GetInsEntry insCoClaimsOptFax;
  /** 198 "Organization.contact.telecom. */
  private GetInsEntry insCoProfessionalProviderNumber;
  /** 199 Organization.extension. */
  private GetInsEntry insCoAnotherCoProcessPrecerts;
  /** 200 . */
  private GetInsEntry insCoStandardFtf;
  /** 201 Organization.contact.address.line. */
  private GetInsEntry insCoClaimsRxStreetAddress1;
  /** 202 Organization.contact.address.line. */
  private GetInsEntry insCoClaimsRxStreetAddress2;
  /** 203 Organization.contact.address.line. */
  private GetInsEntry insCoClaimsRxStreetAddress3;
  /** 204 Organization.contact.address.city. */
  private GetInsEntry insCoClaimsRxCity;
  /** 205 Organization.contact.address.state. */
  private GetInsEntry insCoClaimsRxState;
  /** 206 Organization.contact.address.postalCode. */
  private GetInsEntry insCoClaimsRxZip;
  /**
   * 207 "Organization.contact.extension.display ,Organization.contact.extension.url =
   * via-intermediary".
   */
  private GetInsEntry insCoClaimsRxCompanyName;
  /** 208 Organization.extension. */
  private GetInsEntry insCoAnotherCoProcessRxClaims;
  /** 209 "Organization.contact.telecom.value ,Organization.contact.telecom.system = fax". */
  private GetInsEntry insCoClaimsRxFax;
  /** 210 Organization.extension. */
  private GetInsEntry insCoStandardFtfValue;
  /** 211 "Organization.contact.address.line ,Organization.contact.purpose = DENTALCLAIM". */
  private GetInsEntry insCoClaimsDentalStreetAddr1;
  /** 212 "Organization.contact.telecom.value ,Organization.contact.telecom.system = phone". */
  private GetInsEntry insCoClaimsDentalPhoneNumber;
  /** 213 Organization.contact.address.line. */
  private GetInsEntry insCoClaimsDentalStreetAddr2;
  /** 214 Organization.contact.address.city. */
  private GetInsEntry insCoBlank;
  /** 215 Organization.contact.address.city. */
  private GetInsEntry insCoClaimsDentalProcessCity;
  /** 216 Organization.contact.address.state. */
  private GetInsEntry insCoClaimsDentalProcessState;
  /** 217 Organization.contact.address.postalCode. */
  private GetInsEntry insCoClaimsDentalProcessZip;
  /**
   * 218 "Organization.contact.extension.display ,Organization.contact.extension.url =
   * via-intermediary".
   */
  private GetInsEntry insCoClaimsDentalCompanyName;
  /** 219 Organization.extension. */
  private GetInsEntry insCoAnotherCoProcDentClaims;
  /** 220 "Organization.contact.telecom.value ,Organization.contact.telecom.system = fax". */
  private GetInsEntry insCoClaimsDentalFax;
  /** 221 Organization.extension. */
  private GetInsEntry insCoReimburse;
  /** 222 Organization.extension. */
  private GetInsEntry insCoSignatureRequiredOnBill;
  /** 223 Organization.extension. */
  private GetInsEntry insCoTransmitElectronically;
  /** 224 "Organization.identifier ,identifier.type.coding.code=PROFEDI". */
  private GetInsEntry insCoEdiIdNumberProf;
  /** 225 . */
  private GetInsEntry insCoBinNumber;
  /** 226 "Organization.identifier ,identifier.type.coding.code=INSTEDI". */
  private GetInsEntry insCoEdiIdNumberInst;
  /** 230 Organization.extension. */
  private GetInsEntry insCoElectronicInsuranceType;
  /** 231 Organization.extension. */
  private GetInsEntry insCoPayer;
  /** 234 . */
  private GetInsEntry insCoEdiIdNumberDental;
  /** 235 Organization.extension. */
  private GetInsEntry insCoPerfProvSecondIdType1500;
  /** 236 Organization.extension. */
  private GetInsEntry insCoPerfProvSecondIdTypeUb;
  /** 237 computed. */
  private GetInsEntry insCoSecondaryIdRequirements;
  /** 238 Organization.extension. */
  private GetInsEntry insCoRefProvSecIdDefCms1500;
  /** 239 Organization.extension. */
  private GetInsEntry insCoRefProvSecIdReqOnClaims;
  /** 240 Organization.extension. */
  private GetInsEntry insCoAttrendIdBillSecIdProf;
  /** 242 Organization.extension. */
  private GetInsEntry insCoAttrendIdBillSecIdInst;
  /**
   * 250 "Organization.identifier.type.coding.code ,Organization.identifier.type.coding.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.36.68001".
   */
  private GetInsEntry insCoEdiInstSecondaryIdQual1;
  /** 251 Organization.identifier.value. */
  private GetInsEntry insCoEdiInstSecondaryId1;
  /**
   * 252 "Organization.identifier.type.coding.code ,Organization.identifier.type.coding.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.36.68003".
   */
  private GetInsEntry insCoEdiInstSecondaryIdQual2;
  /** 253 Organization.identifier.value. */
  private GetInsEntry insCoEdiInstSecondaryId2;
  /**
   * 254 "Organization.identifier.type.coding.code ,Organization.identifier.type.coding.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.36.68005".
   */
  private GetInsEntry insCoEdiProfSecondaryIdQual1;
  /** 255 Organization.identifier.value. */
  private GetInsEntry insCoEdiProfSecondaryId1;
  /**
   * 256 "Organization.identifier.type.coding.code ,Organization.identifier.type.coding.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.36.68007".
   */
  private GetInsEntry insCoEdiProfSecondaryIdQual2;
  /** 257 Organization.identifier.value. */
  private GetInsEntry insCoEdiProfSecondaryId2;
  /** 258 Organization.extension. */
  private GetInsEntry insCoPrintSectertAutoClaims;
  /** 259 Organization.extension. */
  private GetInsEntry insCoPrintSecMedClaimsWoMra;
  /** 260 . */
  private GetInsEntry insCoEdiUmo278Id;
  /** 270 "Organization.identifier.value ,Organization.identifier.type.coding.code = 277EDI ,". */
  private GetInsEntry insCo277ediIdNumber;
  /** 272 InsurancePlan.ownedBy. */
  private GetInsEntry groupPlanInsuranceCompany;
  /** 273 computed. */
  private GetInsEntry groupPlanIsThisAGroupPolicy;
  /** 276 InsurancePlan.extension. */
  private GetInsEntry groupPlanIsUtilizationReviewRequired;
  /** 277 InsurancePlan.extension. */
  private GetInsEntry groupPlanIsPrecertificationRequired;
  /** 278 InsurancePlan.extension. */
  private GetInsEntry groupPlanExcludePreexistingCondition;
  /** 279 InsurancePlan.extension. */
  private GetInsEntry groupPlanBenefitsAssignable;
  /**
   * 280 "InsurancePlan.plan.type.coding.code ,InsurancePlan.plan.type.coding.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.355803.8009".
   */
  private GetInsEntry groupPlanTypeOfPlan;
  /** 283 InsurancePlan.extension. */
  private GetInsEntry groupPlanAmbulatoryCareCertification;
  /** 284 InsurancePlan.extension. */
  private GetInsEntry groupPlanPlanFilingTimeFrame;
  /**
   * 285 "InsurancePlan.type.coding.code ,InsurancePlan.type.coding.system =
   * 2.16.840.1.113883.3.8901.3.355803.8014".
   */
  private GetInsEntry groupPlanPlanCategory;
  /**
   * 286 "InsurancePlan.type.coding.code ,InsurancePlan.type.coding.system =
   * 2.16.840.1.113883.3.8901.3.355803.8015".
   */
  private GetInsEntry groupPlanElectronicPlanType;
  /** 287 InsurancePlan.extension. */
  private GetInsEntry groupPlanPlanStandardFtf;
  /** 288 InsurancePlan.extension. */
  private GetInsEntry groupPlanPlanStandardFtfValue;
  /** 289 computed. */
  private GetInsEntry groupPlanDateEntered;
  /** 290 computed. */
  private GetInsEntry groupPlanEnteredBy;
  /** 291 computed. */
  private GetInsEntry groupPlanDateLastVerified;
  /** 292 computed. */
  private GetInsEntry groupPlanVerifiedBy;
  /** 293 computed. */
  private GetInsEntry groupPlanDateLastEdited;
  /** 294 computed. */
  private GetInsEntry groupPlanLastEditedBy;
  /** 297 InsurancePlan.name. */
  private GetInsEntry groupPlanGroupName;
  /**
   * 298 "InsurancePlan.identifier.value ,InsurancePlan.identifier.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.355803.28002".
   */
  private GetInsEntry groupPlanGroupNumber;
  /**
   * 299 "InsurancePlan.identifier.value ,InsurancePlan.identifier.system =
   * urn:oid:2.16.840.1.113883.3.8901.3.355803.68001".
   */
  private GetInsEntry groupPlanPlanId;
  /** 300 . */
  private GetInsEntry groupPlanBankingIdentificationNumber;
  /** 301 . */
  private GetInsEntry groupPlanProcessorControlNumberPcn;
  /** 303 computed. */
  private GetInsEntry limitationsPlan;
  /** 304 insurance.item.category. */
  private GetInsEntry limitationsCoverageCategory;
  /** 305 insurance.benefitPeriod. */
  private GetInsEntry limitationsEffectiveDate;
  /** 306 insurance.inForce. */
  private GetInsEntry limitationsCoverageStatus;
  /** 307 computed. */
  private GetInsEntry limitationsDateEntered;
  /** 308 computed. */
  private GetInsEntry limitationsEnteredBy;
  /** 309 computed. */
  private GetInsEntry limitationsDateLastEdited;
  /** 310 computed. */
  private GetInsEntry limitationsLastEditedBy;
  /** 311 "insurance.item.description ,insurance.item.excluded = true". */
  private GetInsEntry limitationsLimitationComment;
}
