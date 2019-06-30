package dev.fuelyour.injectjava.tests.container.apply;

import dev.fuelyour.injectjava.Container;
import dev.fuelyour.injectjava.exceptions.MultiConstructorException;
import dev.fuelyour.injectjava.tests.container.apply.example1classes.*;
import dev.fuelyour.injectjava.tests.container.apply.example2classes.*;
import dev.fuelyour.injectjava.tests.container.apply.example3classes.*;
import dev.fuelyour.injectjava.testtools.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.fuelyour.injectjava.Container.buildContainer;
import static dev.fuelyour.injectjava.Rules.useConstructor;
import static dev.fuelyour.injectjava.Rules.withParams;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(ReplaceCamelCase.class)
@DisplayName("container.apply(Module module) method...")
public class ContainerApplyModuleTest {

  /**
   * Now there are a number of cases where it can't automatically figure out the
   * right way to build something. Take, for instance, a class with multiple
   * constructors. It has no way of knowing which constructor you intend for it
   * to use, and will throw an exception without guidance.
   */
  @Test
  @DisplayName("a multi constructor class will cause an exception if no " +
      "module is applied with a rule to specify which constructor to use")
  void aMultiConstructorClassWillCauseAnException() {
    Container container = buildContainer();
    assertThrows(MultiConstructorException.class,
        () -> container.get(Ex1Root.class));
  }

  /*
   * To fix this, you need to apply a module that has a rule specifying which
   * constructor to use. (We'll go into more detail on the specifics of rules
   * later.)
   */
  @Test
  @DisplayName("can be used to specify rules, such as which constructor " +
      "to use when there are multiple available")
  void canBeUsedToSpecifyRules() {
    Container container = buildContainer();
    container.apply(rc -> rc.setRule(
        Ex1Root.class,
        useConstructor(Ex1Root.class,
            withParams(Ex1Dep1.class))));
    Ex1Root root = container.get(Ex1Root.class);

    assertThat(root, is(notNullValue()));
    assertThat(root.getD1(), is(notNullValue()));
    assertThat(root.getD2(), is(nullValue()));
  }

  /*
   * By applying multiple modules, you can add the rules from each module, or
   * override rules applied in earlier modules with rules applied in later
   * modules.
   */
  @Test
  void laterModulesCanAddRulesAndOverrideRulesFromEarlierModules() {
    Container container = buildContainer();

    container.apply(rc -> rc.setRule(
        Ex1Root.class,
        useConstructor(Ex1Root.class,
            withParams(Ex1Dep1.class))));

    container.apply(rc -> rc
        .setRule(
            Ex1Root.class,
            useConstructor(Ex1Root.class,
                withParams(Ex1Dep2.class)))
        .setRule(
            Ex1Dep2.class,
            useConstructor(Ex1Dep2.class,
                withParams(Ex1Dep3.class))));

    Ex1Root root = container.get(Ex1Root.class);

    assertThat(root, is(notNullValue()));
    assertThat(root.getD1(), is(nullValue()));
    assertThat(root.getD2(), is(notNullValue()));
    assertThat(root.getD2().getD3(), is(notNullValue()));
  }

  /*
   * This can be very useful for integration tests where you want to test
   * everything together, but want to swap out a couple of classes for testing
   * purposes.
   */
  @Nested
  class canBeUsefulForIntegrationTesting {

    @Test
    void integrationTestProgramMainExample() {
      Ex2Main.main(new String[]{});
      Ex2Root root = Ex2Main.getRoot();
      assertThat(root.getD1(), is(notNullValue()));
      assertThat(root.getD2(), is(nullValue()));

      Ex2Main.programMain(new String[]{},
          rc -> rc.setRule(Ex2Root.class,
              useConstructor(Ex2Root.class,
                  withParams(Ex2Dep2.class))));
      root = Ex2Main.getRoot();
      assertThat(root, is(notNullValue()));
      assertThat(root.getD1(), is(nullValue()));
      assertThat(root.getD2(), is(notNullValue()));
    }

    @Test
    void integrationTestArgExample() {
      Ex3Main.main(new String[]{});
      Ex3Root root = Ex3Main.getRoot();
      assertThat(root, is(notNullValue()));
      assertThat(root.getD1(), is(notNullValue()));
      assertThat(root.getD2(), is(nullValue()));

      Ex3Main.main(new String[]{TestModule.class.getName()});
      root = Ex3Main.getRoot();
      assertThat(root.getD1(), is(nullValue()));
      assertThat(root.getD2(), is(notNullValue()));
    }
  }
}

