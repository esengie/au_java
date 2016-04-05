package functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collections {
    public static <T, R> List<R> map(Function1<? super T, R> f, Iterable<T> collection) {
        List<R> res = new ArrayList<>();
        for (T elem : collection) {
            res.add(f.apply(elem));
        }
        return res;
    }

    public static <T> List<T> filter(Predicate<? super T> p, Iterable<T> collection) {
        List<T> res = new ArrayList<>();
        for (T elem : collection) {
            if (p.apply(elem)) {
                res.add(elem);
            }
        }
        return res;
    }

    public static <T> List<T> takeWhile(Predicate<? super T> p, Iterable<T> collection) {
        List<T> res = new ArrayList<>();
        for (T elem : collection) {
            if (!p.apply(elem)) {
                break;
            }
            res.add(elem);
        }
        return res;
    }

    public static <T> List<T> takeUnless(Predicate<? super T> p, Iterable<T> collection) {
        return takeWhile(p.not(), collection);
    }

    public static <T, R> R foldl(Function2<? super R, ? super T, R> f, R initA, Iterable<T> collection) {
        R res = initA;
        for (T elem : collection) {
            res = f.apply(res, elem);
        }
        return res;
    }

    public static <T, R> R foldr(Function2<? super T, ? super R, R> f, R initA, Iterable<T> collection) {
        return foldrHelper(f, initA, collection.iterator());
    }

    private static <T, R> R foldrHelper(Function2<? super T, ? super R, R> f, R initA, Iterator<T> iterator) {
        if (!iterator.hasNext()) {
            return initA;
        }
        T valLeft = iterator.next();
        return f.apply(valLeft, foldrHelper(f, initA, iterator));
    }
}
