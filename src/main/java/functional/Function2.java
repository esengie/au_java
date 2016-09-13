package functional;

public interface Function2<A1, A2, R> {
    R apply(A1 arg1, A2 arg2);

    default <R2> Function2<A1, A2, R2> compose(Function1<? super R, R2> g) {
        return (arg1, arg2) -> g.apply(apply(arg1, arg2));
    }

    default Function1<A2, R> bind1(A1 arg1) {
        return arg2 -> apply(arg1, arg2);
    }

    default Function1<A1, R> bind2(A2 arg2) {
        return arg1 -> apply(arg1, arg2);
    }

    default Function1<A1, Function1<A2, R>> curry() {
        return arg1 -> arg2 -> apply(arg1, arg2);
    }
}
