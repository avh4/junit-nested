package net.avh4.test.junit.test.support;

public class Counter {
    private int count;

    public void reset() {
        count = 0;
    }

    public void increment() {
        count++;
    }

    public int count() {
        return count;
    }
}
