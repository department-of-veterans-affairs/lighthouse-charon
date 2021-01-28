package gov.va.api.lighthouse.vistalink.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RpcDetails {
  @NotBlank private String name;
  @NotBlank private String context;
  private Optional<Double> version;

  @Builder.Default private List<Parameter> parameters = new ArrayList<>();

  /** Lazy getter. */
  public Optional<Double> version() {
    if (version == null) {
      version = Optional.empty();
    }
    return version;
  }

  @Data
  @NoArgsConstructor
  @JsonAutoDetect(fieldVisibility = Visibility.ANY)
  public static class Parameter {
    @JsonDeserialize(using = ParameterValueDeserializer.class)
    private String ref;

    @JsonDeserialize(using = ParameterValueDeserializer.class)
    private String string;

    @JsonDeserialize(contentUsing = ParameterValueDeserializer.class)
    private List<String> array;

    @JsonDeserialize(contentUsing = ParameterValueDeserializer.class)
    private Map<String, String> namedArray;

    /**
     * Create a new instance, however only one parameter can be specified. The other must be null.
     * This constructor is explicitly intended to be used with a Builder.
     */
    @Builder
    private Parameter(
        String ref, String string, List<String> array, Map<String, String> namedArray) {
      this.ref = ref;
      this.string = string;
      this.array = array;
      this.namedArray = namedArray;
      checkOnlyOneSet();
    }

    /** Verify that only parameter field is set. */
    public void checkOnlyOneSet() {
      int count = 0;
      if (isRef()) {
        count++;
      }
      if (isString()) {
        count++;
      }
      if (isArray()) {
        count++;
      }
      if (isNamedArray()) {
        count++;
      }
      if (count != 1) {
        throw new IllegalArgumentException(
            "Exact one of ref, string, array, or namedArray must be specified");
      }
    }

    public boolean isArray() {
      return array != null;
    }

    public boolean isNamedArray() {
      return namedArray != null;
    }

    public boolean isRef() {
      return ref != null;
    }

    public boolean isString() {
      return string != null;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName() + "(" + type() + "=" + value() + ")";
    }

    /** Determine RPC parameter type based on the fields that are set. */
    public String type() {
      if (isRef()) {
        return "ref";
      }
      if (isString()) {
        return "string";
      }
      if (isArray()) {
        return "array";
      }
      if (isNamedArray()) {
        return "array";
      }
      throw new IllegalStateException("unknown type");
    }

    /** Determine RPC parameter value based on the fields that are set. */
    public Object value() {
      if (isRef()) {
        return ref;
      }
      if (isString()) {
        return string;
      }
      if (isArray()) {
        return array;
      }
      if (isNamedArray()) {
        return namedArray;
      }
      throw new IllegalStateException("unknown type");
    }
  }

  public static class ParameterValueDeserializer extends StdDeserializer<String> {

    public ParameterValueDeserializer() {
      super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return StringUtils.trim(p.getText());
    }
  }
}
