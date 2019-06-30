# Inject-Java

A reflection based dependency injection framework for Java that doesn't use
annotations.

## Motivation

There are a number of dependency injection frameworks on the market, and
numerous available for Java. From the Spring framework that has dependency
injection as just one of it's many tools, to the standalone frameworks Guice
and Dagger 2, there's already a lot to choose from. So why did I build yet
another dependency injection framework?

Well, honestly part of the reason was simply to learn and improve as a
developer. But there was also the more practical reason in that each of the
aforementioned frameworks require annotating the classes before the framework
will take those classes and compose them into an object graph, and to be honest
I don't much care for that. Annotating each class is in a small way making each
class aware of the dependency injection framework. I like the idea of keeping
all the configuration in the composition root, and keeping the classes unaware
of the dependency injection container.

So I've built Inject-Java, a dependency injection container that doesn't
require that classes be annotated, but rather will strive to intelligently
build your object graph, while allowing you to specify rules to clarify
situations that it is unable to figure out on its own.

## Installation

I haven't as of yet uploaded to Maven Central (it's on my todo list and I'll
come back to it eventually), but it's simple enough to build from it's source.
It's a standard Gradle setup, and the source code has no dependencies on other
libraries. The test code only depends on Junit and Hamcrest.

## Documentation

### Container

The Container class is the main class that you'll work with. It will compose
your object graph following rules that you specify while trying to be
intelligent about what you don't specify, in order to minimize the amount of
configuration code that you need to write.

#### Container.buildContainer()

So to start out, the Container class has a static method called
`buildContainer()`. Use this to get the dependency injection container.

```java
import dev.fuelyour.injectjava.Container;

public class Main {
  public static void main(String[] args) {
    Container container = Container.buildContainer();
  }
}
```
Note that by doing a static import of the buildcontainer method, you can remove
the `Container.` at the beginning of the call, which can allow for a shorter,
cleaner syntax.

```java
import dev.fuelyour.injectjava.Container;
import static dev.fuelyour.injectjava.Container.buildContainer;

public class Main {
  public static void main(String[] args) {
    Container container = buildContainer();
  }
}
```

#### container.get(Class\<T\> clazz)

Next we'll want to take a look at the `get(Class<T> clazz)` method. Calling
this method (or the related `get(String name)` method, which we'll
go over later) is what will generally kick off the building
of your object graph.

It will use Java Reflection to build and return an instance of the class
specified.

```java
public class Root { }

public class Main {
  public static void main(String[] args) {
    Container container = buildContainer();
    Root root = container.get(Root.class);
  }
}
```

In addition, it will build and supply any dependencies in the constructor, so
long as it can determine how to build them.

```java
public class Root {

  private final Dep1 d1;
  private final Dep2 d2;

  public Ex2Root(Dep1 d1, Dep2 d2) {
    this.d1 = d1;
    this.d2 = d2;
  }

  public Dep1 getD1() {
    return d1;
  }

  public Dep2 getD2() {
    return d2;
  }
}

public class Dep1 { }

public class Dep2 { }

public class Main {
  public static void main(String[] args) {
    Container container = buildContainer();
    Root root = container.get(Root.class);
  }
}
```

And it will do the same for any dependencies of the dependencies, and so on.

```java
public class Root {

  private final Dep1 d1;
  private final Dep2 d2;

  public Root(Dep1 d1, Dep2 d2) {
    this.d1 = d1;
    this.d2 = d2;
  }

  public Dep1 getD1() {
    return d1;
  }

  public Dep2 getD2() {
    return d2;
  }
}

public class Dep1 {

  private final Dep3 d3;

  public Dep1(Dep3 d3) {
    this.d3 = d3;
  }

  public Dep3 getD3() {
    return d3;
  }
}

public class Dep2 {

  private final Dep4 d4;

  public Dep2(Dep4 d4) {
    this.d4 = d4;
  }

  public Dep4 getD4() {
    return d4;
  }
}

public class Dep3 { }

public class Dep4 {

  private final Dep5 d5;

  public Dep4(Dep5 d5) {
    this.d5 = d5;
  }

  public Dep5 getD5() {
    return d5;
  }
}

public class Dep5 {
}

public class Main {
  public static void main(String[] args) {
    Container container = buildContainer();
    Root root = container.get(Root.class);
  }
}
```

It will construct each class as a singleton, and will pass the same single
instance into all places that request it.

```java
public class Root {

  private final Dep1 d1;
  private final Dep2 d2;

  public Root(Dep1 d1, Dep2 d2) {
    this.d1 = d1;
    this.d2 = d2;
  }

  public Dep1 getD1() {
    return d1;
  }

  public Dep2 getD2() {
    return d2;
  }
}

public class Dep1 {

  private final Dep2 d2;
  private final Dep3 d3;

  public Dep1(Dep2 d2, Dep3 d3) {
    this.d2 = d2;
    this.d3 = d3;
  }

  public Dep2 getD2() {
    return d2;
  }

  public Dep3 getD3() {
    return d3;
  }
}

public class Dep2 {

  private final Dep3 d3;

  public Dep2(Dep3 d3) {
    this.d3 = d3;
  }

  public Dep3 getD3() {
    return d3;
  }
}

public class Dep3 { }

public class Test {
  
  @Test
  void willCreateASingleInstanceOfEachClass() {
    Container container = buildContainer();
    Root root = container.get(Root.class);
    Dep1 d1 = container.get(Dep1.class);
    Dep2 d2 = container.get(Dep2.class);
    Dep3 d3 = container.get(Dep3.class);

    assertThat(d1, is(root.getD1()));
    assertThat(d2, is(root.getD2()));
    assertThat(d2, is(d1.getD2()));
    assertThat(d3, is(d1.getD3()));
    assertThat(d3, is(d2.getD3()));
  }
}
```

#### container.apply(Module module)

Now there are a number of cases where it can't automatically figure out the
right way to build something. Take, for instance, a class with multiple
constructors. It has no way of knowing which constructor you intend for it to
use, and will throw an exception without guidance.

```java
public class Root {

  private final Dep1 d1;
  private final Dep2 d2;

  public Root(Dep1 d1) {
    this.d1 = d1;
    this.d2 = null;
  }

  public Root(Dep2 d2) {
    this.d1 = null;
    this.d2 = d2;
  }

  public Dep1 getD1() {
    return d1;
  }

  public Dep2 getD2() {
    return d2;
  }
}

public class Dep1 { }

public class Dep2 {

  private final Dep3 d3;

  public Dep2() {
    this.d3 = null;
  }

  public Dep2(Dep3 d3) {
    this.d3 = d3;
  }

  public Dep3 getD3() {
    return d3;
  }
}

public class Dep3 { }

public class Main {
  public static void main(String[] args) {
    Container container = buildContainer();
    container.get(Root.class); //This will throw an exception.
  }
}
```

to fix this, you need to apply a module that has a rule specifying which
constructor to use. (We'll go into more detail on the specifics of rules
later.)

```java
public class Main {
  public static void main(String[] args) {
    Container container = buildContainer();
    container.apply(rc -> rc.setRule(
        Root.class,
        useConstructor(Root.class,
            withParams(Dep1.class))));
    Root root = container.get(Root.class);
  }
}
```

By applying multiple modules, you can add the rules from each module, or
override rules applied in earlier modules with rules applied in later modules.

```java
public class Test {
  @Test
  void laterModulesCanAddRulesAndOverrideRulesFromEarlierModules() {
    Container container = buildContainer();

    container.apply(rc -> rc.setRule(
        Root.class,
        useConstructor(Root.class,
            withParams(Dep1.class))));

    container.apply(rc -> rc
        .setRule(
            Root.class,
            useConstructor(Root.class,
                withParams(Dep2.class)))
        .setRule(
            Dep2.class,
            useConstructor(Dep2.class,
                withParams(Dep3.class))));

    Root root = container.get(Root.class);

    assertThat(root, is(notNullValue()));
    assertThat(root.getD1(), is(nullValue()));
    assertThat(root.getD2(), is(notNullValue()));
    assertThat(root.getD2().getD3(), is(notNullValue()));
  }
}
```

This can be very useful for integration tests where you want to test everything
together, but want to swap out a couple of classes for testing purposes.

```java
public class Root {
  private final Dep1 d1;
  private final Dep2 d2;

  public Root(Dep1 d1) {
    this.d1 = d1;
    this.d2 = null;
  }

  public Root(Dep2 d2) {
    this.d1 = null;
    this.d2 = d2;
  }

  public Dep1 getD1() {
    return d1;
  }

  public Dep2 getD2() {
    return d2;
  }
}

public class Dep1 { }

public class Dep2 { }

public class Main {

  private static Root root;

  public static void main(String[] args) {
    programMain(args, null);
  }

  public static void programMain(String[] args, Module testModule) {
    Container container = buildContainer();
    container.apply(rc -> rc.setRule(Root.class,
        useConstructor(Root.class,
            withParams(Dep1.class))));
    if (testModule != null) {
      container.apply(testModule);
    }
    root = container.get(Root.class);
  }

  public static Root getRoot() {
    return root;
  }
}

public class Test {
  @Test
  void integrationTestProgramMainExample() {
    Main.main(new String[]{});
    Root root = Main.getRoot();
    
    assertThat(root.getD1(), is(notNullValue()));
    assertThat(root.getD2(), is(nullValue()));
    
    Main.programMain(new String[]{},
        rc -> rc.setRule(Root.class,
            useConstructor(Root.class,
                withParams(Dep2.class))));
    root = Main.getRoot();
    
    assertThat(root, is(notNullValue()));
    assertThat(root.getD1(), is(nullValue()));
    assertThat(root.getD2(), is(notNullValue()));
  }
}
```

```java
public class Root {
  private final Dep1 d1;
  private final Dep2 d2;

  public Root(Dep1 d1) {
    this.d1 = d1;
    this.d2 = null;
  }

  public Root(Dep2 d2) {
    this.d1 = null;
    this.d2 = d2;
  }

  public Dep1 getD1() {
    return d1;
  }

  public Dep2 getD2() {
    return d2;
  }
}

public class Dep1 { }

public class Dep2 { }

public class Main {

  private static Root root;

  public static Root getRoot() {
    return root;
  }

  public static void main(String[] args) {
    Container container = buildContainer();
    container.apply(rc -> rc.setRule(Root.class,
        useConstructor(Root.class,
            withParams(Dep1.class))));
    if (args.length > 0) {
      String moduleClassName = args[0];
      applyTestModule(container, moduleClassName);
    }
    root = container.get(Root.class);
  }

  @SuppressWarnings("unchecked")
  private static void applyTestModule(
      Container container,
      String moduleClassName) {
    try {
      Class<? extends Module> clazz =
          (Class<? extends Module>) Class.forName(moduleClassName);
      Constructor<? extends Module> ctor = clazz.getConstructor();
      Module testModule = ctor.newInstance();
      container.apply(testModule);
    } catch (Exception ignored) { }
  }
}

public class TestModule implements Module {

  @Override
  public void configure(RuleConfig rc) {
    rc.setRule(Root.class,
              useConstructor(Root.class,
                  withParams(Dep2.class)));
  }
}

public class Test {
  @Test
  void integrationTestArgExample() {
    Main.main(new String[]{});
    Root root = Main.getRoot();
    assertThat(root, is(notNullValue()));
    assertThat(root.getD1(), is(notNullValue()));
    assertThat(root.getD2(), is(nullValue()));

    Main.main(new String[]{TestModule.class.getName()});
    root = Main.getRoot();
    assertThat(root.getD1(), is(nullValue()));
    assertThat(root.getD2(), is(notNullValue()));
  }
}
```

#### container.get(String name)

There are times when you will need to inject instances of the same class with
different values. To allow for these situations, the instances can be
identified by a String name value instead of the class. The container has a
`get(String name)` method to allow you to access these directly.

```java
public class Test {
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
}
```

Note that a named value won't be returned when the class is specified, but you
can set a rule to associate a named value with the class, or vice versa,
essentially allowing you to pick a default for when a name isn't present.

```java
public class Test {
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
}
```

You can even associate two names to the same instance. Be careful in your use
of this. While I can imagine use cases, I also see it as a way of shooting
yourself in the foot if it gets out of control.

```java
public class Test {
  @Test
  void twoNamesCanBeAssociatedToTheSameInstance() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule("first", useInstance("One instance"))
        .setRule("second", get("first")));

    assertThat(container.get("first"), is("One instance"));
    assertThat(container.get("second"), is("One instance"));
  }
}
```

To inject named values into classes that the container builds, you will need to
associate the names with the corresponding constructor parameters.

```java
public class Root {

  private final Dep1 d1;
  private final Dep2 d2;

  public Root(Dep1 d1, Dep2 d2) {
    this.d1 = d1;
    this.d2 = d2;
  }

  public Dep1 getD1() {
    return d1;
  }

  public Dep2 getD2() {
    return d2;
  }
}

public class Dep1 {

  private final String value;

  public Dep1(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

public class Dep2 {

  private final String value;

  public Dep2(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

public class Test {
  @Test
  void associateNameWithTheCorrespondingConstructorParameters() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule("first", useInstance("one"))
        .setRule("second", useInstance("two"))
        .setRule(Dep1.class,
            useSoleConstructor(Dep1.class,
                withParamNames(paramName("first", String.class))))
        .setRule(Dep2.class,
            useSoleConstructor(Dep2.class,
                withParamNames(paramName("second", String.class)))));
    Root root = container.get(Root.class);

    assertThat(root.getD1().getValue(), is("one"));
    assertThat(root.getD2().getValue(), is("two"));
  }
}
```

The parameter name is associated with the parameter class in the constructor.
If the constructor has multiple parameters of the same class, then it will
assume the first occurrence, unless otherwise specified by you.

```java
public class Root {

  private final Dep firstDep;
  private final Dep secondDep;

  public Root(Dep firstDep, Dep secondDep) {
    this.firstDep = firstDep;
    this.secondDep = secondDep;
  }

  public Dep getFirstDep() {
    return firstDep;
  }

  public Dep getSecondDep() {
    return secondDep;
  }
}

public class Dep {

  private final String value;

  public Dep(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

public class Test {
  @Test
  void specifyParamNameOccurrenceWhenMultipleParamsWithSameClass() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule("first", useInstance("one"))
        .setRule("second", useInstance("two"))
        .setRule("firstDep",
            useSoleConstructor(Dep.class,
                withParamNames(paramName("first", String.class))))
        .setRule("secondDep",
            useSoleConstructor(Dep.class,
                withParamNames(paramName("second", String.class))))
        .setRule(Root.class,
            useSoleConstructor(Root.class,
                withParamNames(
                    paramName("firstDep", Dep.class),
                    paramName("secondDep", Dep.class, 2)))));

    Root root = container.get(Root.class);
    Dep firstDep = root.getFirstDep();
    Dep secondDep = root.getSecondDep();

    assertThat(firstDep, is(not(secondDep)));
    assertThat(firstDep.getValue(), is("one"));
    assertThat(secondDep.getValue(), is("two"));
  }
}
```

### RuleConfig

#### ruleConfig.setRule(...)

There are four variants of the `setRule` method, but they essentially do the
same thing. Each will take two parameters: a key that will tell the container
what this rule will be applied to, and a lambda that is the rule to apply. The
variants essentially allow you to choose whether the rule is keyed to a class 
or to a name, and choose whether or not you need access to the SimpleContainer 
supplied to your lambda.

The SimpleContainer supplies various access methods for getting other objects
stored in the container, specifying parameter names, and specifying what class
or constructor to use.

```java
public class Root {

  private final String value;

  public Root() {
    this.value = null;
  }

  public Root(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

public class Test {
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
        Root.class,
        c -> c.buildWithConstructor(Root.class, new Class<?>[]{})));
    Root root = container.get(Root.class);

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
        c -> c.buildWithConstructor(Root.class, new Class<?>[]{})));
    Root root = container.get("root");

    assertThat(root.getValue(), is(nullValue()));
  }
}
```

Commonly used rules have helper methods built out for them in the Rules class.

```java
public class Root {

  private final Dep dependency;

  public Root() {
    this.dependency = null;
  }

  public Root(Dep dependency) {
    this.dependency = dependency;
  }

  public Dep getDependency() {
    return dependency;
  }
}

public class Dep {

  private final String firstName;
  private final String lastName;

  public Dep(String firstName, String lastName) {
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

public class Test {
  @Test
  void ruleHelpers() {
    Container container = buildContainer();
    container.apply(rc -> rc
        .setRule(String.class, useInstance("Bob"))
        .setRule("firstName", get(String.class))
        .setRule("lastName", useInstance("Smith"))
        .setRule(Dep.class,
            useSoleConstructor(Dep.class,
                withParamNames(
                    paramName("firstName", String.class),
                    paramName("lastName", String.class, 2))))
        .setRule(Root.class,
            useConstructor(Root.class,
                withParams(Dep.class))));
    Root root = container.get(Root.class);

    assertThat(root.getDependency(), is(notNullValue()));
    assertThat(root.getDependency().getFirstName(), is("Bob"));
    assertThat(root.getDependency().getLastName(), is("Smith"));
  }
}
```

#### ruleConfig.setGeneralRule(...)

There are many times where the rules that you'd need to implement are very
similar to each other, and could be better handled with some general purpose
rule, and that's the exact point of the GeneralRule. You essentially write
two lambda expressions, one that serves as the condition that determines
whether or not the rule should be applied, and the other is the rule itself.
I've provided pre built rules for a couple of common scenarios.

The first scenario is the situation where a class has multiple constructors,
one of which is a default constructor with no parameters. This rule will
default to using the default constructor when no more specific rule is given.

```java
public class Root {

  private final String value;

  public Root() {
    value = null;
  }

  public Root(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
}

public class Test {
  @Test
  void defaultConstructorGeneralRule() {
    Container container = buildContainer();
    container.apply(rc -> rc.setGeneralRule(useDefaultConstructor()));
    Root root = container.get(Root.class);
    
    assertThat(multi.getClassWithNoDeps(), is(nullValue()));
  }
}
```

Another scenario is for if you choose to follow the design pattern where all
classes have interfaces. This frequently leads to a one to one mapping and
a naming convention of the class having the same name as the interface, but
with Impl on the end. This general rule will match up all such classes with
their interfaces via the naming convention.

```java
public interface Interface {
  Dep getDep();
}

public class InterfaceImpl implements Interface {
  
  private final Dep dep;
  
  public InterfaceImpl(Dep dep) {
    this.dep = dep;
  }
  
  @Override
  public Dep getDep() {
    return dep;
  }
}

public class Dep { }

public class Test {
  @Test
  void generalRuleTest() {
    Container container = buildContainer();
    container.apply(rc -> rc.setGeneralRule(useInterfaceImplRule()));
    Interface i = container.get(Interface.class);
    
    assertThat(i, is(instanceOf(InterfaceImpl.class)));
  }
}
```

You can have multiple general rules, and they will be evaluated in the order
that they are added to the container. The first one that matches the condition
will be used to construct the object. Note that rules are checked before
general rules, so as such a rule will override a general rule. Also, if no
general rule is present with a matching condition, it will then move on to
the general purpose constructor.

Also note that a rule name can optionally be supplied for a general rule. The
purpose of this rule name is to give a way to override the general rule in a
followup module. If a general rule with the same name as a previous general
rule is applied, it will overwrite the previous general rule. Also note that
it will take the same spot in the evaluation line that the previous general
rule held. So this is the only way that a general rule added later can be
placed in front of general rules that were added before it.

#### setTransformRule(...) and setGeneralTransformRule(...)

In addition to constructor injection, most other dependency injection
frameworks also support method injection and field injection, and while most
experts on the subject recommend sticking with constructor injection, there are
plenty of legitimate cases for these other types of injection (or at least
field injection, more on that in a moment). So how does inject-java handle
this? It does so through transform rules. Transform rules are rules that are
applied to the object after it has been constructed and registered with the
container. This is done be the object instance being supplied to the lambda
you write for the rule.

Now like the rule and the general rule discussed prior, the transform rule
allows you to apply a rule specified by class or name, and the general
transform rule allows you to specify a condition under which the rule is
applied. But unlike with the rule and general rule, the transform rule
actually just a general transform rule where the condition is built out
for you. As such, both the transform rule and the general transform rule
are more like the general rule, in that you can have as many of them as you
want, and you can specify a rule name for later overriding. But they differ
from the general rule in that all rules will be applied that have matching
conditions, and not just the first matching condition.

It's also worth noting that the transform rules and general transform rules
live in the same collection, so a transform rule can be overridden by a
general transform rule, and vice versa.

An example of how this can be used is for dealing with circular references
(note that in general you want to avoid having circular references, but if you
need to have a circular reference, this is how you deal with it). If class A
needs a reference to class B and class B needs a reference to class A, then
pure constructor injection is out of the question. You'll instead need to have
at least one of the classes utilize method injection. Something kind of like
this:

```java
public class Root {
  
  private final Dep dep;
  
  public Root(Dep dep) {
    this.dep = dep;
  }
}

public class Dep {
  
  private Root root;
  
  public Dep() { }
  
  public void setRoot(Root root) {
    this.root = root;
  }
}

public class Main {
  public static void main(String[] args) {
    Container container = buildContainer();
    container.apply(rc -> rc.setTransformRule(
        Dep.class,
        (c, dep) -> dep.setRoot(c.get(Root.class))));
    Root root = container.get(Root.class);
  }
}
```

Now I don't have specific example code for the general transform rule at the
moment, but a powerful use case would be to register methods in a class as
handlers of events based off of annotations on the method or class, or based
off the name of the class or method. Ultimately, there's a lot of potential
power in general rules and general transform rules.

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2019 © Trevor Young.
