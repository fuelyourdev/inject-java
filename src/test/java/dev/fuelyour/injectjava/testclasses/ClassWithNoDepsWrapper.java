package dev.fuelyour.injectjava.testclasses;

public class ClassWithNoDepsWrapper extends ClassWithNoDeps {

  private ClassWithNoDeps wrapped;

  @Override
  public String getConstant() {
    return wrapped.getConstant();
  }

  public void setWrapped(ClassWithNoDeps wrapped) {
    this.wrapped = wrapped;
  }
}
