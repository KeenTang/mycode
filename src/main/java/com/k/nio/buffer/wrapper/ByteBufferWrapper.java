package com.k.nio.buffer.wrapper;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author keen on 2018/6/12.
 */
public class ByteBufferWrapper {
    private final static int DEFAULT_CAPACITY = 2 << 7;
    private final static int MAX_CAPACITY = Integer.MAX_VALUE;
    private final static int THRESHOLD = 1024 * 1024 * 4;
    private int capacity;
    private int markReadIndex;
    private int markWriteIndex;
    private int readIndex;
    private int writeIndex;
    //private boolean bigEndian;
    private ByteBuffer byteBuffer;

    public ByteBufferWrapper(boolean isDirect) {
        this(DEFAULT_CAPACITY, isDirect);
    }

    public ByteBufferWrapper(int capacity, boolean isDirect) {
        this(capacity, 0, 0, isDirect);
    }

    public ByteBufferWrapper(int capacity, int readIndex, int writeIndex, boolean isDirect) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity<0");
        }
        if (capacity > MAX_CAPACITY) {
            throw new IllegalArgumentException("capacity>" + MAX_CAPACITY);
        }
        this.capacity = capacity == 0 ? DEFAULT_CAPACITY : capacity;
        this.readIndex(readIndex);
        this.writeIndex(writeIndex);
        byteBuffer = isDirect ? ByteBuffer.allocateDirect(this.capacity()) : ByteBuffer.allocate(this.capacity());
    }


    public int capacity() {
        return capacity;
    }

    public int writeIndex() {
        return this.writeIndex;
    }

    public int readIndex() {
        return this.readIndex;
    }

    public void readIndex(int readIndex) {
        this.checkIndex(readIndex, 0);
        this.readIndex = readIndex;
    }

    public void writeIndex(int writeIndex) {
        if (writeIndex < readIndex || writeIndex > capacity) {
            throw new IndexOutOfBoundsException();
        }
        this.writeIndex = writeIndex;
    }

    public ByteBufferWrapper markReadIndex() {
        this.markReadIndex = this.readIndex;
        return this;
    }

    public ByteBufferWrapper markWriteIndex() {
        this.markWriteIndex = this.writeIndex;
        return this;
    }


    public ByteBufferWrapper clear() {
        this.writeIndex = readIndex = 0;
        this.discardMark();
        return this;
    }

    public ByteBufferWrapper discardReadMark() {
        this.markReadIndex = 0;
        return this;
    }

    public ByteBufferWrapper discardWriteMark() {
        this.markWriteIndex = 0;
        return this;
    }

    public ByteBufferWrapper discardMark() {
        this.discardWriteMark();
        this.discardReadMark();
        return this;
    }

    public ByteBufferWrapper put(byte val) {
        this.ensureCapacity(1);
        this.byteBuffer.put(val);
        this.writeIndex++;
        return this;
    }

    public ByteBufferWrapper put(ByteBuffer src) {
        return this.put(src, 0, src.remaining());
    }

    public ByteBufferWrapper put(ByteBuffer src, int offset, int length) {
        checkBounds(offset, length, src.remaining());
        this.ensureCapacity(length);
        System.arraycopy(src.array(), offset, this.byteBuffer.array(), this.writeIndex(), length);
        this.writeIndex += length;
        return this;
    }


    public ByteBufferWrapper put(byte[] src) {
        return this.put(src, 0, src.length);
    }


    public ByteBufferWrapper put(byte[] src, int offset, int length) {
        checkBounds(offset, length, src.length);
        this.ensureCapacity(src.length);
        byteBuffer.put(src, offset, length);
        this.writeIndex += length;
        return this;
    }

    public ByteBufferWrapper putBoolean(boolean val) {
        this.ensureCapacity(1);
        this.byteBuffer.put((byte) (val ? 1 : 0));
        this.writeIndex++;
        return this;
    }

    public ByteBufferWrapper putShort(short val) {
        this.ensureCapacity(2);
        this.byteBuffer.putShort(val);
        this.writeIndex += 2;
        return this;
    }

    public ByteBufferWrapper putChar(char val) {
        this.ensureCapacity(val);
        this.byteBuffer.putChar(val);
        this.writeIndex += 2;
        return this;
    }

    public ByteBufferWrapper putInt(int val) {
        this.ensureCapacity(4);
        this.byteBuffer.putInt(val);
        this.writeIndex += 4;
        return this;
    }

    public ByteBufferWrapper putLong(long val) {
        this.ensureCapacity(8);
        this.byteBuffer.putLong(val);
        this.writeIndex += 8;
        return this;
    }

    public ByteBufferWrapper putFloat(float val) {
        this.ensureCapacity(8);
        this.byteBuffer.putFloat(val);
        this.writeIndex += 4;
        return this;
    }

    public ByteBufferWrapper putDouble(double val) {
        this.ensureCapacity(8);
        this.byteBuffer.putDouble(val);
        this.writeIndex += 8;
        return this;
    }


    public byte get() {
        return get(this.readIndex());
    }

    public byte get(int index) {
        this.checkIndex(index, 1);
        byte b = this.byteBuffer.array()[index];
        this.readIndex++;
        return b;
    }

    public short getShort() {
        this.checkIndex(this.readIndex, 2);
        short s;
        if (this.isBigEndian()) {
            s = ByteUtil.makeShort(this.byteBuffer.array()[this.readIndex], this.byteBuffer.array()[this.readIndex + 1]);
        } else {
            s = ByteUtil.makeShort(this.byteBuffer.array()[this.readIndex + 1], this.byteBuffer.array()[this.readIndex]);
        }
        this.readIndex += 2;
        return s;
    }

    public char getChar() {
        this.checkIndex(this.readIndex, 2);
        char c;
        if (this.isBigEndian()) {
            c = ByteUtil.makeChar(this.byteBuffer.array()[this.readIndex], this.byteBuffer.array()[this.readIndex + 1]);
        } else {
            c = ByteUtil.makeChar(this.byteBuffer.array()[this.readIndex + 1], this.byteBuffer.array()[this.readIndex]);
        }
        this.readIndex += 2;
        return c;
    }

    public int getInt() {
        this.checkIndex(this.readIndex, 4);
        byte[] array = this.byteBuffer.array();
        int i;
        if (this.isBigEndian()) {
            i = ByteUtil.makeInt(array[this.readIndex], array[this.readIndex + 1], array[this.readIndex + 2], array[this.readIndex + 3]);
        } else {
            i = ByteUtil.makeInt(array[this.readIndex + 3], array[this.readIndex + 2], array[this.readIndex + 1], array[this.readIndex]);
        }

        this.readIndex += 4;
        return i;
    }

    public long getLong() {
        this.checkIndex(this.readIndex, 8);
        byte[] array = this.byteBuffer.array();
        long l;
        if (this.isBigEndian()) {
            l = ByteUtil.makeLong(array[this.readIndex], array[this.readIndex + 1], array[this.readIndex + 2], array[this.readIndex + 3],
                    array[this.readIndex + 4], array[this.readIndex + 5], array[this.readIndex + 6], array[this.readIndex + 7]);
        } else {
            l = ByteUtil.makeLong(array[this.readIndex + 7], array[this.readIndex + 6], array[this.readIndex + 5], array[this.readIndex + 4],
                    array[this.readIndex + 3], array[this.readIndex + 2], array[this.readIndex + 1], array[this.readIndex]);
        }

        this.readIndex += 8;
        return l;
    }

    public float getFloat() {
        return Float.intBitsToFloat(this.getInt());
    }

    public double getDouble() {
        return Double.longBitsToDouble(this.getLong());
    }

    public int remaining() {
        return this.writeIndex - this.readIndex;
    }

    public boolean hasRemaining() {
        return this.remaining() > 0;
    }

    public boolean isDirect() {
        return this.byteBuffer.isDirect();
    }

    public ByteOrder order() {
        return this.byteBuffer.order();
    }

    public ByteBufferWrapper order(ByteOrder byteOrder) {
        this.byteBuffer.order(byteOrder);
        return this;
    }

    public boolean isBigEndian() {
        return this.order() == ByteOrder.BIG_ENDIAN;
    }

    public ByteBufferWrapper compact() {
        if (this.readIndex == 0) {
            return this;
        }
        if (this.writeIndex == this.readIndex) {
            this.writeIndex = this.readIndex = 0;
        } else {
            System.arraycopy(this.byteBuffer.array(), this.readIndex, this.byteBuffer.array(), 0, this.writeIndex - this.readIndex);
            this.writeIndex -= this.readIndex;
            this.readIndex = 0;
        }
        this.discardMark();
        return this;
    }

    private void checkIndex(int index, int length) {
        checkBounds(index, length, this.writeIndex);
    }

    private void disable(ByteBuffer byteBuffer) {
        if (byteBuffer != null) {
            if (byteBuffer.isDirect()) {
                ((DirectBuffer) byteBuffer).cleaner().clean();
            }
            byteBuffer = null;
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new IllegalArgumentException("minCapacity小于0");
        }
        if (this.writeCapacity() < minCapacity) {
            if (MAX_CAPACITY - writeIndex < minCapacity) {
                throw new IndexOutOfBoundsException(String.format("writeIndex+minCapacity大于maxCapacity," +
                        "writeIndex=%d,minCapacity=%d,maxCapacity=%d", writeIndex, minCapacity, DEFAULT_CAPACITY));
            }
            this.rebuildBuffer(minCapacity);
        }
    }

    private void calculateNewCapacity(int minCapacity) {
        if (minCapacity < THRESHOLD) {
            this.capacity = minCapacity << 1;
        }
        if (minCapacity == THRESHOLD) {
            this.capacity = minCapacity;
        }
        this.capacity = Math.max(minCapacity, (int) (minCapacity * 0.2) + minCapacity);
    }

    private void checkBounds(int offset, int length, int size) {
        if ((offset | length | (size - length - offset)) < 0) {
            throw new IndexOutOfBoundsException();
        }
    }


    private int writeCapacity() {
        return this.capacity - this.writeIndex;
    }

    private void rebuildBuffer(int minCapacity) {
        calculateNewCapacity(minCapacity);
        ByteBuffer oldByteBuffer = this.byteBuffer;
        byteBuffer = oldByteBuffer.isDirect() ? ByteBuffer.allocateDirect(this.capacity()) : ByteBuffer.allocate(this.capacity());
        System.arraycopy(oldByteBuffer.array(), 0, byteBuffer.array(), 0, this.writeIndex());
        this.disable(oldByteBuffer);
    }
}
