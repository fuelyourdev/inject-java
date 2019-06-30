package dev.fuelyour.injectjava.tests.container.get.clazz.example3classes;

public class Ex3Dep4 {

  private final Ex3Dep5 d5;

  public Ex3Dep4(Ex3Dep5 d5) {
    this.d5 = d5;
  }

  public Ex3Dep5 getD5() {
    return d5;
  }
}
