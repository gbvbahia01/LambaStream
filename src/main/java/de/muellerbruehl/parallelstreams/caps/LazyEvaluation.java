/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams.caps;

import de.muellerbruehl.parallelstreams.Person;
import de.muellerbruehl.parallelstreams.Persons;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 *
 * @author Guilherme
 */
public class LazyEvaluation {

    public static void main(String[] args) {
        Persons personsFac = Persons.getInstance();
        List<Person> persons = personsFac.getPersons();
        int personCount = invokeMethod(() -> getVendorCount(persons));
        System.out.print("personCount:");
        System.out.println(personCount);    
    }

    public static <T> T invokeMethod(Supplier<T> method) {
        System.out.println("invokeMethod");
        long start = System.nanoTime();
        T result = method.get();
        long elapsedTime = System.nanoTime() - start;
        System.out.println("Elapsed time: " + elapsedTime / 1000000);
        return result;
    }

    private static Integer getVendorCount(List<Person> persons) {
        System.out.println("getVendorCount");
        List<Person> vendors = new LinkedList<>();
        persons.forEach((Person p) -> { 
            if (p.isVendor()) vendors.add(p);
        });
        return vendors.size();
    }
}
