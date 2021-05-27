package gov.va.api.lighthouse.charon.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.io.Serial;
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

/** Container class for specific rpc details that the RpcInvoker needs to make a vista request. */
@Data
@Builder(toBuilder = true)
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

  /** Parameter object that handles safely invoking vista requests with parameters. */
  @Data
  @NoArgsConstructor
  @JsonAutoDetect(fieldVisibility = Visibility.ANY, isGetterVisibility = Visibility.NONE)
  public static class Parameter {
    @JsonDeserialize(using = ParameterValueDeserializer.class)
    @JsonSerialize(using = ParameterValueSerializer.class)
    private String ref;

    @JsonDeserialize(using = ParameterValueDeserializer.class)
    @JsonSerialize(using = ParameterValueSerializer.class)
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
    private void checkOnlyOneSet() {
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
            "Exactly one of ref, string, array, or namedArray must be specified. Found " + count);
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

  /** Deserializer for RpcDetail's safe parameters objects. */
  public static class ParameterValueDeserializer extends StdDeserializer<String> {

    @Serial private static final long serialVersionUID = 5543697039890338690L;

    public ParameterValueDeserializer() {
      super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      return StringUtils.trim(p.getText());
    }
  }

  /** Serializer for RpcDetail's safe parameters objects. */
  public static class ParameterValueSerializer extends StdSerializer<String> {

    @Serial private static final long serialVersionUID = -4201150277421890777L;

    public ParameterValueSerializer() {
      super(String.class);
    }

    @Override
    public void serialize(
        String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
      jsonGenerator.writeString(s);
    }
  }
}
