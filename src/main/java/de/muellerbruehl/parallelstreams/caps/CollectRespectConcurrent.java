/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams.caps;

import de.muellerbruehl.parallelstreams.Person;
import de.muellerbruehl.parallelstreams.Persons;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 *
 * @author Guilherme
 */
public class CollectRespectConcurrent {

    public static void main(String[] arg) {
        Persons personsFac = Persons.getInstance();
        List<Person> persons = personsFac.getPersons();

        Map<Integer, List<Person>> ageMap = persons.stream()
                .collect(Collectors.groupingBy(p -> p.getAge()));

        System.out.println(ageMap.size());

        ConcurrentMap<Integer, List<Person>> ageMapPar = persons.parallelStream()
                .collect(Collectors.groupingByConcurrent(Person::getAge));

        System.out.println(ageMapPar.size());

    }

    public static class SummingAges {

        private int _sum;

        public SummingAges() {
            System.out.println("new SummingAges()");
        }

        public int getSum() {
            return _sum;
        }

        public void sum(int value) {
            _sum += value;
        }

        public void combine(SummingAges other) {
            _sum += other._sum;
        }
    }

}
