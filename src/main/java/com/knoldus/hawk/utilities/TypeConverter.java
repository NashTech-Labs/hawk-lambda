package com.knoldus.hawk.utilities;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * The TypeConverter converts the image.
 */
public class TypeConverter {
    private static final Logger logger = Logger.getLogger(TypeConverter.class.getName());

    /**
     * This method is used to get image buffer.
     *
     * @param bytes This is the image
     * @return This returns is image buffer
     */
    public ByteBuffer byteArrayToByteBuffer(byte[] bytes) {
        logger.info("byteArrayToByteBuffer is hit successfully");
        return ByteBuffer.wrap(bytes);
    }
}