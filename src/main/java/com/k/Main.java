package com.k;


import com.k.nio.buffer.wrapper.ByteBufferWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * author keen on 2018/6/12.
 */
public class Main {
    public static void main(String[]args){
        ByteBuf byteBuf=Unpooled.buffer(200);
        //Unsafe unsafe = Unsafe.getUnsafe();
       // unsafe.
        //byteBuf.writei
        ByteBufferWrapper bufferWrapper=new ByteBufferWrapper(false);
        bufferWrapper.order(ByteOrder.LITTLE_ENDIAN);
        bufferWrapper.putFloat(2.11111f);
        bufferWrapper.put((byte) 21);
        bufferWrapper.putChar('A');

        System.out.println(bufferWrapper.getFloat());
        System.out.println(bufferWrapper.get());
        System.out.println(bufferWrapper.getChar());
        ByteBuffer buffer=ByteBuffer.allocate(2);
        buffer.isDirect();
        byte[] bytes = buffer.array();
        buffer.asCharBuffer();
        //buffer.order()
        bytes[0]=(byte)1;
    }
}
