/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams.caps;

import de.muellerbruehl.parallelstreams.AverageBuilder;
import de.muellerbruehl.parallelstreams.Person;
import de.muellerbruehl.parallelstreams.Persons;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class GroupAverageCollector implements Collector<Person, Map<Integer, AverageBuilder>, Map<Integer, Double>> {

    public static void main(String[] args) {
        Persons personsFac = Persons.getInstance();
        List<Person> persons = personsFac.getPersons();
        Map<Integer, Double> result = persons.parallelStream().collect(new GroupAverageCollector());
        System.out.println(result);
    }
    
    
    @Override
    public Supplier<Map<Integer, AverageBuilder>> supplier() {
        return () -> new HashMap<>();
    }

    @Override
    public BiConsumer<Map<Integer, AverageBuilder>, Person> accumulator() {
        return (m, p) -> add(m, p);
    }

    @Override
    public BinaryOperator<Map<Integer, AverageBuilder>> combiner() {
        return (left, right) -> { 
                combine(left, right);
                return left;
        };
    }

    @Override
    public Function<Map<Integer, AverageBuilder>, Map<Integer, Double>> finisher() {
        return m -> finish(m);
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
    
    
    private static void add(Map<Integer, AverageBuilder> map, Person person) {
          int group = person.getAge() / 10;
          if (!map.containsKey(group)) {
              map.put(group, new AverageBuilder());
          }
          long cents = person.getBuying().values().stream().mapToLong(a -> a.getAmount().getCents()).sum();
          map.get(group).add(cents);
    }
    
    private static void combine(Map<Integer, AverageBuilder> left, 
                                Map<Integer, AverageBuilder> right) {
        for (int group : right.keySet()) {
            if (!left.containsKey(group)) {
                left.put(group, right.get(group));
            } else {
                left.get(group).add(right.get(group));
            }
        }
    }
    
    private static Map<Integer, Double> finish(Map<Integer, AverageBuilder> map) {
        Map<Integer, Double> result = new HashMap<>();
        for (int group : map.keySet()) {
            result.put(group, map.get(group).getAverage());
        }
        return result;
    }
}
