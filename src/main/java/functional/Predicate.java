package functional;

public interface Predicate<A> extends Function1<A, Boolean> {
    static final Predicate<Object> ALWAYS_TRUE = (arg -> true);
    static final Predicate<Object> ALWAYS_FALSE = (arg -> false);

    default Predicate<A> or(Predicate<? super A> other) {
        return arg -> apply(arg) || other.apply(arg);
    }

    default Predicate<A> and(Predicate<? super A> other) {
        return arg -> apply(arg) && other.apply(arg);
    }

    default Predicate<A> not() {
        return arg -> !apply(arg);
    }

}
