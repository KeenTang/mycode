package com.k.nio.buffer.wrapper;

import com.k.nio.buffer.wrapper.AbstractBufferWrapper;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * @author keen on 2018/6/12.
 */
public class ByteBufferWrapper extends AbstractBufferWrapper {
    private ByteBuffer byteBuffer;

    public ByteBufferWrapper(boolean isDirect) {
        this(0, isDirect);
    }

    public ByteBufferWrapper(int capacity, boolean isDirect) {
        super(capacity);
        byteBuffer = isDirect ? ByteBuffer.allocateDirect(this.capacity()) : ByteBuffer.allocate(this.capacity());
    }

    void rebuildBuffer(int minCapacity) {
        calculateNewCapacity(minCapacity);
        ByteBuffer oldByteBuffer = this.byteBuffer;
        byteBuffer = oldByteBuffer.isDirect() ? ByteBuffer.allocateDirect(this.capacity()) : ByteBuffer.allocate(this.capacity());
        System.arraycopy(oldByteBuffer.array(),0,byteBuffer.array(),0,this.writeIndex());
        this.disable(oldByteBuffer);
    }

    public AbstractBufferWrapper put(ByteBuffer byteBuffer){
        return this;
    }

    public AbstractBufferWrapper put(byte val) {
        this.ensureCapacity(this.writeIndex() + 1);
        byteBuffer.put(val);
        return this;
    }

    public AbstractBufferWrapper put(byte[] src) {
        this.ensureCapacity(this.writeIndex()+src.length);
        return this.put(src,0,src.length);
    }

    public AbstractBufferWrapper put(byte[] src, int offset, int length) {
        byteBuffer.put(src,offset,length);
        return this;
    }

    public byte get() {
       return get(this.readIndex());
    }

    public byte get(int index) {
        if(index<0||index>this.writeIndex()){
            throw new IndexOutOfBoundsException();
        }
        byte b=this.byteBuffer.array()[index];
        this.readIndex(this.readIndex()+1);
        return b;
    }

    private void disable(ByteBuffer byteBuffer){
        if(byteBuffer!=null){
            if(byteBuffer.isDirect()){
                ((DirectBuffer)byteBuffer).cleaner().clean();
            }
            byteBuffer=null;
        }
    }
}
