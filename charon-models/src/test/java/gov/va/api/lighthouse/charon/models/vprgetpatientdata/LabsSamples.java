package gov.va.api.lighthouse.charon.models.vprgetpatientdata;

import gov.va.api.lighthouse.charon.models.CodeAndNameXmlAttribute;
import gov.va.api.lighthouse.charon.models.ValueOnlyXmlAttribute;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "create")
public final class LabsSamples {
  public List<Labs.Lab> labResults() {
    return List.of(
        Labs.Lab.builder()
            .collected(ValueOnlyXmlAttribute.of("3100715.110005"))
            .comment(
                ValueOnlyXmlAttribute.of(
                    "Ordering Provider: Eightyeight Vehu Report Released\\n"
                        + "Date/Time: Apr 12, 2011@12:51\\n"
                        + "Performing Lab: ALBANY VA MEDICAL CENTER VA MEDICAL CENTER 1 3RD sT. ALBANY, NY 12180-0097"))
            .facility(CodeAndNameXmlAttribute.of("500", "CAMP MASTER"))
            .groupName(ValueOnlyXmlAttribute.of("RIA 0412 7"))
            .high(ValueOnlyXmlAttribute.of("15"))
            .id(ValueOnlyXmlAttribute.of("CH;6899283.889996;741"))
            .interpretation(ValueOnlyXmlAttribute.of("EXP"))
            .labOrderId(ValueOnlyXmlAttribute.of("6899283.889995"))
            .localName(ValueOnlyXmlAttribute.of("TSH"))
            .loinc(ValueOnlyXmlAttribute.of("10347-3"))
            .low(ValueOnlyXmlAttribute.of("7"))
            .performingLab(ValueOnlyXmlAttribute.of("ALBANY VA MEDICAL CENTER"))
            .provider(provider())
            .orderId(ValueOnlyXmlAttribute.of("500049"))
            .result(ValueOnlyXmlAttribute.of("11"))
            .resulted(ValueOnlyXmlAttribute.of("3110425.190958"))
            .sample(ValueOnlyXmlAttribute.of("SERUM"))
            .specimen(CodeAndNameXmlAttribute.of("0X500", "SERUM"))
            .status(ValueOnlyXmlAttribute.of("completed"))
            .test(ValueOnlyXmlAttribute.of("TSH"))
            .type(ValueOnlyXmlAttribute.of("CH"))
            .units(ValueOnlyXmlAttribute.of("MCIU/ML"))
            .vuid(ValueOnlyXmlAttribute.of("1234"))
            .build());
  }

  public Labs labs() {
    return Labs.builder().total(1).labResults(labResults()).build();
  }

  public Labs.Provider provider() {
    return Labs.Provider.builder()
        .code("20090")
        .name("VEHU,EIGHTYEIGHT")
        .officePhone("(518) 827-3158")
        .analogPager("(518) 930-4216")
        .fax("(518) 123-4567")
        .email("eightyeight.vehu@va.gov")
        .taxonomyCode("203B00000N")
        .providerType("Physicians (M.D. and D.O.)")
        .classification("Physician/Osteopath")
        .specialization("Emergency Medicine")
        .service("MEDICINE")
        .build();
  }
}
