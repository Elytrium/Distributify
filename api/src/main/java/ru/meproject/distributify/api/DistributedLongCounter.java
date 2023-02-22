package ru.meproject.distributify.api;

public interface DistributedLongCounter {

    void add(long x);

    void increment();

    void decrement();

    long sum();

    void reset();

    long sumAndReset();
}
