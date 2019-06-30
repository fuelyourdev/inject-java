package dev.fuelyour.injectjava;

import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SimpleContainer {

  private final Container container;
  private final List<Id> hist;

  SimpleContainer(
      Container container,
      List<Id> hist) {
    this.container = container;
    this.hist = hist;
  }

  public <T> T get(Class<T> clazz) {
    return container.get(clazz, hist);
  }

  public <T> T get(String name) {
    return container.get(name, hist);
  }

  public boolean hasRule(Class<?> clazz) {
    return container.hasRule(clazz);
  }

  public boolean hasRule(String name) {
    return container.hasRule(name);
  }

  public <T> T build(Class<T> clazz) {
    return build(clazz, new ParameterName[]{});
  }

  public <T> T build(Class<T> clazz, ParameterName[] paramNames) {
    return container.build(clazz, hist, paramNames);
  }

  public <T> T buildWithConstructor(
      Class<T> clazz,
      Class<?>[] params) {
    return buildWithConstructor(clazz, params, new ParameterName[]{});
  }

  public <T> T buildWithConstructor(
      Class<T> clazz,
      Class<?>[] params,
      ParameterName[] paramNames) {
    return container.buildWithConstructor(clazz, hist, params, paramNames);
  }

  <T> T get(Id id) {
    return container.get(id, hist);
  }
}
