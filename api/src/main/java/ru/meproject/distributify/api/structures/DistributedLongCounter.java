package ru.meproject.distributify.api.structures;

/**
 * Blocking API for Counter
 */
public interface DistributedLongCounter {

    void add(long x);

    void increment();

    void decrement();

    long sum();

    void reset();

    long sumAndReset();
}
