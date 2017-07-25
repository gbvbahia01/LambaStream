/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams.caps;

import java.util.stream.LongStream;

/**
 *
 * @author Guilherme
 */
public class SequentialParallelAndParallelOrderedProcessing {

    public static void main(String[] arg) {
        /*
        For a simple demonstration, these three demos use a side effect. 
        All intermediate results will be stored in an external variable.
        Because this has to be effectively final, we used a dirty trick:
        instead of a long, we used an array. In a productive application, 
        never use such dirty tricks! Here is a clean variant.
        long reduce = LongStream.range(0, 1000).reduce(0, (a, c) -> (a + c) * c);
         */
        long[] result = new long[1];

        for (int i = 0; i < 10; i++) {
            result[0] = 0;
            LongStream.range(0, 1000)
                    .forEach(n -> result[0] = (result[0] + n) * n);
            System.out.println("serial: " + result[0]);
        }

        for (int i = 0; i < 10; i++) {
            result[0] = 0;
            LongStream.range(0, 1000).parallel()
                    .forEach(n -> result[0] = (result[0] + n) * n);
            System.out.println("parallel: " + result[0]);
        }

        for (int i = 0; i < 10; i++) {
            result[0] = 0;
            LongStream.range(0, 1000).parallel()
                    .forEachOrdered(n -> result[0] = (result[0] + n) * n);
            System.out.println("parallel ordered: " + result[0]);
        }

        /*
        The first parameter is used to initialize an accumulator.
        The second operation is a binary Function that takes the 
        accumulator (a) and the current object (c) of the stream.
        The result of this operation will replace the
        former value of the accumulator and becomes available
        for the next object of the stream.
         */
        long reduce = LongStream.range(0, 1000).reduce(0, (a, c) -> (a + c) * c);
        System.out.println("reduce: " + reduce);
    }
}
