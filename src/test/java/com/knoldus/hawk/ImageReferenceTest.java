package com.knoldus.hawk;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.knoldus.hawk.exceptions.DataNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.knoldus.hawk.testConstants.TestConstants.IMAGE_ID;
import static com.knoldus.hawk.testConstants.TestConstants.REF_IMAGE_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageReferenceTest {

    @Mock
    private AmazonS3 s3Client;
    @Mock
    private S3Object s3Object;
    @InjectMocks
    private ImageReference imageReference;

    @Test(expected = ExecutionException.class)
    public void shouldReturnExecutionException() throws ExecutionException, InterruptedException, TimeoutException {
        when(s3Client.getObject(any())).thenThrow(new DataNotFoundException(REF_IMAGE_NOT_FOUND));
        byte[] response = REF_IMAGE_NOT_FOUND.getBytes();
        assertEquals(response, imageReference.getImageFromS3(IMAGE_ID).get(10, TimeUnit.SECONDS));
    }
}

