package dev.hunterwilkins.monads;

import java.util.function.BiFunction;
import java.util.function.Function;

public class State<S, A> {
    private final Function<S, Pair<A, S>> state;

    private State(Function<S, Pair<A, S>> state) { this.state = state; }

    public static <S, A> State<S, A> of(Function<S, Pair<A, S>> state) {
        return new State<>(state);
    }

    public static <S, A> State<S, A> pure(A a) {
        return new State<>(s -> new Pair<>(a, s));
    }

    public Pair<A, S> runstate(S initalState) {
        return state.apply(initalState); 
    }

    public <B> State<S, B> map(Function<A, B> f) {
        return State.of(s -> {
            Pair<A, S> s1 = runstate(s);
            return new Pair<B, S>(f.apply(s1.Item1()), s1.Item2());
        });
    }

    public <B, C> State<S, C> liftA2(State<S, B> b, BiFunction<A, B, State<S, C>> biFunction) {
        return flatmap(aVal -> b.flatmap(bVal -> biFunction.apply(aVal, bVal)));
    }

    public <B> State<S, B> flatmap(Function<A, State<S, B>> f) {
        return State.of(s -> {
            Pair<A, S> s1 = runstate(s);
            State<S, B> s2 = f.apply(s1.Item1());
            return s2.runstate(s1.Item2());
        });
    }

    @Override
    public String toString() {
        return "State()";
    }
}