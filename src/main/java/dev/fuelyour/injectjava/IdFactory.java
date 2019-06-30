package dev.fuelyour.injectjava;

class IdFactory {

  Id buildId(String name) {
    return new Id(name);
  }

  Id buildId(Class<?> clazz) {
    return new Id(clazz);
  }

  Id buildId(Class<?> clazz, String name) {
    return new Id(clazz, name);
  }
}
