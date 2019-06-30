package dev.fuelyour.injectjava.testtools;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

public class ReplaceCamelCase extends DisplayNameGenerator.Standard {

  @Override
  public String generateDisplayNameForClass(Class<?> testClass) {
    String defaultDisplayName = super.generateDisplayNameForClass(testClass);
    String displayName = defaultDisplayName.toLowerCase().endsWith("test") ?
        defaultDisplayName.substring(0, defaultDisplayName.length() - 4) :
        defaultDisplayName;
    return "The " + displayName + " class...";
  }

  @Override
  public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
    String defaultDisplayName = super.generateDisplayNameForNestedClass(nestedClass);
    String displayName = defaultDisplayName.toLowerCase().endsWith("methodtest") ?
        defaultDisplayName.toLowerCase().charAt(0) +
            defaultDisplayName.substring(1, defaultDisplayName.length() - 10) +
            " method" :
        replaceCapitals(defaultDisplayName);
    return displayName + "...";
  }

  @Override
  public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
    return replaceCapitals(testMethod.getName()) + ".";
  }

  private String replaceCapitals(String name) {
    return name
        .replaceAll("([A-Z])", " $1")
        .replaceAll("([0-9].)", " $1")
        .toLowerCase();
  }
}
