package dev.fuelyour.injectjava.tests.container.apply.example2classes;

public class Ex2Root {
  private final Ex2Dep1 d1;
  private final Ex2Dep2 d2;

  public Ex2Root(Ex2Dep1 d1) {
    this.d1 = d1;
    this.d2 = null;
  }

  public Ex2Root(Ex2Dep2 d2) {
    this.d1 = null;
    this.d2 = d2;
  }

  public Ex2Dep1 getD1() {
    return d1;
  }

  public Ex2Dep2 getD2() {
    return d2;
  }
}
