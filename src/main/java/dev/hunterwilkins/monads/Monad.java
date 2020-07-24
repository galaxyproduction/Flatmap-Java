package dev.hunterwilkins.monads;

import java.util.function.Function;

public interface Monad<A> extends Functor<A>, Applicative<A> {
    <B> Monad<B> flatmap(Function<A, Monad<B>> f);
}