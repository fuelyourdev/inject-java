package dev.fuelyour.injectjava;

import java.util.HashMap;
import java.util.Map;

class ObjectManager {

  private final Map<Id, Object> objs = new HashMap<>();

  boolean containsKey(Id key) {
    return objs.containsKey(key);
  }

  Object get(Id key) {
    return objs.get(key);
  }

  void put(Id key, Object value) {
    objs.put(key, value);
  }
}
