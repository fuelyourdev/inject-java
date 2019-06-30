package dev.fuelyour.injectjava;

@FunctionalInterface
interface QuadConsumer<A,B,C,D> {

  void accept(A a, B b, C c, D d);

}
