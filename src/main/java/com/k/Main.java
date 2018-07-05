package com.k;


import com.k.nio.buffer.wrapper.ByteBufferWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * author keen on 2018/6/12.
 */
public class Main {
    private static final Logger log=LoggerFactory.getLogger(Main.class);
    public static void main(String[]args){
        ByteBuf byteBuf=Unpooled.buffer(200);
        //Unsafe unsafe = Unsafe.getUnsafe();
       // unsafe.
        //byteBuf.writei
        ByteBufferWrapper bufferWrapper=new ByteBufferWrapper(false);

        byte[]b={1,23,21,11,2};
        bufferWrapper.put(b);
        byte b1 = bufferWrapper.get();
        System.out.println(Arrays.toString(bufferWrapper.getByteBuffer().array()));
        byte[] range = Arrays.copyOfRange(b, 2, 4);
        System.out.println(Arrays.toString(range));
    }
}
