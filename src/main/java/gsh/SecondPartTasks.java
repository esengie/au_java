package gsh;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private static final int ATTEMPT_COUNT = 1000000;
    private static final double TARGET_RANGE = 0.5;

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream()
                .flatMap(s -> {
                    try {
                        return Files.lines(Paths.get(s)).filter(l -> !l.isEmpty());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .filter(s -> s.contains(sequence))
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random rand = new Random();
        Predicate<Pair<Double, Double>> isSuccessShot = p -> {
            double x = p.getFirst();
            double y = p.getSecond();
            return x * x + y * y <= TARGET_RANGE * TARGET_RANGE;
        };

        long success = Stream
                .generate(() -> new Pair<>(rand.nextDouble() - TARGET_RANGE, rand.nextDouble() - TARGET_RANGE))
                .limit(ATTEMPT_COUNT)
                .filter(isSuccessShot)
                .count();

        return (double) success / ATTEMPT_COUNT;
    }


    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream()
                .map(entry ->
                        new Pair<>(entry.getKey(), entry.getValue()
                                .stream()
                                .mapToInt(String::length)
                                .sum()))
                .max(Comparator.comparing(Pair::getSecond))
                .map(Pair::getFirst)
                .orElse(null);
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream()
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum));
    }

    private static class Pair<T, U> {
        public Pair(T fst, U snd) {
            first = fst;
            second = snd;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }

        private T first;
        private U second;
    }
}
