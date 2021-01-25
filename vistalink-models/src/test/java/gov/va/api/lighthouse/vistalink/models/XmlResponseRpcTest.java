package gov.va.api.lighthouse.vistalink.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

public class XmlResponseRpcTest {
  Known itIsKnown =
      Known.builder()
          .knownAttribute("i know this attribute")
          .knownProperty("known property")
          .build();

  @Test
  public void deserializeBadKnownThrows() {
    assertThrows(
        VistalinkModelExceptions.VistaModelException.class,
        () ->
            XmlResponseRpc.deserialize(
                "<known knownAttribute='i know this attribute'>\n"
                    + "<knownProperty>know property</knownProperty>\n"
                    + "<unclosed xml tag uh oh! known>",
                Known.class));
  }

  @Test
  public void deserializeBadKnownThrowsVistaModelException() {
    assertThrows(
        VistalinkModelExceptions.VistaModelException.class,
        () ->
            XmlResponseRpc.deserialize(
                "<known knownAttribute='i know this attribute'>\n"
                    + "<knownProperty>know property</knownProperty>\n"
                    + "<unclosed xml tag uh oh! known>",
                Known.class));
  }

  @Test
  public void deserializeKnown() {
    assertThat(
            XmlResponseRpc.deserialize(
                "<known knownAttribute='i know this attribute'>\n"
                    + "<knownProperty>known property</knownProperty>\n"
                    + "</known>",
                Known.class))
        .isEqualTo(itIsKnown);
  }

  @Test
  public void deserializeNullThrowsVistaModelException() {
    assertThrows(
        VistalinkModelExceptions.VistaModelException.class,
        () -> XmlResponseRpc.deserialize(null, Known.class));
  }

  @Test
  public void deserializeUnknown() {
    assertThat(
            XmlResponseRpc.deserialize(
                "<known knownAttribute='i know this attribute' unknownAttribute='????'>\n"
                    + "<knownProperty>known property</knownProperty>\n"
                    + "<unknownProperty>unknown property</unknownProperty>\n"
                    + "</known>\n"
                    + "<unknown unattribute='????'>\n"
                    + "<unproperty>????<unproperty>\n"
                    + "</unknown>",
                Known.class))
        .isEqualTo(itIsKnown);
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @JacksonXmlRootElement(localName = "known")
  private static class Known {
    @JacksonXmlProperty(isAttribute = true)
    String knownAttribute;

    @JacksonXmlProperty String knownProperty;
  }
}
