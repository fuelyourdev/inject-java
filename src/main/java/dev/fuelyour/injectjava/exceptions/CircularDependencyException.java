package dev.fuelyour.injectjava.exceptions;

import dev.fuelyour.injectjava.Id;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class CircularDependencyException extends InjectJavaException {

  public CircularDependencyException(Id id, List<Id> hist) {
    super("Circular dependency detected.\n" +
        hist.subList(hist.indexOf(id), hist.size()).stream()
            .map(Id::toString)
            .collect(joining(" -> ")) +
        " -> " + id.toString());
  }
}
