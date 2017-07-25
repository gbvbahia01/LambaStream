/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muellerbruehl.parallelstreams.caps;

/**
 *
 * @author Guilherme
 */
public class DefaultMethod {

    public interface InterfaceA {

        default InterfaceA print() {
            System.out.println("InterfaceA::print");
            return this;
        }

        default InterfaceA sout() {
            System.out.println("InterfaceA::sout");
            return this;
        }
    }

    public static class InterfaceImplementor implements InterfaceA {

        public InterfaceA sout() {
            System.out.println("InterfaceImplementor::sout");
            return this;
        }
    }

    public static void main(String[] args) {
        new InterfaceImplementor().print().sout();
    }
}
