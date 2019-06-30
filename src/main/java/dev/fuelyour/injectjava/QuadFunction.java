package dev.fuelyour.injectjava;

@FunctionalInterface
interface QuadFunction<A,B,C,D,R> {

  R apply(A a, B b, C c, D d);

}
