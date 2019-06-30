package dev.fuelyour.injectjava.tests.container.get.clazz.example4classes;

public class Ex4Dep2 {

  private final Ex4Dep3 d3;

  public Ex4Dep2(Ex4Dep3 d3) {
    this.d3 = d3;
  }

  public Ex4Dep3 getD3() {
    return d3;
  }
}
