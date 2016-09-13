package functional;

public interface Function1<A, R> {
    R apply(A arg);

    default <R2> Function1<A, R2> compose(Function1<? super R, R2> g) {
        return arg -> g.apply(apply(arg));
    }
}
