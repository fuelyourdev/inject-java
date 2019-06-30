package dev.fuelyour.injectjava.testclasses;

import static dev.fuelyour.injectjava.testclasses.ConstructorUsed.NoArg;
import static dev.fuelyour.injectjava.testclasses.ConstructorUsed.OneArg;

public class MultiConstructorClass {

  private final ClassWithNoDeps noDeps;
  private final ConstructorUsed constructorUsed;

  public MultiConstructorClass() {
    noDeps = null;
    constructorUsed = NoArg;
  }

  public MultiConstructorClass(ClassWithNoDeps noDeps) {
    this.noDeps = noDeps;
    constructorUsed = OneArg;
  }

  public ClassWithNoDeps getClassWithNoDeps() {
    return noDeps;
  }

  public ConstructorUsed getConstructorUsed() {
    return constructorUsed;
  }
}
