package dev.fuelyour.injectjava.testclasses;

public class ClassWithOneDep {

  private final ClassWithNoDeps noDeps;

  public ClassWithOneDep(ClassWithNoDeps noDeps) {
    this.noDeps = noDeps;
  }

  public ClassWithNoDeps getClassWithNoDeps() {
    return noDeps;
  }
}
