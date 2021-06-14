package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

import lombok.Builder;
import lombok.Data;

/**
 * Java representation of Fileman file #36 in VistA.
 *
 * <p>Currently using insuranceElementList.xlsx version: 179.0
 */
@Data
@Builder
public class InsuranceCompanyFile {
  private GetInsEntry name;

  private GetInsEntry allowMultipleBedsections;

  private GetInsEntry differentRevenueCodesToUse;

  private GetInsEntry oneOptVisitOnBillOnly;

  private GetInsEntry ambulatorySurgRevCode;

  private GetInsEntry streetAddressLine1;

  private GetInsEntry streetAddressLine2;

  private GetInsEntry streetAddressLine3;

  private GetInsEntry city;

  private GetInsEntry state;

  private GetInsEntry zipCode;

  private GetInsEntry billingCompanyName;

  private GetInsEntry faxNumber;

  private GetInsEntry filingTimeFrame;

  private GetInsEntry claimsInptStreetAddress1;

  private GetInsEntry claimsInptStreetAddress2;

  private GetInsEntry claimsInptStreetAddress3;

  private GetInsEntry claimsInptProcessCity;

  private GetInsEntry claimsInptProcessState;

  private GetInsEntry claimsInptProcessZip;

  private GetInsEntry claimsInptCompanyName;

  private GetInsEntry anotherCoProcessIpClaims;

  private GetInsEntry claimsInptFax;

  private GetInsEntry typeOfCoverage;

  private GetInsEntry phoneNumber;

  private GetInsEntry claimsRxPhoneNumber;

  private GetInsEntry billingPhoneNumber;

  private GetInsEntry precertificationPhoneNumber;

  private GetInsEntry verificationPhoneNumber;

  private GetInsEntry claimsInptPhoneNumber;

  private GetInsEntry claimsOptPhoneNumber;

  private GetInsEntry appealsPhoneNumber;

  private GetInsEntry inquiryPhoneNumber;

  private GetInsEntry precertCompanyName;

  private GetInsEntry appealsAddressStLine1;

  private GetInsEntry appealsAddressStLine2;

  private GetInsEntry appealsAddressStLine3;

  private GetInsEntry appealsAddressCity;

  private GetInsEntry appealsAddressState;

  private GetInsEntry appealsAddressZip;

  private GetInsEntry appealsCompanyName;

  private GetInsEntry anotherCoProcessAppeals;

  private GetInsEntry appealsFax;

  private GetInsEntry prescriptionRefillRevCode;

  private GetInsEntry inquiryAddressStLine1;

  private GetInsEntry inquiryAddressStLine2;

  private GetInsEntry inquiryAddressStLine3;

  private GetInsEntry inquiryAddressCity;

  private GetInsEntry inquiryAddressState;

  private GetInsEntry inquiryAddressZipCode;

  private GetInsEntry inquiryCompanyName;

  private GetInsEntry anotherCoProcessingInquiries;

  private GetInsEntry inquiryFax;

  private GetInsEntry claimsOptStreetAddress1;

  private GetInsEntry claimsOptStreetAddress2;

  private GetInsEntry claimsOptStreetAddress3;

  private GetInsEntry claimsOptProcessCity;

  private GetInsEntry claimsOptProcessState;

  private GetInsEntry claimsOptProcessZip;

  private GetInsEntry claimsOptCompanyName;

  private GetInsEntry anotherCoProcessOpClaims;

  private GetInsEntry claimsOptFax;

  private GetInsEntry anotherCoProcessPrecerts;

  private GetInsEntry claimsRxStreetAddress1;

  private GetInsEntry claimsRxStreetAddress2;

  private GetInsEntry claimsRxStreetAddress3;

  private GetInsEntry claimsRxCity;

  private GetInsEntry claimsRxState;

  private GetInsEntry claimsRxZip;

  private GetInsEntry claimsRxCompanyName;

  private GetInsEntry anotherCoProcessRxClaims;

  private GetInsEntry claimsRxFax;

  private GetInsEntry standardFtfValue;

  private GetInsEntry claimsDentalPhoneNumber;

  private GetInsEntry claimsDentalStreetAddr1;

  private GetInsEntry claimsDentalStreetAddr2;

  private GetInsEntry claimsDentalProcessCity;

  private GetInsEntry claimsDentalProcessState;

  private GetInsEntry claimsDentalProcessZip;

  private GetInsEntry claimsDentalCompanyName;

  private GetInsEntry anotherCoProcDentClaims;

  private GetInsEntry claimsDentalFax;

  private GetInsEntry reimburse;

  private GetInsEntry signatureRequiredOnBill;

  private GetInsEntry transmitElectronically;

  private GetInsEntry ediIdNumberProf;

  private GetInsEntry binNumber;

  private GetInsEntry ediIdNumberInst;

  private GetInsEntry electronicInsuranceType;

  private GetInsEntry payer;

  private GetInsEntry ediIdNumberDental;

  private GetInsEntry perfProvSecondIdType1500;

  private GetInsEntry perfProvSecondIdTypeUb;

  private GetInsEntry secondaryIdRequirements;

  private GetInsEntry refProvSecIdDefCms1500;

  private GetInsEntry refProvSecIdReqOnClaims;

  private GetInsEntry attRendIdBillSecIdProf;

  private GetInsEntry attRendIdBillSecIdInst;

  private GetInsEntry ediInstSecondaryIdQual1;

  private GetInsEntry ediInstSecondaryId1;

  private GetInsEntry ediInstSecondaryIdQual2;

  private GetInsEntry ediInstSecondaryId2;

  private GetInsEntry ediProfSecondaryIdQual1;

  private GetInsEntry ediProfSecondaryId1;

  private GetInsEntry ediProfSecondaryIdQual2;

  private GetInsEntry ediProfSecondaryId2;

  private GetInsEntry printSecTertAutoClaims;

  private GetInsEntry printSecMedClaimsWoMra;

  private GetInsEntry ediUmo278Id;

  private GetInsEntry x277EdiIdNumber;
}
