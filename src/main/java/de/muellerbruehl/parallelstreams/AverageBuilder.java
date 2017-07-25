/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams;

/**
 *
 * @author Guilherme
 */
public class AverageBuilder {
    
    private int _count;
    private long _cents;

    public int getCount() {
        return _count;
    }

    public long getCents() {
        return _cents;
    }
    
    public void add(long cents) {
        _count++;
        _cents += cents;
    }
    
    public void add(AverageBuilder avg) {
        _count += avg._count;
        _cents += avg._cents;
    }
    
    public double getAverage() {
        return _cents / 100D / _count;
    }
}
