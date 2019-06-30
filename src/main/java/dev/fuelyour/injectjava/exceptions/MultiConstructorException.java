package dev.fuelyour.injectjava.exceptions;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class MultiConstructorException extends InjectJavaException {

  public MultiConstructorException(Class<?> klass) {
    super(klass.getSimpleName() + " has multiple constructors:\n  " +
        stream(klass.getConstructors())
            .map(con -> klass.getSimpleName() + "(" +
                stream(con.getParameters())
                    .map(param -> param.getType().getSimpleName() + " " +
                        param.getName())
                    .collect(joining(", ")) +
                ")")
            .collect(joining("\n  ")) + "\n" +
        "Fuel Injector does not know which one to use.");
  }
}
