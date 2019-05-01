package com.knoldus.hawk;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.knoldus.hawk.utilities.TypeConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.knoldus.hawk.testConstants.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageComparisonTest {

    @Mock
    private TypeConverter typeConverter;
    @Mock
    private CompareFacesRequest compareFacesRequest;
    @Mock
    private CompareFacesMatch compareFacesMatch;
    @Mock
    private AmazonRekognition amazonRekognition;
    @Mock
    private CompareFacesResult compareFacesResult;
    @InjectMocks
    private ImageComparison compareImages;
    private List<CompareFacesMatch> compareFacesMatches = new ArrayList<>();
    private File clickedImage = new File(CLICKED_IMAGE_FILE_PATH);

    @Test
    public void shouldReturnValidThresholdForSimilarImages() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        compareFacesMatches.add(compareFacesMatch);
        File referenceImage = new File(MATCHING_REFERENCE_IMAGE_FILE_PATH);
        byte[] clickedImageFile = Files.readAllBytes(clickedImage.toPath());
        byte[] referenceImageFile = Files.readAllBytes(referenceImage.toPath());
        when(typeConverter.byteArrayToByteBuffer(clickedImageFile))
                .thenReturn(ByteBuffer.wrap(clickedImageFile));
        when(typeConverter.byteArrayToByteBuffer(clickedImageFile))
                .thenReturn(ByteBuffer.wrap(referenceImageFile));
        when(compareFacesRequest
                .withSourceImage(any()))
                .thenReturn(compareFacesRequest);
        when(compareFacesRequest
                .withTargetImage(any()))
                .thenReturn(compareFacesRequest);
        when(amazonRekognition.compareFaces(compareFacesRequest))
                .thenReturn(compareFacesResult);
        when(compareFacesResult.getFaceMatches()).thenReturn(compareFacesMatches);
        when(compareFacesMatch.getSimilarity()).thenReturn(MATCHED_IMAGES_SCORE);
        assertEquals(MATCHED_IMAGES_SCORE, compareImages.getSimilarity(
                clickedImageFile, referenceImageFile).get(10, TimeUnit.SECONDS), 0.0);
    }

    @Test
    public void shouldReturnInvalidThresholdForDifferentImages() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        compareFacesMatches.add(compareFacesMatch);
        File referenceImage = new File(DIFFERENT_REFERENCE_IMAGE_FILE_PATH);
        byte[] clickedImageFile = Files.readAllBytes(clickedImage.toPath());
        byte[] referenceImageFile = Files.readAllBytes(referenceImage.toPath());
        when(typeConverter.byteArrayToByteBuffer(clickedImageFile))
                .thenReturn(ByteBuffer.wrap(clickedImageFile));
        when(typeConverter.byteArrayToByteBuffer(clickedImageFile))
                .thenReturn(ByteBuffer.wrap(referenceImageFile));
        when(compareFacesRequest
                .withSourceImage(any()))
                .thenReturn(compareFacesRequest);
        when(compareFacesRequest
                .withTargetImage(any()))
                .thenReturn(compareFacesRequest);
        when(amazonRekognition.compareFaces(compareFacesRequest))
                .thenReturn(compareFacesResult);
        when(compareFacesResult.getFaceMatches()).thenReturn(compareFacesMatches);
        when(compareFacesMatch.getSimilarity()).thenReturn(UNMATCHED_IMAGES_SCORE);
        assertEquals(UNMATCHED_IMAGES_SCORE, compareImages.getSimilarity(
                clickedImageFile, referenceImageFile).get(10, TimeUnit.SECONDS), 0.0);
    }
}