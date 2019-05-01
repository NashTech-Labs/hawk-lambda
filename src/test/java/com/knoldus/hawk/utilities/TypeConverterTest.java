package com.knoldus.hawk.utilities;

import org.junit.Test;

import java.nio.ByteBuffer;

import static com.knoldus.hawk.testConstants.TestConstants.IMAGE_ID;
import static org.junit.Assert.assertEquals;

public class TypeConverterTest {
    @Test
    public void shouldReturnByteBufferOfByteArray() {
        TypeConverter typeConverter = new TypeConverter();
        assertEquals(ByteBuffer.wrap(IMAGE_ID.getBytes()), typeConverter.byteArrayToByteBuffer(IMAGE_ID.getBytes()));
    }
}