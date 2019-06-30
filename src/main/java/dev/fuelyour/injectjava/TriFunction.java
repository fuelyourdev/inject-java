package dev.fuelyour.injectjava;

@FunctionalInterface
interface TriFunction<A,B,C,R> {

  R apply(A a, B b, C c);

}
