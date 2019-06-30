package dev.fuelyour.injectjava.tests.container.get.clazz.example3classes;

public class Ex3Dep2 {

  private final Ex3Dep4 d4;

  public Ex3Dep2(Ex3Dep4 d4) {
    this.d4 = d4;
  }

  public Ex3Dep4 getD4() {
    return d4;
  }
}
