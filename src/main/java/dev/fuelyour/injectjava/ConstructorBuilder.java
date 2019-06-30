package dev.fuelyour.injectjava;

import dev.fuelyour.injectjava.exceptions.FailedToInstantiateException;
import dev.fuelyour.injectjava.exceptions.MultiConstructorException;
import dev.fuelyour.injectjava.exceptions.NoRuleException;
import dev.fuelyour.injectjava.exceptions.NoSuchConstructorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;

class ConstructorBuilder {

  private final IdFactory idFactory;

  ConstructorBuilder(IdFactory idFactory) {
    this.idFactory = idFactory;
  }

  @SuppressWarnings("unchecked")
  <T> T build(SimpleContainer container, Id id) {
    return build(container, (Class<T>) id.getClassId(), new ParameterName[0]);
  }

  <T> T build(
      SimpleContainer container,
      Class<T> clazz,
      ParameterName[] paramNames) {
    verifyClassCanBeInstantiatedByConstructor(clazz);
    Constructor<T> constructor = chooseConstructor(clazz);
    Object[] paramObjs = instantiateParameters(
        constructor,
        container,
        paramNames);
    return instantiateClass(constructor, paramObjs);
  }

  <T> T buildWithConstructor(
      SimpleContainer container,
      Class<T> clazz,
      Class<?>[] params,
      ParameterName[] paramNames) {
    try {
      Constructor<T> ctor = clazz.getConstructor(params);
      Parameter[] ctorParams = ctor.getParameters();
      Object[] paramObjs =
          stream(injectIdsIntoParameterList(ctorParams, paramNames))
          .map(container::get)
          .toArray();
      return ctor.newInstance(paramObjs);
    } catch (NoSuchMethodException e) {
      throw new NoSuchConstructorException(clazz, params, e);
    } catch (Exception e) {
      throw new FailedToInstantiateException(e);
    }
  }

  private void verifyClassCanBeInstantiatedByConstructor(Class<?> clazz) {
    ifInterfaceThrowException(clazz);
    ifAbstractClassThrowException(clazz);
  }

  private void ifInterfaceThrowException(Class<?> clazz) {
    if (clazz.isInterface()) {
      throw new NoRuleException(clazz.getSimpleName() + " is an " +
          "interface. Please specify an implementing class with a Rule.");
    }
  }

  private void ifAbstractClassThrowException(Class<?> clazz) {
    if (isAbstract(clazz.getModifiers())) {
      throw new NoRuleException(
          clazz.getSimpleName() + " is abstract. " +
              "Please specify a rule for how to build.");
    }
  }

  @SuppressWarnings("unchecked")
  private <T> Constructor<T> chooseConstructor(Class<T> clazz) {
    Constructor<?>[] ctors = clazz.getConstructors();
    Constructor<T> ctor;
    if (ctors.length > 1) {
      throw new MultiConstructorException(clazz);
    } else if (ctors.length == 1) {
      ctor = (Constructor<T>) ctors[0];
    } else {
      throw new NoRuleException(clazz.getSimpleName() + " has no available " +
          "constructors. Please use a rule to specify how to build.");
    }
    return ctor;
  }

  private <T> Object[] instantiateParameters(
      Constructor<T> constructor,
      SimpleContainer container,
      ParameterName[] paramNames) {
    Parameter[] params = constructor.getParameters();
    Id[] paramKeys = injectIdsIntoParameterList(params, paramNames);
    Object[] paramObjs = new Object[paramKeys.length];
    for (int i = 0; i < paramKeys.length; i++) {
      Id param = paramKeys[i];
      paramObjs[i] = container.get(param);
    }
    return paramObjs;
  }

  private Id[] injectIdsIntoParameterList(
      Parameter[] params,
      ParameterName[] paramNames) {
    Map<Class<?>, Integer> occurrenceIndex = new HashMap<>();
    Id[] result = new Id[params.length];
    for (int i = 0; i < params.length; i++) {
      Parameter param = params[i];
      Class<?> paramType = param.getType();
      if (occurrenceIndex.containsKey(paramType)) {
        occurrenceIndex.put(paramType, occurrenceIndex.get(paramType) + 1);
      } else {
        occurrenceIndex.put(paramType, 1);
      }
      int occurrence = occurrenceIndex.get(paramType);
      result[i] = resolveParameter(paramType, occurrence, paramNames);
    }
    return result;
  }

  private Id resolveParameter(
      Class<?> paramType,
      int occurrence,
      ParameterName[] paramNames) {
    return stream(paramNames)
          .filter(paramName -> paramType == paramName.getType() &&
                  occurrence == paramName.getOccurrence())
          .reduce((first, second) -> second)
          .map(paramName -> idFactory.buildId(paramType, paramName.getName()))
          .orElse(idFactory.buildId(paramType));
  }

  private <T> T instantiateClass(
      Constructor<T> constructor,
      Object[] paramObjs) {
    try {
      return constructor.newInstance(paramObjs);
    } catch (Exception e) {
      throw new FailedToInstantiateException(e);
    }
  }
}
