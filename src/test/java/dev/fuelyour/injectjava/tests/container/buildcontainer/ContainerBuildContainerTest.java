package dev.fuelyour.injectjava.tests.container.buildcontainer;


import dev.fuelyour.injectjava.Container;
import dev.fuelyour.injectjava.testtools.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static dev.fuelyour.injectjava.Container.buildContainer;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayNameGeneration(ReplaceCamelCase.class)
@DisplayName("Container.buildContainer() method...")
public class ContainerBuildContainerTest {

  /**
   * So to start out, the Container class has a static method called
   * buildContainer(). Use this to get the dependency injection
   * container.
   */
  @Test
  void isAStaticMethodThatGetsTheContainer() {
    Container container = Container.buildContainer();
    assertThat(container, is(notNullValue()));
  }

  /**
   * Note that by doing a static import of the buildContainer method, you
   * can remove the "Container." at the beginning of the call, which can
   * allow for a shorter, cleaner syntax.
   */
  @Test
  void canBeStaticallyImportedForCleanerSyntax() {
    Container container = buildContainer();
    assertThat(container, is(notNullValue()));
  }
}

