package dev.fuelyour.injectjava.tests.container.get.name.example2classes;

public class Ex2Root {

  private final Ex2Dep firstDep;
  private final Ex2Dep secondDep;

  public Ex2Root(Ex2Dep firstDep, Ex2Dep secondDep) {
    this.firstDep = firstDep;
    this.secondDep = secondDep;
  }

  public Ex2Dep getFirstDep() {
    return firstDep;
  }

  public Ex2Dep getSecondDep() {
    return secondDep;
  }
}
