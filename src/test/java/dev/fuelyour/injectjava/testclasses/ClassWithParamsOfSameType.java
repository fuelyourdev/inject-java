package dev.fuelyour.injectjava.testclasses;

public class ClassWithParamsOfSameType {

  private final ClassWithNoDeps noDeps1;
  private final ClassWithNoDeps noDeps2;

  public ClassWithParamsOfSameType(ClassWithNoDeps noDeps1, ClassWithNoDeps noDeps2) {
    this.noDeps1 = noDeps1;
    this.noDeps2 = noDeps2;
  }

  public ClassWithNoDeps getNoDeps1() {
    return noDeps1;
  }

  public ClassWithNoDeps getNoDeps2() {
    return noDeps2;
  }
}
