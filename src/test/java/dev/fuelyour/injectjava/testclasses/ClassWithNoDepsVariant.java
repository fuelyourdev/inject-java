package dev.fuelyour.injectjava.testclasses;

public class ClassWithNoDepsVariant extends ClassWithNoDeps {

  public static final String CONSTANT = "constantVariant";

  @Override
  public String getConstant() {
    return CONSTANT;
  }
}
