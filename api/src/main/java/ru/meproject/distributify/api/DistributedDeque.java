package ru.meproject.distributify.api;

public interface DistributedDeque<E> {

    boolean add(E element);

    void addFirst(E element);

    void addLast(E element);

    boolean offer(E element);

    void offerFirst(E element);

    void offerLast(E element);

    E peek();

    E peekFirst();

    E peekLast();

    E poll();

    E pollFirst();

    E pollLast();

    E remove();

    boolean remove(E element);

    boolean removeFirst();

    boolean removeLast();

    boolean contains(E element);

    int size();

    int pos(E element);


}
