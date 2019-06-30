package dev.fuelyour.injectjava.testclasses;

public class ClassWithTwoDeps {

  private final ClassWithNoDeps noDeps;
  private final ClassWithOneDep oneDep;

  public ClassWithTwoDeps(ClassWithNoDeps noDeps, ClassWithOneDep oneDep) {
    this.noDeps = noDeps;
    this.oneDep = oneDep;
  }

  public ClassWithNoDeps getClassWithNoDeps() {
    return noDeps;
  }

  public ClassWithOneDep getClassWithOneDep() {
    return oneDep;
  }
}
