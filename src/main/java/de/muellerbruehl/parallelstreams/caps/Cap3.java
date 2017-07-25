/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams.caps;

import de.muellerbruehl.parallelstreams.Gender;
import de.muellerbruehl.parallelstreams.Person;
import de.muellerbruehl.parallelstreams.Persons;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.time.StopWatch;

/**
 *
 * @author Guilherme
 */
public class Cap3 {

    public static void main(String[] args) {
        Cap3 cap3 = new Cap3();
        StopWatch watch = new StopWatch();
        Persons personsFac = Persons.getInstance();
        List<Person> persons = personsFac.getPersons();

        watch.start();
        cap3.getPersonsLessThan20Years(persons);
        watch.split();
        System.out.println("Split 20y: " + watch.toSplitString());

        cap3.getPersonsByAgeRange(persons, 30, 40);
        watch.split();
        System.out.println("Split 30-40y: " + watch.toSplitString());

        cap3.getPersonByCondition(persons, new YoungerThanCondition(20));
        watch.split();
        System.out.println("Split 20y Condition: " + watch.toSplitString());
        
        cap3.getPersonByCondition(persons, t -> t.getAge() < 20);
        watch.split();
        System.out.println("Split 20y Labda: " + watch.toSplitString());
        
        cap3.getPersonByCondition(persons, (Person t) -> t.getGender().equals(Gender.Female));
        watch.split();
        System.out.println("Split Female Labda: " + watch.toSplitString());
        
        cap3.getPersonByCondition(persons, Person::isVendor);
        watch.split();
        System.out.println("Split Person::isVendor: " + watch.toSplitString());
        
        watch.stop();
    }

    @FunctionalInterface
    public interface Condition<T> {
        boolean test(T t);
    }

    public static class YoungerThanCondition implements Condition<Person> {
        private int _age;
        public YoungerThanCondition(int age) {
            this._age = age;
        }
        
        @Override
        public boolean test(Person t) {
            return t.getAge() < _age;
        }
    }
    
    public List<Person> getPersonByCondition(List<Person> persons, Condition<Person> condition) {
        List<Person> result = new ArrayList<>();
        for (Person person : persons) {
            if (condition.test(person)) {
                result.add(person);
            }
        }
        return result;
    }

    public List<Person> getPersonsLessThan20Years(List<Person> persons) {
        List<Person> result = new ArrayList<>();
        for (Person person : persons) {
            if (person.getAge() < 20) {
                result.add(person);
            }
        }
        return result;
    }

    public List<Person> getPersonsByAgeRange(List<Person> persons, int from, int to) {
        List<Person> result = new ArrayList<>();
        for (Person person : persons) {
            if (person.getAge() >= from && person.getAge() <= to) {
                result.add(person);
            }
        }
        return result;
    }
}
