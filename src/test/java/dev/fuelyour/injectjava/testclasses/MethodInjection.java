package dev.fuelyour.injectjava.testclasses;

public class MethodInjection {

  private ClassWithNoDeps noDeps;

  public ClassWithNoDeps getNoDeps() {
    return noDeps;
  }

  public void setNoDeps(ClassWithNoDeps noDeps) {
    this.noDeps = noDeps;
  }
}
