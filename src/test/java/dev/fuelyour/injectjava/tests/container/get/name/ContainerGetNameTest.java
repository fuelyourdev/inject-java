package dev.fuelyour.injectjava.tests.container.get.name;

import dev.fuelyour.injectjava.Container;
import dev.fuelyour.injectjava.tests.container.get.name.example1classes.Ex1Dep1;
import dev.fuelyour.injectjava.tests.container.get.name.example1classes.Ex1Dep2;
import dev.fuelyour.injectjava.tests.container.get.name.example1classes.Ex1Root;
import dev.fuelyour.injectjava.tests.container.get.name.example2classes.Ex2Dep;
import dev.fuelyour.injectjava.tests.container.get.name.example2classes.Ex2Root;
import dev.fuelyour.injectjava.testtools.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static dev.fuelyour.injectjava.Container.buildContainer;
import static dev.fuelyour.injectjava.Rules.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayNameGeneration(ReplaceCamelCase.class)
@DisplayName("container.get(String name) method...")
public class ContainerGetNameTest {

  /**
   * There are times when you will need to inject instances of the same
   * class with different values. To allow for these situations, the instances
   * can be identified by a String name value instead of the class. The
   * container has a get(String name) method to allow you to access these
   * directly.
   */
  @Test
  void canAccessNamedInstancesOfAClass() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule(String.class, useInstance("default"))
        .setRule("first", useInstance("one"))
        .setRule("second", useInstance("two")));

    assertThat(container.get(String.class), is("default"));
    assertThat(container.get("first"), is("one"));
    assertThat(container.get("second"), is("two"));
  }

  /**
   * Note that a named value won't be returned when the class is specified,
   * but you can set a rule to associate a named value with the class, or vice
   * versa, essentially allowing you to pick a default for when a name isn't
   * present.
   */
  @Test
  void aNameCanBeAssociatedWithAClass() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule(String.class, useInstance("one"))
        .setRule("first", get(String.class))
        .setRule("second", useInstance("two")));

    assertThat(container.get(String.class), is("one"));
    assertThat(container.get("first"), is("one"));
    assertThat(container.get("second"), is("two"));
  }

  @Test
  void aClassCanBeAssociatedWithAName() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule("first", useInstance("one"))
        .setRule(String.class, get("first"))
        .setRule("second", useInstance("two")));

    assertThat(container.get(String.class), is("one"));
    assertThat(container.get("first"), is("one"));
    assertThat(container.get("second"), is("two"));
  }

  /**
   * You can even associate two names to the same instance. Be careful in your
   * use of this. While I can imagine use cases, I also see it as a way of
   * shooting yourself in the foot if it gets out of control.
   */
  @Test
  void twoNamesCanBeAssociatedToTheSameInstance() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule("first", useInstance("One instance"))
        .setRule("second", get("first")));

    assertThat(container.get("first"), is("One instance"));
    assertThat(container.get("second"), is("One instance"));
  }

  /**
   * To inject named values into classes that the di container builds,
   * you will need to associate the names with the corresponding constructor
   * parameters.
   */
  @Test
  void associateNameWithTheCorrespondingConstructorParameters() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule("first", useInstance("one"))
        .setRule("second", useInstance("two"))
        .setRule(Ex1Dep1.class,
            useSoleConstructor(Ex1Dep1.class,
                withParamNames(paramName("first", String.class))))
        .setRule(Ex1Dep2.class,
            useSoleConstructor(Ex1Dep2.class,
                withParamNames(paramName("second", String.class)))));
    Ex1Root root = container.get(Ex1Root.class);

    assertThat(root.getD1().getValue(), is("one"));
    assertThat(root.getD2().getValue(), is("two"));
  }

  /**
   * The parameter name is associated with the parameter class in the
   * constructor. If the constructor has multiple parameters of the
   * same class, then it will assume the first occurrence, unless
   * otherwise specified by you.
   */
  @Test
  void specifyParamNameOccurrenceWhenMultipleParamsWithSameClass() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule("first", useInstance("one"))
        .setRule("second", useInstance("two"))
        .setRule("firstDep",
            useSoleConstructor(Ex2Dep.class,
                withParamNames(paramName("first", String.class))))
        .setRule("secondDep",
            useSoleConstructor(Ex2Dep.class,
                withParamNames(paramName("second", String.class))))
        .setRule(Ex2Root.class,
            useSoleConstructor(Ex2Root.class,
                withParamNames(
                    paramName("firstDep", Ex2Dep.class),
                    paramName("secondDep", Ex2Dep.class, 2)))));

    Ex2Root root = container.get(Ex2Root.class);
    Ex2Dep firstDep = root.getFirstDep();
    Ex2Dep secondDep = root.getSecondDep();

    assertThat(firstDep, is(not(secondDep)));
    assertThat(firstDep.getValue(), is("one"));
    assertThat(secondDep.getValue(), is("two"));
  }

  @Test
  void ifRuleUnavailableForNameItFallsBackToTheClass() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule(String.class, useInstance("one"))
        .setRule("second", useInstance("two"))
        .setRule("firstDep",
            useSoleConstructor(Ex2Dep.class,
                withParamNames(paramName("first", String.class))))
        .setRule("secondDep",
            useSoleConstructor(Ex2Dep.class,
                withParamNames(paramName("second", String.class))))
        .setRule(Ex2Root.class,
            useSoleConstructor(Ex2Root.class,
                withParamNames(
                    paramName("firstDep", Ex2Dep.class),
                    paramName("secondDep", Ex2Dep.class, 2)))));

    Ex2Root root = container.get(Ex2Root.class);
    Ex2Dep firstDep = root.getFirstDep();
    Ex2Dep secondDep = root.getSecondDep();

    assertThat(firstDep, is(not(secondDep)));
    assertThat(firstDep.getValue(), is("one"));
    assertThat(secondDep.getValue(), is("two"));
  }
}
