package dev.fuelyour.injectjava.tests.container.get.clazz.example3classes;

public class Ex3Dep1 {

  private final Ex3Dep3 d3;

  public Ex3Dep1(Ex3Dep3 d3) {
    this.d3 = d3;
  }

  public Ex3Dep3 getD3() {
    return d3;
  }
}
