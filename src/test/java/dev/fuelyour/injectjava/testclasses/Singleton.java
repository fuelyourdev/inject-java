package dev.fuelyour.injectjava.testclasses;

public class Singleton {

  private static Singleton instance = null;

  public static Singleton instance() {
    if (instance == null) {
      instance = new Singleton();
    }
    return instance;
  }

  private Singleton() {

  }
}
