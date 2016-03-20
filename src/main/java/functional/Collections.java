package functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collections {
    public static <T, R> Iterable<R> map(Function1<T, R> f, Iterable<T> collection) {
        List<R> res = new ArrayList<>();
        for (T elem : collection) {
            res.add(f.apply(elem));
        }
        return res;
    }

    public static <T> Iterable<T> filter(Predicate<T> p, Iterable<T> collection) {
        List<T> res = new ArrayList<>();
        for (T elem : collection) {
            if (p.apply(elem)) {
                res.add(elem);
            }
        }
        return res;
    }

    public static <T> Iterable<T> takeWhile(Predicate<T> p, Iterable<T> collection) {
        List<T> res = new ArrayList<>();
        for (T elem : collection) {
            if (p.not().apply(elem)) {
                break;
            }
            res.add(elem);
        }
        return res;
    }

    public static <T> Iterable<T> takeUnless(Predicate<T> p, Iterable<T> collection) {
        List<T> res = new ArrayList<>();
        for (T elem : collection) {
            if (p.apply(elem)) {
                break;
            }
            res.add(elem);
        }
        return res;
    }

    public static <T, R> R foldl(Function2<R, T, R> f, R a0, Iterable<T> collection) {
        R res = a0;
        for (T elem : collection) {
            res = f.apply(res, elem);
        }
        return res;
    }

    public static <T, R> R foldr(Function2<T, R, R> f, R a0, Iterable<T> collection) {
        return foldrHelper(f, a0, collection.iterator());
    }

    private static <T, R> R foldrHelper(Function2<T, R, R> f, R a0, Iterator<T> iterator) {
        if (!iterator.hasNext()) {
            return a0;
        }
        T valLeft = iterator.next();
        return f.apply(valLeft, foldrHelper(f, a0, iterator));
    }
}
