package gov.va.api.lighthouse.charon.models.iblhsamcmsgetins;

public class GetInsResponseSerializerConfigFactory {

  /** Create a Get Ins Serializer configuration. */
  public static GetInsResponseSerializerConfig create() {
    return GetInsResponseSerializerConfig.builder().results(GetInsRpcResults.empty()).build();
  }
}
