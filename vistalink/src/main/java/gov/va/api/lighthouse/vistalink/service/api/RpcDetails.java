package gov.va.api.lighthouse.vistalink.service.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String ref;
    private String string;
    private List<String> array;

    /**
     * Create a new instance, however only one parameter can be specified. The other must be null.
     * This constructor is explicitly intended to be used with a Builder.
     */
    @Builder
    private Parameter(String ref, String string, List<String> array) {
      this.ref = ref;
      this.string = string;
      this.array = array;
      checkOnlyOneSet();
    }

    /** Verify that only parameter field is set. */
    public void checkOnlyOneSet() {
      int count = 0;
      if (ref != null) {
        count++;
      }
      if (string != null) {
        count++;
      }
      if (array != null) {
        count++;
      }
      if (count != 1) {
        throw new IllegalArgumentException("Exact one of ref, string, or array must be specified");
      }
    }

    @Override
    public String toString() {
      return getClass().getSimpleName() + "(" + type() + "=" + value() + ")";
    }

    /** Determine RPC parameter type based on the fields that are set. */
    public String type() {
      if (ref != null) {
        return "ref";
      }
      if (string != null) {
        return "string";
      }
      if (array != null) {
        return "array";
      }
      throw new IllegalStateException("unknown type");
    }

    /** Determine RPC parameter value based on the fields that are set. */
    public Object value() {
      if (ref != null) {
        return ref;
      }
      if (string != null) {
        return string;
      }
      if (array != null) {
        return array;
      }
      throw new IllegalStateException("unknown type");
    }
  }
}
