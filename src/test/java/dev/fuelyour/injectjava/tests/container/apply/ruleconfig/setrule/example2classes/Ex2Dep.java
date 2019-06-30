package dev.fuelyour.injectjava.tests.container.apply.ruleconfig.setrule.example2classes;

public class Ex2Dep {

  private final String firstName;
  private final String lastName;

  public Ex2Dep(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }
}
