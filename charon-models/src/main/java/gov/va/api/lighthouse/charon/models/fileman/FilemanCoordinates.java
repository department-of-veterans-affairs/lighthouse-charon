package gov.va.api.lighthouse.charon.models.fileman;

public record FilemanCoordinates(String fileNumber, String fieldNumber) {
  public static FilemanCoordinates of(String fileNumber, String fieldNumber) {
    return new FilemanCoordinates(fileNumber, fieldNumber);
  }
}
