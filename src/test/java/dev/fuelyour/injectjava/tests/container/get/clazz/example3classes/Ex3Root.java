package dev.fuelyour.injectjava.tests.container.get.clazz.example3classes;

public class Ex3Root {

  private final Ex3Dep1 d1;
  private final Ex3Dep2 d2;

  public Ex3Root(Ex3Dep1 d1, Ex3Dep2 d2) {
    this.d1 = d1;
    this.d2 = d2;
  }

  public Ex3Dep1 getD1() {
    return d1;
  }

  public Ex3Dep2 getD2() {
    return d2;
  }
}
