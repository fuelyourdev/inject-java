package dev.fuelyour.injectjava.tests.container.get.name.example1classes;

public class Ex1Root {

  private final Ex1Dep1 d1;
  private final Ex1Dep2 d2;

  public Ex1Root(Ex1Dep1 d1, Ex1Dep2 d2) {
    this.d1 = d1;
    this.d2 = d2;
  }

  public Ex1Dep1 getD1() {
    return d1;
  }

  public Ex1Dep2 getD2() {
    return d2;
  }
}
