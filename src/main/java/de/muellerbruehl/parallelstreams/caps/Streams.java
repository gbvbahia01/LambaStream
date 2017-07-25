/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams.caps;

import de.muellerbruehl.parallelstreams.ArticleInfo;
import de.muellerbruehl.parallelstreams.Person;
import de.muellerbruehl.parallelstreams.Persons;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.time.StopWatch;

/**
 *
 * @author Guilherme
 */
public class Streams {

    public static void main(String[] arg) {
        Persons personsFac = Persons.getInstance();
        List<Person> persons = personsFac.getPersons();
        StopWatch watch = new StopWatch();
        watch.start();

        stream(persons);
        watch.split();
        System.out.println("stream: " + watch.toSplitString());

        parallelStream(persons);
        watch.split();
        System.out.println("parallelStream: " + watch.toSplitString());

        Supplier<Long> totalSellingStream = () -> persons.stream()
                .filter(p -> p.isVendor())
                .flatMapToLong(p -> p.getSelling().values().stream()
                        .mapToLong(ArticleInfo::getQuantity)).sum();
        System.out.println("total totalSellingStream:" + totalSellingStream.get());

        Supplier<Long> totalSellingParallelStream = () -> persons.parallelStream()
                .filter(p -> p.isVendor())
                .flatMapToLong(p -> p.getSelling().values().parallelStream()
                        .mapToLong(ArticleInfo::getQuantity)).sum();
        System.out.println("total totalSellingParallelStream:" + totalSellingParallelStream.get());
    }

    public static <T> T invokeMethod(Supplier<T> method) {
        System.out.println("invokeMethod");
        long start = System.nanoTime();
        T result = method.get();
        long elapsedTime = System.nanoTime() - start;
        System.out.println("Elapsed time: " + elapsedTime / 1000000);
        return result;
    }

    public static void parallelStream(List<Person> persons) {
        List<Person> youngFemales = persons.parallelStream()
                .filter(p -> p.getAge() < 20)
                .filter(p -> p.isFemale())
                .collect(Collectors.toList());

        double averageAge = persons.parallelStream()
                .filter(p -> p.getAge() < 20 && p.isFemale())
                .mapToInt((Person p) -> p.getAge()).average().getAsDouble();

        long totalSelling = persons.parallelStream()
                .filter(Person::isVendor)
                .mapToLong(p -> p.getSelling().values().parallelStream()
                        .mapToLong(ArticleInfo::getQuantity).sum()).sum();

        totalSelling = persons.parallelStream()
                .filter(Person::isVendor)
                .flatMapToLong(p -> p.getSelling().values().parallelStream()
                        .mapToLong(ArticleInfo::getQuantity)).sum();

        int namesWithLetterA = persons.parallelStream()
                .filter(p -> p.getGivenName().contains("A"))
                .collect(Collectors.toList()).size();

    }

    public static void stream(List<Person> persons) {
        List<Person> youngFemales = persons.stream()
                .filter(p -> p.getAge() < 20)
                .filter(p -> p.isFemale())
                .collect(Collectors.toList());

        double averageAge = persons.stream()
                .filter(p -> p.getAge() < 20 && p.isFemale())
                .mapToInt((Person p) -> p.getAge()).average().getAsDouble();

        long totalSelling = persons.stream()
                .filter(Person::isVendor)
                .mapToLong(p -> p.getSelling().values().stream()
                        .mapToLong(ArticleInfo::getQuantity).sum()).sum();

        totalSelling = persons.stream()
                .filter(Person::isVendor)
                .flatMapToLong(p -> p.getSelling().values().stream()
                        .mapToLong(ArticleInfo::getQuantity)).sum();

        int namesWithLetterA = persons.stream()
                .filter(p -> p.getGivenName().contains("A"))
                .collect(Collectors.toList()).size();

    }

    public static double dynamicBuildOfProcessChain(boolean flow, Collection<Person> persons) {
        Stream<Person> stream = persons.stream();
        if (flow) {
            stream = stream.filter(p -> p.getAge() < 20);
        } else {
            stream = stream.filter(Person::isFemale);
        }
        return stream.mapToDouble(p -> p.getAge()).average().getAsDouble();
    }
}
