package gov.va.api.lighthouse.charon.models;

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
  FugaziXmlModel itIsKnown =
      FugaziXmlModel.builder()
          .knownAttribute("i know this attribute")
          .knownProperty("known property")
          .build();

  @Test
  public void deserializeBadKnownThrowsVistaModelException() {
    assertThrows(
        RpcModelExceptions.RpcModelException.class,
        () ->
            XmlResponseRpc.deserialize(
                "<known knownAttribute='i know this attribute'>\n"
                    + "<knownProperty>know property</knownProperty>\n"
                    + "<unclosed xml tag uh oh! known>",
                FugaziXmlModel.class));
  }

  @Test
  public void deserializeKnown() {
    assertThat(
            XmlResponseRpc.deserialize(
                "<known knownAttribute='i know this attribute'>\n"
                    + "<knownProperty>known property</knownProperty>\n"
                    + "</known>",
                FugaziXmlModel.class))
        .isEqualTo(itIsKnown);
  }

  @Test
  public void deserializeNullThrowsVistaModelException() {
    assertThrows(
        RpcModelExceptions.RpcModelException.class,
        () -> XmlResponseRpc.deserialize(null, FugaziXmlModel.class));
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
                FugaziXmlModel.class))
        .isEqualTo(itIsKnown);
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @JacksonXmlRootElement(localName = "known")
  private static class FugaziXmlModel {
    @JacksonXmlProperty(isAttribute = true)
    String knownAttribute;

    @JacksonXmlProperty String knownProperty;
  }
}
