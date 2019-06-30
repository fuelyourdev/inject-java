package dev.fuelyour.injectjava.tests.container.get.clazz;


import dev.fuelyour.injectjava.Container;
import dev.fuelyour.injectjava.tests.container.get.clazz.example1classes.*;
import dev.fuelyour.injectjava.tests.container.get.clazz.example2classes.*;
import dev.fuelyour.injectjava.tests.container.get.clazz.example3classes.*;
import dev.fuelyour.injectjava.tests.container.get.clazz.example4classes.*;
import dev.fuelyour.injectjava.testtools.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static dev.fuelyour.injectjava.Container.buildContainer;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayNameGeneration(ReplaceCamelCase.class)
@DisplayName("container.get(Class<T> clazz) method...")
public class ContainerGetClassTest {

  /**
   * Next we'll want to take a look at the get(Class<T> clazz) method. Calling
   * this method (or the related method get(String name)) is what will generally
   * kick off the building of your object graph.
   * <br /><br />
   * It will use Java Reflection to build and return an instance of the class
   * specified.
   */
  @Test
  void canBuildAndReturnAnInstanceOfTheSpecifiedClass () {
    Container container = buildContainer();
    Ex1Root root = container.get(Ex1Root.class);
    assertThat(root, is(instanceOf(Ex1Root.class)));
  }

  /**
   * In addition, it will build and supply any dependencies in the constructor,
   * so long as it can determine how to build them.
   */
  @Test
  void willBuildAndSupplyTheDependenciesSpecifiedByTheConstructor() {
    Container container = buildContainer();
    Ex2Root root = container.get(Ex2Root.class);

    assertThat(root, is(instanceOf(Ex2Root.class)));
    assertThat(root.getD1(), is(instanceOf(Ex2Dep1.class)));
    assertThat(root.getD2(), is(instanceOf(Ex2Dep2.class)));
  }

  /**
   * And it will do the same for any dependencies of the dependencies, and so
   * on.
   */
  @Test
  void willBuildTheDependenciesOfTheDependenciesAndSoOn() {
    Container container = buildContainer();
    Ex3Root root = container.get(Ex3Root.class);

    assertThat(root, is(instanceOf(Ex3Root.class)));

    Ex3Dep1 d1 = root.getD1();
    assertThat(d1, is(instanceOf(Ex3Dep1.class)));

    Ex3Dep2 d2 = root.getD2();
    assertThat(d2, is(instanceOf(Ex3Dep2.class)));

    Ex3Dep3 d3 = d1.getD3();
    assertThat(d3, is(instanceOf(Ex3Dep3.class)));

    Ex3Dep4 d4 = d2.getD4();
    assertThat(d4, is(instanceOf(Ex3Dep4.class)));

    Ex3Dep5 d5 = d4.getD5();
    assertThat(d5, is(instanceOf(Ex3Dep5.class)));
  }

  /**
   * It will construct each class as a singleton, and will pass the same single
   * instance into all places that request it.
   */
  @Test
  @DisplayName("will create a single instance of each class that is reused " +
      "for multiple references.")
  void willCreateASingleInstanceOfEachClass() {
    Container container = buildContainer();
    Ex4Root root = container.get(Ex4Root.class);
    Ex4Dep1 d1 = container.get(Ex4Dep1.class);
    Ex4Dep2 d2 = container.get(Ex4Dep2.class);
    Ex4Dep3 d3 = container.get(Ex4Dep3.class);

    assertThat(d1, is(root.getD1()));
    assertThat(d2, is(root.getD2()));
    assertThat(d2, is(d1.getD2()));
    assertThat(d3, is(d1.getD3()));
    assertThat(d3, is(d2.getD3()));
  }
}

