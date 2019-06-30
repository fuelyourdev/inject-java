package dev.fuelyour.injectjava;

import java.util.Objects;

class GeneralRuleId {

  private final int id;
  private final String name;

  GeneralRuleId(int id) {
    this.id = id;
    this.name = null;
  }

  GeneralRuleId(String name) {
    id = -1;
    this.name = name;
  }

  @Override
  public String toString() {
    if (name != null) {
      return name;
    } else {
      return "unspecified" + id;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof GeneralRuleId) {
      GeneralRuleId other = (GeneralRuleId) obj;
      if (name != null) {
        return name.equals(other.name);
      } else {
        return id == other.id;
      }
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
