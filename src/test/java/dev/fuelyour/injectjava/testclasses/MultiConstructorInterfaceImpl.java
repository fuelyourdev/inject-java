package dev.fuelyour.injectjava.testclasses;

@SuppressWarnings("unused")
public class MultiConstructorInterfaceImpl implements Interface {

  private final ClassWithNoDeps noDeps;

  public MultiConstructorInterfaceImpl() {
    this.noDeps = null;
  }

  public MultiConstructorInterfaceImpl(ClassWithNoDeps noDeps) {
    this.noDeps = noDeps;
  }

  @Override
  public ClassWithNoDeps getClassWithNoDeps() {
    return noDeps;
  }
}
