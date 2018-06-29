package com.k.nio.buffer.wrapper;

import java.nio.Buffer;

/**
 * @author keen on 2018/6/12.
 */
public abstract class AbstractBufferWrapper {
    private final static int DEFAULT_CAPACITY = 2 << 7;
    private final static int THRESHOLD = 1024 * 1024 * 4;
    private int capacity;
    private int markReadIndex;
    private int markWriteIndex;
    private int readIndex;
    private int writeIndex;

    AbstractBufferWrapper() {
        this(DEFAULT_CAPACITY);
    }

    AbstractBufferWrapper(int capacity) {
        this(capacity, 0, 0);
    }

    AbstractBufferWrapper(int capacity, int readIndex, int writeIndex) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity<0");
        }
        if (capacity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("capacity>" + Integer.MAX_VALUE);
        }
        this.capacity = capacity == 0 ? DEFAULT_CAPACITY : capacity;
        this.readIndex(readIndex);
        this.writeIndex(writeIndex);
    }

    final void ensureCapacity(int minCapacity) {
        if (this.capacity < minCapacity) {
            this.rebuildBuffer(minCapacity);
        }
    }

    final void calculateNewCapacity(int minCapacity) {
        if (minCapacity < THRESHOLD) {
            this.capacity = minCapacity << 1;
        }
        if (minCapacity == THRESHOLD) {
            this.capacity = minCapacity;
        }
        this.capacity = Math.max(minCapacity, (int) (minCapacity * 0.2) + minCapacity);
    }

    final void checkBounds(int offset, int length, int size) {
        if ((offset | length | (size - length - offset)) < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    final AbstractBufferWrapper readIndex(int readIndex) {
        if (readIndex < 0 || readIndex > writeIndex) {
            throw new IndexOutOfBoundsException();
        }
        this.readIndex = readIndex;
        return this;
    }

    final AbstractBufferWrapper writeIndex(int writeIndex) {
        if (writeIndex < 0 || writeIndex > capacity) {
            throw new IndexOutOfBoundsException();
        }
        this.writeIndex = writeIndex;
        return this;
    }

    final byte[] int2bytes(int val){
        return null;
    }

    public final int writeIndex() {
        return this.writeIndex;
    }

    public final int capacity() {
        return capacity;
    }


    public final int readIndex() {
        return this.readIndex;
    }

    public final AbstractBufferWrapper mardReadIndex() {
        this.markReadIndex = this.readIndex;
        return this;
    }

    public final AbstractBufferWrapper markWriteIndex() {
        this.markWriteIndex = this.writeIndex;
        return this;
    }


    public final AbstractBufferWrapper clear() {
        this.writeIndex = readIndex = 0;
        this.discardReadMark();
        this.discardWriteMark();
        return this;
    }

    public final AbstractBufferWrapper discardReadMark() {
        this.markReadIndex = 0;
        return this;
    }

    public final AbstractBufferWrapper discardWriteMark() {
        this.markWriteIndex = 0;
        return this;
    }

    abstract void rebuildBuffer(int minCapcity);

}
