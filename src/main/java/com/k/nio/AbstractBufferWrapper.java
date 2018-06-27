package com.k.nio;

import java.nio.Buffer;

/**
 * @author keen on 2018/6/12.
 */
public abstract class AbstractBufferWrapper {
    private final static int DEFAULT_CAPACITY = 256;
    private int capacity;
    private int mark;
    private int position;
    private int limit;
    private int readIndex;
    private int writeIndex;
    protected Buffer buffer;

    AbstractBufferWrapper() {
        this(DEFAULT_CAPACITY);
    }

    AbstractBufferWrapper(int capacity) {
        this.capacity = capacity;
    }

    AbstractBufferWrapper(int capacity, int position, int limit, int mark, int readIndex, int writeIndex) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}
