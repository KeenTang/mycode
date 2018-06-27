package com.k.nio.buffer.wrapper;

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
        this(capacity,0,0,0,0,0);
    }

    AbstractBufferWrapper(int capacity, int limit, int position, int readIndex, int writeIndex, int mark) {
        if(capacity<0){
            throw new IllegalArgumentException("capacity不能小于0,capacity="+capacity);
        }
        if(capacity>Integer.MAX_VALUE){
            throw new IllegalArgumentException("capacity不能大于"+Integer.MAX_VALUE+",capacity="+capacity);
        }
        this.capacity = capacity;
        this.limit(limit);
        this.position(position);
        this.readIndex(readIndex);
        this.writeIndex(writeIndex);
        if(mark>=0){
            if(mark>writeIndex){
                throw new IllegalArgumentException();
            }
            this.mark=mark;
        }

    }

    public final int capacity() {
        return capacity;
    }

    public final int limit(){
        return this.limit;
    }

    public final AbstractBufferWrapper limit(int limit){
        if(limit<0||limit>capacity){
            throw new IllegalArgumentException();
        }
        this.limit=limit;
        return this;
    }

    public final int position(){
        return this.position;
    }

    public final AbstractBufferWrapper position(int position){
        if(position<0||position>limit){
            throw new IllegalArgumentException();
        }
        this.position=position;
        return this;
    }

    public final int readIndex(){
        return this.readIndex;
    }


    public final AbstractBufferWrapper readIndex(int readIndex){
        if(readIndex<0||readIndex>writeIndex){
            throw new IllegalArgumentException();
        }
        this.readIndex=readIndex;
        return this;
    }

    public final int writeIndex(){
        return this.writeIndex;
    }

    public final AbstractBufferWrapper writeIndex(int writeIndex){
        if(writeIndex<0||writeIndex>limit){
            throw new IllegalArgumentException();
        }
        this.writeIndex=writeIndex;
        return this;
    }



}
