/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams.caps;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * @author Guilherme
 */
public class CollectorAndConcurrency {

    public static void main(String[] arg) {
        List<Long> numbers = createNumbers();
        System.out.println("total: "
                + numbers.parallelStream()
                .collect(() -> new SummingUnit(),
                        (summingUnit, value) -> summingUnit.sum(value),
                        (summingUnit, other) -> summingUnit.combine(other))
                .getSum());
        //Alternatively we can use method references.
        numbers.parallelStream()
                .collect(SummingUnit::new,
                        SummingUnit::sum,
                        SummingUnit::combine);
        
        System.out.println("totla SummingCollector: "
        + numbers.parallelStream()
                .collect(new SummingCollector()));

    }

    public static List<Long> createNumbers() {
        List<Long> numbers = new ArrayList<>();
        Random random = new Random();
        int max = 500000 + random.nextInt(500000);
        for (int i = 0; i < max; i++) {
            numbers.add((long) random.nextInt(100));
        }
        return numbers;
    }

    public static class SummingUnit {

        private long _sum;

        public SummingUnit() {
            System.out.println("new SummingUnit()");
        }

        public long getSum() {
            return _sum;
        }

        public void sum(long value) {
            _sum += value;
        }

        public void combine(SummingUnit other) {
            _sum += other._sum;
        }
    }

    /**
     * We parameterize the interface Collector with three types <T, A, R> as shown in the signature.
     * Here T is Long (a number), A is replaced by a SummingUnit, and the result type R in the end
     * is a Long too: the final sum.
     */
    public static class SummingCollector implements Collector<Long, SummingUnit, Long> {

        @Override
        public Supplier<SummingUnit> supplier() {
            return SummingUnit::new;
        }

        @Override
        public BiConsumer<SummingUnit, Long> accumulator() {
            //return SummingUnit::sum; //OR
            return (supplier, value) -> supplier.sum(value);
        }

        @Override
        public BinaryOperator<SummingUnit> combiner() {
            return (left, right) -> {
                left.combine(right);
                return left;
            };
        }

        @Override
        public Function<SummingUnit, Long> finisher() {
            return s -> s.getSum();
        }

        /**
         * Because we did not return the characteristic CONCURRENT, the supplier function is called
         * for every thread.You may watch this by observing the output during object construction.
         * If we provide the characteristic CONCURRENT,Java assumes the supplier to be enabled for
         * multithreading. It would create only one SummingUnit, which is shared by all the
         * threads.And guess, the result would differ from run to run because we have a critical
         * section with a race condition.
         *
         * @return
         */
        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.UNORDERED);
        }

    }
}
