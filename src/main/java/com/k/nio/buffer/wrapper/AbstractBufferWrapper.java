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
            throw new IllegalArgumentException("capacity不能小于0,capacity=" + capacity);
        }
        if (capacity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("capacity不能大于" + Integer.MAX_VALUE + ",capacity=" + capacity);
        }
        this.capacity = capacity == 0 ? DEFAULT_CAPACITY : capacity;
        this.readIndex(readIndex);
        this.writeIndex(writeIndex);
    }

    public final int capacity() {
        return capacity;
    }


    public final int readIndex() {
        return this.readIndex;
    }


    final AbstractBufferWrapper readIndex(int readIndex) {
        if (readIndex < 0 || readIndex > writeIndex) {
            throw new IllegalArgumentException();
        }
        this.readIndex = readIndex;
        return this;
    }

    public final int writeIndex() {
        return this.writeIndex;
    }

    final AbstractBufferWrapper writeIndex(int writeIndex) {
        if (writeIndex < 0 || writeIndex > capacity) {
            throw new IllegalArgumentException();
        }
        this.writeIndex = writeIndex;
        return this;
    }

    public final AbstractBufferWrapper readMark() {
        this.markReadIndex = this.readIndex;
        return this;
    }

    public final AbstractBufferWrapper writeMark() {
        this.markWriteIndex = this.writeIndex;
        return this;
    }

    final void ensureCapacity(int minCapacity) {
        if (this.capacity < minCapacity) {
            this.rebuildBuffer(minCapacity);
        }
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

    final void calculateNewCapacity(int minCapacity){
        if(minCapacity<THRESHOLD){
            this.capacity= minCapacity<<1;
        }
        if(minCapacity==THRESHOLD){
            this.capacity= minCapacity;
        }
        this.capacity=Math.max(minCapacity,(int)(minCapacity*0.2)+minCapacity);
    }

    abstract void rebuildBuffer(int minCapcity);

}
