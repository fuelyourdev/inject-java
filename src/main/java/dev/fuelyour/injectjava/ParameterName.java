package dev.fuelyour.injectjava;

@SuppressWarnings("WeakerAccess")
public class ParameterName {

  private final String name;
  private final Class<?> type;
  private final int occurrence;

  public ParameterName(String name, Class<?> type) {
    this.name = name;
    this.type = type;
    this.occurrence = 1;
  }

  public ParameterName(String name, Class<?> type, int occurrence) {
    this.name = name;
    this.type = type;
    this.occurrence = occurrence;
  }

  public String getName() {
    return name;
  }

  public Class<?> getType() {
    return type;
  }

  public int getOccurrence() {
    return occurrence;
  }
}
