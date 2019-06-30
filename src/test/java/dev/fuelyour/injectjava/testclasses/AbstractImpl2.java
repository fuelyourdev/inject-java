package dev.fuelyour.injectjava.testclasses;

public class AbstractImpl2 extends Abstract {

  private final ClassWithNoDeps noDeps;

  public AbstractImpl2(ClassWithNoDeps noDeps) {
    this.noDeps = noDeps;
  }

  @Override
  public ClassWithNoDeps getClassWithNoDeps() {
    return noDeps;
  }
}
