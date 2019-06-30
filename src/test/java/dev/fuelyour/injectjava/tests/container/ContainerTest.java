package dev.fuelyour.injectjava.tests.container;

import dev.fuelyour.injectjava.Container;
import dev.fuelyour.injectjava.exceptions.MultiConstructorException;
import dev.fuelyour.injectjava.exceptions.NoSuchConstructorException;
import dev.fuelyour.injectjava.testclasses.*;
import dev.fuelyour.injectjava.testtools.ReplaceCamelCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.fuelyour.injectjava.Container.buildContainer;
import static dev.fuelyour.injectjava.GeneralRules.useDefaultConstructor;
import static dev.fuelyour.injectjava.GeneralRules.useInterfaceImplRule;
import static dev.fuelyour.injectjava.Rules.*;
import static dev.fuelyour.injectjava.testclasses.ConstructorUsed.NoArg;
import static dev.fuelyour.injectjava.testclasses.ConstructorUsed.OneArg;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * There are a number of dependency injection frameworks on the market, and
 * numerous available for Java. From the Spring framework that has dependeny
 * injection as just one of many tools, to the standalone frameworks Guice and
 * Dagger 2, there's already a lot to choose from. So why did I build yet
 * another dependency injection framework?
 * <br /><br />
 * Well, honestly part of the reason was to simply learn and grow as a
 * developer. But there was also the more practical reason in that each of the
 * aforementioned frameworks require annotating the classes before the
 * framework will take those classes and compose them into an object graph, and
 * to be honest I don't much care for that. Annotating each class in a small
 * way is making each class aware of the dependency injection framework. I like
 * the idea of keeping all the configuration in the composition root, and
 * keeping the classes unaware of the dependency injection container.
 * <br /><br />
 * So I've built inject-java, a dependency injection container that doesn't
 * require that classes be annotated, but rather will strive to intelligently
 * build your object graph, while allowing you to specify rules to clarify
 * situations that it is unable to figure out on its own.
 * <br /><br />
 * Sections<br />
 * {@link dev.fuelyour.injectjava.tests.container.buildcontainer.ContainerBuildContainerTest}
 * {@link dev.fuelyour.injectjava.tests.container.get.clazz.ContainerGetClassTest}
 * {@link dev.fuelyour.injectjava.tests.container.apply.ContainerApplyModuleTest}
 * {@link dev.fuelyour.injectjava.tests.container.get.name.ContainerGetNameTest}
 * {@link dev.fuelyour.injectjava.tests.container.apply.ruleconfig.setrule.RuleConfigSetRuleTest}
 */
@SuppressWarnings("WeakerAccess")
@DisplayNameGeneration(ReplaceCamelCase.class)
public class ContainerTest {

  @Nested
  class oldTests {
    @Nested
    class IfClassHasMultipleConstructors {

      @Test
      void anExceptionShouldBeThrown() {
        Container container = buildContainer();
        MultiConstructorException ex = assertThrows(
            MultiConstructorException.class,
            () -> container.get(MultiConstructorClass.class));
        String expectedMessage = MultiConstructorClass.class.getSimpleName() +
            " has multiple constructors";
        assertThat(ex.getMessage(), containsString(expectedMessage));
      }

      @Nested
      class TheConstructorCanBeSpecifiedWithARule {

        @Test
        @SuppressWarnings("Convert2MethodRef")
        void noArgConstructorScenario() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(MultiConstructorClass.class,
              useConstructor(MultiConstructorClass.class, withParams())));
          MultiConstructorClass multi = container.get(
              MultiConstructorClass.class);
          assertThat(multi, is(notNullValue()));
          assertThat(multi.getConstructorUsed(), is(NoArg));
          assertThat(multi.getClassWithNoDeps(), is(nullValue()));
        }

        @Test
        void oneArgConstructorScenario() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(MultiConstructorClass.class,
              useConstructor(MultiConstructorClass.class,
                  withParams(ClassWithNoDeps.class))));
          MultiConstructorClass multi = container.get(
              MultiConstructorClass.class);
          assertThat(multi, is(notNullValue()));
          assertThat(multi.getConstructorUsed(), is(OneArg));
          assertThat(multi.getClassWithNoDeps(), is(notNullValue()));
        }
      }

      @Nested
      class TheConstructorCanBeSpecifiedByClassAndParams {

        @Test
        void noArgConstructorScenario() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(
              MultiConstructorClass.class,
              useConstructor(MultiConstructorClass.class, withParams())));
          MultiConstructorClass multi = container.get(
              MultiConstructorClass.class);
          assertThat(multi, is(notNullValue()));
          assertThat(multi.getConstructorUsed(), is(NoArg));
          assertThat(multi.getClassWithNoDeps(), is(nullValue()));
        }

        @Test
        void oneArgConstructorScenario() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(
              MultiConstructorClass.class,
              useConstructor(MultiConstructorClass.class,
                  withParams(ClassWithNoDeps.class))));
          MultiConstructorClass multi = container.get(
              MultiConstructorClass.class);
          assertThat(multi, is(notNullValue()));
          assertThat(multi.getConstructorUsed(), is(OneArg));
          assertThat(multi.getClassWithNoDeps(), is(notNullValue()));
        }
      }

      @Test
      void specifyingParamsForANonExistentConstructorWillThrowAnException() {
        Container container = buildContainer();
        container.apply(rc -> rc.setRule(
            MultiConstructorClass.class,
            useConstructor(MultiConstructorClass.class,
                withParams(ClassWithTwoDeps.class))));
        assertThrows(NoSuchConstructorException.class,
            () -> container.get(MultiConstructorClass.class));
      }

      @Nested
      class AnAlreadyInstantiatedObjectCanBeUsed {

        @Test
        void noArgConstructorScenario() {
          Container container = buildContainer();
          MultiConstructorClass multiUsed = new MultiConstructorClass();
          container.apply(rc -> rc.setRule(MultiConstructorClass.class,
              useInstance(multiUsed)));
          MultiConstructorClass multi = container.get(
              MultiConstructorClass.class);
          assertThat(multi, is(notNullValue()));
          assertThat(multi.getConstructorUsed(), is(NoArg));
          assertThat(multi.getClassWithNoDeps(), is(nullValue()));
          assertThat(multi, is(multiUsed));
        }

        @Test
        void oneArgConstructorScenario() {
          Container container = buildContainer();
          ClassWithNoDeps noDepsUsed = new ClassWithNoDeps();
          MultiConstructorClass multiUsed =
              new MultiConstructorClass(noDepsUsed);
          container.apply(rc -> rc.setRule(MultiConstructorClass.class,
              useInstance(multiUsed)));
          MultiConstructorClass multi = container.get(
              MultiConstructorClass.class);
          assertThat(multi, is(notNullValue()));
          assertThat(multi.getConstructorUsed(), is(OneArg));
          assertThat(multi.getClassWithNoDeps(), is(notNullValue()));
          assertThat(multi, is(multiUsed));
          assertThat(multi.getClassWithNoDeps(), is(noDepsUsed));

          ClassWithNoDeps noDeps = container.get(ClassWithNoDeps.class);
          assertThat(noDeps, is(notNullValue()));
          assertThat(noDeps, is(not(noDepsUsed)));
        }
      }

      @Test
      void theUseDefaultConstructorsSettingCanBeTurnedOn() {
        Container container = buildContainer();
        container.apply(rc -> rc.setGeneralRule(
            "useDefaultConstructor",
            useDefaultConstructor()));
        MultiConstructorClass multi = container.get(
            MultiConstructorClass.class);
        assertThat(multi, is(notNullValue()));
        assertThat(multi.getConstructorUsed(), is(NoArg));
        assertThat(multi.getClassWithNoDeps(), is(nullValue()));
      }

      @Test
      void aRuleOverridesTheUseDefaultConstructorsSetting() {
        Container container = buildContainer();
        container.apply(rc -> rc
            .setGeneralRule(
                "useDefaultConstructor",
                useDefaultConstructor())
            .setRule(
                MultiConstructorClass.class,
                useConstructor(MultiConstructorClass.class,
                    withParams(ClassWithNoDeps.class))));
        MultiConstructorClass multi = container.get(
            MultiConstructorClass.class);
        assertThat(multi, is(notNullValue()));
        assertThat(multi.getConstructorUsed(), is(OneArg));
        assertThat(multi.getClassWithNoDeps(), is(notNullValue()));
      }
    }

    @Nested
    class IfClassIsAnInterface {

      @Test
      void willThrowAnException() {
        Container container = buildContainer();
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> container.get(Interface.class));
        String expectedMessage = "Interface is an interface. " +
            "Please specify an implementing class with a Rule.";
        assertThat(ex.getMessage(), containsString(expectedMessage));
      }

      @Nested
      class ASubclassCanBeSpecifiedWithARule {

        @Test
        void scenario1() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(Interface.class,
              get(InterfaceImpl.class)));
          Interface i = container.get(Interface.class);
          InterfaceImpl impl = container.get(InterfaceImpl.class);
          assertThat(i, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(i, is(instanceOf(InterfaceImpl.class)));
          assertThat(i.getClassWithNoDeps(), is(nullValue()));
          assertThat(i, is(impl));
        }

        @Test
        void scenario2() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(Interface.class,
              get(InterfaceImpl2.class)));
          Interface i = container.get(Interface.class);
          InterfaceImpl2 impl = container.get(InterfaceImpl2.class);
          assertThat(i, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(i, is(instanceOf(InterfaceImpl2.class)));
          assertThat(i.getClassWithNoDeps(), is(notNullValue()));
          assertThat(i, is(impl));
        }
      }

      @Nested
      class ASubclassCanBeSpecifiedWithASubclassRule {

        @Test
        void scenario1() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(Interface.class,
              get(InterfaceImpl.class)));
          Interface i = container.get(Interface.class);
          InterfaceImpl impl = container.get(InterfaceImpl.class);
          assertThat(i, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(i, is(instanceOf(InterfaceImpl.class)));
          assertThat(i.getClassWithNoDeps(), is(nullValue()));
          assertThat(i, is(impl));
        }

        @Test
        void scenario2() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(Interface.class,
              get(InterfaceImpl2.class)));
          Interface i = container.get(Interface.class);
          InterfaceImpl2 impl = container.get(InterfaceImpl2.class);
          assertThat(i, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(i, is(instanceOf(InterfaceImpl2.class)));
          assertThat(i.getClassWithNoDeps(), is(notNullValue()));
          assertThat(i, is(impl));
        }
      }

      @Nested
      class AndSubclassHasMultipleConstructors {

        @Test
        void specifyingOnlySubclassRuleWillGiveMultipleConstructorError() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(Interface.class,
              get(MultiConstructorInterfaceImpl.class)));
          assertThrows(MultiConstructorException.class,
              () -> container.get(Interface.class));
        }

        @Test
        void canBeSpecifiedWithSubclassAndConstructorRules() {
          Container container = buildContainer();
          container.apply(rc -> rc
              .setRule(Interface.class,
                  get(MultiConstructorInterfaceImpl.class))
              .setRule(MultiConstructorInterfaceImpl.class,
                  useConstructor(MultiConstructorInterfaceImpl.class,
                      withParams())));
          Interface i = container.get(Interface.class);
          MultiConstructorInterfaceImpl impl =
              container.get(MultiConstructorInterfaceImpl.class);
          assertThat(i, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(i.getClassWithNoDeps(), is(nullValue()));
          assertThat(i, is(impl));
        }

        @Test
        void canUseTheUseDefaultConstructorSetting() {
          Container container = buildContainer();
          container.apply(rc -> rc
              .setGeneralRule(
                  "useDefaultConstructor",
                  useDefaultConstructor())
              .setRule(
                  Interface.class,
                  get(MultiConstructorInterfaceImpl.class)));
          Interface i = container.get(Interface.class);
          MultiConstructorInterfaceImpl impl = container.get(
              MultiConstructorInterfaceImpl.class);
          assertThat(i, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(i.getClassWithNoDeps(), is(nullValue()));
          assertThat(i, is(impl));
        }
      }
    }

    @Nested
    class IfClassIsAbstract {

      @Test
      void anExceptionWillBeThrown() {
        Container container = buildContainer();
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> container.get(Abstract.class));
        String expectedMessage = Abstract.class.getSimpleName() +
            " is abstract. Please specify a rule for how to build.";
        assertThat(ex.getMessage(), is(expectedMessage));
      }

      @Nested
      class ARuleCanBeUsed {

        @Test
        void scenario1() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(Abstract.class,
              get(AbstractImpl.class)));
          Abstract a = container.get(Abstract.class);
          AbstractImpl impl = container.get(AbstractImpl.class);
          assertThat(a, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(a.getClassWithNoDeps(), is(nullValue()));
          assertThat(a, is(impl));
        }

        @Test
        void scenario2() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(Abstract.class,
              get(AbstractImpl2.class)));
          Abstract a = container.get(Abstract.class);
          AbstractImpl2 impl = container.get(AbstractImpl2.class);
          assertThat(a, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(a.getClassWithNoDeps(), is(notNullValue()));
          assertThat(a, is(impl));
        }
      }

      @Nested
      class ASubclassRuleCanBeUsed {

        @Test
        void scenario1() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(
              Abstract.class,
              get(AbstractImpl.class)));
          Abstract a = container.get(Abstract.class);
          AbstractImpl impl = container.get(AbstractImpl.class);
          assertThat(a, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(a.getClassWithNoDeps(), is(nullValue()));
          assertThat(a, is(impl));
        }

        @Test
        void scenario2() {
          Container container = buildContainer();
          container.apply(rc -> rc.setRule(
              Abstract.class,
              get(AbstractImpl2.class)));
          Abstract a = container.get(Abstract.class);
          AbstractImpl2 impl = container.get(AbstractImpl2.class);
          assertThat(a, is(notNullValue()));
          assertThat(impl, is(notNullValue()));
          assertThat(a.getClassWithNoDeps(), is(notNullValue()));
          assertThat(a, is(impl));
        }
      }
    }

    @Test
    void ARuleCanSpecifyASubclassInPlaceOfAParentClass() {
      Container container = buildContainer();
      container.apply(rc -> rc.setRule(
          ParentClass.class,
          get(ChildClass.class)));
      ParentClass p = container.get(ParentClass.class);
      ChildClass c = container.get(ChildClass.class);
      assertThat(p, is(notNullValue()));
      assertThat(c, is(notNullValue()));
      assertThat(p, is(c));
    }

    @Test
    void ASubclassRuleCanSpecifyASubclassInPlaceofAParentClass() {
      Container container = buildContainer();
      container.apply(rc -> rc.setRule(
          ParentClass.class,
          get(ChildClass.class)));
      ParentClass p = container.get(ParentClass.class);
      ChildClass c = container.get(ChildClass.class);
      assertThat(p, is(notNullValue()));
      assertThat(c, is(notNullValue()));
      assertThat(p, is(c));
    }

    @Nested
    class IfClassUsesTheSingletonPattern {

      @Test
      void anExceptionWillBeThrown() {
        Container container = buildContainer();
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> container.get(Singleton.class));
        String expectedMessage = "Singleton has no available constructors";
        assertThat(ex.getMessage(), containsString(expectedMessage));
      }

      @Test
      void aRuleCanBeSpecified() {
        Container container = buildContainer();
        container.apply(rc -> rc.setRule(Singleton.class, Singleton::instance));
        Singleton s = container.get(Singleton.class);
        assertThat(s, is(notNullValue()));
      }
    }

    @Test
    void circularReferencesWillBeDetectedAndThrowAnException() {
      Container container = buildContainer();
      RuntimeException ex = assertThrows(RuntimeException.class,
          () -> container.get(CircularRef1.class));
      String expectedMessage = "Circular dependency detected";
      assertThat(ex.getMessage(), containsString(expectedMessage));
    }
  }

  @Nested
  @DisplayName("get(String name) method...")
  class BuildStringMethodTest {

    @Nested
    class CanBuildAClassWithNoDependencies {

      @Test
      void ifRuleUsesRcGetThenGettingTheClassAndGettingTheIdWillBeTheSame() {
        Container container = buildContainer();
        container.apply(rc -> rc
            .setRule("noDeps", get(ClassWithNoDeps.class))
            .setRule("noDeps2", useSoleConstructor(ClassWithNoDeps.class)));
        ClassWithNoDeps noDeps = container.get("noDeps");
        ClassWithNoDeps noDeps2 = container.get("noDeps2");
        ClassWithNoDeps noDepsClass = container.get(ClassWithNoDeps.class);
        assertThat(noDeps, is(notNullValue()));
        assertThat(noDeps2, is(notNullValue()));
        assertThat(noDepsClass, is(notNullValue()));
        assertThat(noDeps, is(not(noDeps2)));
        assertThat(noDeps, is(noDepsClass));
        assertThat(noDeps2, is(not(noDepsClass)));
      }

      @Test
      void ifRcGetInNotUsedThenGettingTheClassAndGettingTheIdWillDiffer() {
        Container container = buildContainer();
        container.apply(rc -> rc.setRule("noDeps",
            useSoleConstructor(ClassWithNoDeps.class)));
        ClassWithNoDeps noDeps = container.get("noDeps");
        ClassWithNoDeps noDepsClass = container.get(ClassWithNoDeps.class);
        assertThat(noDeps, is(notNullValue()));
        assertThat(noDepsClass, is(notNullValue()));
        assertThat(noDeps, is(not(noDepsClass)));
      }
    }

    @Test
    void generalRuleTest() {
      Container container = buildContainer();
      container.apply(rc -> rc.setGeneralRule(
          "InterfaceImplRule",
          useInterfaceImplRule()));
      Interface i = container.get(Interface.class);
      assertThat(i, is(instanceOf(InterfaceImpl.class)));
      Abstract a = container.get(Abstract.class);
      assertThat(a, is(instanceOf(AbstractImpl.class)));
    }

    @Test
    void generalRuleTest2() {
      Container container = buildContainer();
      container.apply(rc -> rc.setGeneralRule(
          useInterfaceImplRule()));
      Interface i = container.get(Interface.class);
      assertThat(i, is(instanceOf(InterfaceImpl.class)));
      Abstract a = container.get(Abstract.class);
      assertThat(a, is(instanceOf(AbstractImpl.class)));
    }

    @Test
    void methodInjection() {
      Container container = buildContainer();
      container.apply(rc -> rc.setTransformRule(
          MethodInjection.class,
          (c, mi) -> mi.setNoDeps(c.get(ClassWithNoDeps.class))));
      MethodInjection mi = container.get(MethodInjection.class);
      assertThat(mi, is(instanceOf(MethodInjection.class)));
      assertThat(mi.getNoDeps(), is(notNullValue()));
      assertThat(mi.getNoDeps(), is(instanceOf(ClassWithNoDeps.class)));
    }
  }
}
