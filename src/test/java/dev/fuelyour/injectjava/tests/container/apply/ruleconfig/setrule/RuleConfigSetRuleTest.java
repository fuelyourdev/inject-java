package dev.fuelyour.injectjava.tests.container.apply.ruleconfig.setrule;

import dev.fuelyour.injectjava.Container;
import dev.fuelyour.injectjava.tests.container.apply.ruleconfig.setrule.example1classes.Ex1Root;
import dev.fuelyour.injectjava.tests.container.apply.ruleconfig.setrule.example2classes.Ex2Dep;
import dev.fuelyour.injectjava.tests.container.apply.ruleconfig.setrule.example2classes.Ex2Root;
import dev.fuelyour.injectjava.testtools.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.fuelyour.injectjava.Container.buildContainer;
import static dev.fuelyour.injectjava.Rules.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A rule is used to map a class or a name value to a lambda used to produce an
 * instance for when the default construction settings are insufficient.
 */
@DisplayNameGeneration(ReplaceCamelCase.class)
@DisplayName("ruleConfig.setRule(...) method ...")
public class RuleConfigSetRuleTest {

  /**
   * There are four variants of the setRule method, each to allow for setting
   * a rule for a class or a name, and to allow you to choose whether or not
   * you want a SimpleContainer instance supplied to your lambda. The
   * SimpleContainer supplies various access methods to getting other objects
   * stored in the container, specifying parameter names, and specifying what
   * class or constructor to use.
   */
  @Nested
  class FourVariantsOfSetRule {

    @Test
    void setRuleClassSupplier() {
      Container container = buildContainer();
      container.apply(rc -> rc.setRule(String.class, () -> "Hello World"));
      String greeting = container.get(String.class);

      assertThat(greeting, is("Hello World"));
    }

    @Test
    void setRuleClassFunction() {
      Container container = buildContainer();
      container.apply(rc -> rc.setRule(
          Ex1Root.class,
          c -> c.buildWithConstructor(Ex1Root.class, new Class<?>[]{})));
      Ex1Root root = container.get(Ex1Root.class);

      assertThat(root.getValue(), is(nullValue()));
    }

    @Test
    void setRuleNameSupplier() {
      Container container = buildContainer();
      container.apply(rc -> rc.setRule("greeting", () -> "Hello World"));
      String greeting = container.get("greeting");

      assertThat(greeting, is("Hello World"));
    }

    @Test
    void setRuleNameFunction() {
      Container container = buildContainer();
      container.apply(rc -> rc.setRule(
          "root",
          c -> c.buildWithConstructor(Ex1Root.class, new Class<?>[]{})));
      Ex1Root root = container.get("root");

      assertThat(root.getValue(), is(nullValue()));
    }
  }

  /**
   * Commonly used rules have helper methods built out for them in the Rules
   * class.
   */
  @Test
  void ruleHelpers() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule(String.class, useInstance("Bob"))
        .setRule("firstName", get(String.class))
        .setRule("lastName", useInstance("Smith"))
        .setRule(Ex2Dep.class,
            useSoleConstructor(Ex2Dep.class,
                withParamNames(
                    paramName("firstName", String.class),
                    paramName("lastName", String.class, 2))))
        .setRule(Ex2Root.class,
            useConstructor(Ex2Root.class,
                withParams(Ex2Dep.class))));
    Ex2Root root = container.get(Ex2Root.class);

    assertThat(root.getDependency(), is(notNullValue()));
    assertThat(root.getDependency().getFirstName(), is("Bob"));
    assertThat(root.getDependency().getLastName(), is("Smith"));
  }
}
