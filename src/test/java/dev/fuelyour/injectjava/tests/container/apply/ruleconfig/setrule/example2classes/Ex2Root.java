package dev.fuelyour.injectjava.tests.container.apply.ruleconfig.setrule.example2classes;

public class Ex2Root {

  private final Ex2Dep dependency;

  public Ex2Root() {
    this.dependency = null;
  }

  public Ex2Root(Ex2Dep dependency) {
    this.dependency = dependency;
  }

  public Ex2Dep getDependency() {
    return dependency;
  }
}
