package dev.fuelyour.injectjava.tests.container.get.clazz.example4classes;

public class Ex4Dep1 {

  private final Ex4Dep2 d2;
  private final Ex4Dep3 d3;

  public Ex4Dep1(Ex4Dep2 d2, Ex4Dep3 d3) {
    this.d2 = d2;
    this.d3 = d3;
  }

  public Ex4Dep2 getD2() {
    return d2;
  }

  public Ex4Dep3 getD3() {
    return d3;
  }
}
