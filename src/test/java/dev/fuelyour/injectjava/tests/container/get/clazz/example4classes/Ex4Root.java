package dev.fuelyour.injectjava.tests.container.get.clazz.example4classes;

public class Ex4Root {

  private final Ex4Dep1 d1;
  private final Ex4Dep2 d2;

  public Ex4Root(Ex4Dep1 d1, Ex4Dep2 d2) {
    this.d1 = d1;
    this.d2 = d2;
  }

  public Ex4Dep1 getD1() {
    return d1;
  }

  public Ex4Dep2 getD2() {
    return d2;
  }
}
