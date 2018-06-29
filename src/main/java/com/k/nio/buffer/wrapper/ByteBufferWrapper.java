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
        System.arraycopy(oldByteBuffer.array(), 0, byteBuffer.array(), 0, this.writeIndex());
        this.disable(oldByteBuffer);
    }

    public AbstractBufferWrapper put(ByteBuffer src) {
        return this.put(src, 0, src.remaining());
    }

    public AbstractBufferWrapper put(ByteBuffer src, int offset, int length) {
        checkBounds(offset,length,src.remaining());
        this.ensureCapacity(this.writeIndex() + length);
        System.arraycopy(src.array(), offset, this.byteBuffer.array(), this.writeIndex(), length);
        this.writeIndex(this.writeIndex() + length);
        return this;
    }


    public AbstractBufferWrapper put(byte[] src) {
        return this.put(src, 0, src.length);
    }

    public AbstractBufferWrapper put(byte val) {
        return this.put(this.writeIndex(),val);
    }

    public AbstractBufferWrapper put(int index,byte val) {
        this.ensureCapacity(this.writeIndex() + 1);
        byteBuffer.put(val);
        this.writeIndex(this.writeIndex() + 1);
        return this;
    }

    public AbstractBufferWrapper putInt(int val){
        this.ensureCapacity(this.writeIndex()+4);
        this.byteBuffer.putInt(val);
        return this;
    }

    public AbstractBufferWrapper put(byte[] src, int offset, int length) {
        checkBounds(offset,length,src.length);
        this.ensureCapacity(this.writeIndex() + src.length);
        byteBuffer.put(src, offset, length);
        this.writeIndex(this.writeIndex()+length);
        return this;
    }

    public byte get() {
        return get(this.readIndex());
    }

    public byte get(int index) {
        this.readIndex(index + 1);
        return this.byteBuffer.array()[index];
    }

    private void disable(ByteBuffer byteBuffer) {
        if (byteBuffer != null) {
            if (byteBuffer.isDirect()) {
                ((DirectBuffer) byteBuffer).cleaner().clean();
            }
            byteBuffer = null;
        }
    }
}
