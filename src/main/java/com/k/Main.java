package com.k;


import com.k.list.ListTest;
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
        ListTest.testSimpleLinkedList();
    }
}
