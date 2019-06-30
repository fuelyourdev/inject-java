package dev.fuelyour.injectjava.testclasses;

public class InterfaceImpl2 implements Interface {

  private final ClassWithNoDeps noDeps;

  public InterfaceImpl2(ClassWithNoDeps noDeps) {
    this.noDeps = noDeps;
  }

  @Override
  public ClassWithNoDeps getClassWithNoDeps() {
    return noDeps;
  }
}
