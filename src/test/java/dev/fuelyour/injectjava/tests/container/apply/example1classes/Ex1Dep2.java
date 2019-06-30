package dev.fuelyour.injectjava.tests.container.apply.example1classes;

public class Ex1Dep2 {

  private final Ex1Dep3 d3;

  public Ex1Dep2() {
    this.d3 = null;
  }

  public Ex1Dep2(Ex1Dep3 d3) {
    this.d3 = d3;
  }

  public Ex1Dep3 getD3() {
    return d3;
  }
}
