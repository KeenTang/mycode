package com.k.nio.buffer.wrapper;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: keen
 * Date: 2018-07-01
 * Time: 21:22
 */
public final  class ByteUtil {
    public static int makeInt(byte b3,byte b2,byte b1,byte b0){
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }

    public static short makeShort(byte b1,byte b0){
        return (short)((b1 << 8) | (b0 & 0xff));
    }

    public static char makeChar(byte b1, byte b0) {
        return (char)((b1 << 8) | (b0 & 0xff));
    }

    public static long makeLong(byte b7, byte b6, byte b5, byte b4,
                                byte b3, byte b2, byte b1, byte b0){
        return ((((long)b7       ) << 56) |
                (((long)b6 & 0xff) << 48) |
                (((long)b5 & 0xff) << 40) |
                (((long)b4 & 0xff) << 32) |
                (((long)b3 & 0xff) << 24) |
                (((long)b2 & 0xff) << 16) |
                (((long)b1 & 0xff) <<  8) |
                (((long)b0 & 0xff)      ));
    }

}
