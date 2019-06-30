package dev.fuelyour.injectjava;

import java.util.Objects;

public class Id {

  private final Class<?> clazz;
  private final String name;

  Id(Class<?> clazz) {
    this.clazz = clazz;
    name = null;
  }

  Id(String name) {
    this.name = name;
    clazz = null;
  }

  Id(Class<?> clazz, String name) {
    this.clazz = clazz;
    this.name = name;
  }

  Class<?> getClassId() {
    return clazz;
  }

  @SuppressWarnings("unused")
  String getStringId() {
    return name;
  }

  @Override
  public String toString() {
    return name != null ?
        "Name: " + name :
        "Class: " + clazz;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Id) {
      Id objId = (Id) obj;
      if (name != null) {
        return name.equals(objId.name);
      } else if (objId.name != null) {
        return false;
      } else if (clazz != null) {
        return clazz.equals(objId.clazz);
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (name != null) {
      return Objects.hash(name);
    } else {
      return Objects.hash(clazz);
    }
  }
}
