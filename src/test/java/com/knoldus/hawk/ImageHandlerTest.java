package com.knoldus.hawk;

import com.amazonaws.services.lambda.runtime.Context;
import com.knoldus.hawk.exceptions.DataNotFoundException;
import com.knoldus.hawk.exceptions.ValidationException;
import com.knoldus.hawk.utilities.ImageUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.knoldus.hawk.testConstants.TestConstants.CLICKED_IMAGE_FILE_PATH;
import static com.knoldus.hawk.testConstants.TestConstants.EMPLOYEE_CODE;
import static com.knoldus.hawk.testConstants.TestConstants.MATCHING_REFERENCE_IMAGE_FILE_PATH;
import static com.knoldus.hawk.testConstants.TestConstants.MATCHED_IMAGES_SCORE;
import static com.knoldus.hawk.testConstants.TestConstants.IMAGE_RECOGNIZED;
import static com.knoldus.hawk.testConstants.TestConstants.CLICKED_IMAGE;
import static com.knoldus.hawk.testConstants.TestConstants.IMAGE_ID;
import static com.knoldus.hawk.testConstants.TestConstants.DIFFERENT_REFERENCE_IMAGE_FILE_PATH;
import static com.knoldus.hawk.testConstants.TestConstants.UNMATCHED_IMAGES_SCORE;
import static com.knoldus.hawk.testConstants.TestConstants.IMAGE_NOT_RECOGNIZED;
import static com.knoldus.hawk.testConstants.TestConstants.REF_IMAGE_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageHandlerTest {

    private Context context;
    @Mock
    private ImageComparison compareImages;
    @Mock
    private ImageUtility imageUtility;
    @Mock
    private ImageReference imageReference;
    @InjectMocks
    private ImageHandler imageHandler;
    private File clickedImage = new File(CLICKED_IMAGE_FILE_PATH);
    private byte[] imageId = EMPLOYEE_CODE.getBytes();
    private Map<String, byte[]> images = new HashMap<>();

    @Test
    public void shouldReturnTrueForSimilarImages() throws IOException, ValidationException {
        File referenceImage = new File(MATCHING_REFERENCE_IMAGE_FILE_PATH);
        byte[] clickedImageFile = Files.readAllBytes(clickedImage.toPath());
        byte[] referenceImageFile = Files.readAllBytes(referenceImage.toPath());
        when(imageReference.getImageFromS3(EMPLOYEE_CODE)).thenReturn(CompletableFuture.completedFuture(referenceImageFile));
        when(compareImages.getSimilarity(clickedImageFile, referenceImageFile)).thenReturn(CompletableFuture.completedFuture(MATCHED_IMAGES_SCORE));
        when(imageUtility.checkImageThreshold(MATCHED_IMAGES_SCORE)).thenReturn(IMAGE_RECOGNIZED);
        images.put(CLICKED_IMAGE, clickedImageFile);
        images.put(IMAGE_ID, imageId);
        assertEquals(IMAGE_RECOGNIZED, imageHandler.handleRequest(images, context));
    }

    @Test
    public void shouldReturnFalseForDifferentImages() throws IOException, ValidationException {
        File referenceImage = new File(DIFFERENT_REFERENCE_IMAGE_FILE_PATH);
        byte[] clickedImageFile = Files.readAllBytes(clickedImage.toPath());
        byte[] referenceImageFile = Files.readAllBytes(referenceImage.toPath());
        when(imageReference.getImageFromS3(EMPLOYEE_CODE)).thenReturn(CompletableFuture.completedFuture(referenceImageFile));
        when(compareImages.getSimilarity(clickedImageFile, referenceImageFile)).thenReturn(CompletableFuture.completedFuture(UNMATCHED_IMAGES_SCORE));
        when(imageUtility.checkImageThreshold(UNMATCHED_IMAGES_SCORE)).thenReturn(IMAGE_NOT_RECOGNIZED);
        images.put(CLICKED_IMAGE, clickedImageFile);
        images.put(IMAGE_ID, imageId);
        assertEquals(IMAGE_NOT_RECOGNIZED, imageHandler.handleRequest(images, context));
    }

    @Test(expected = DataNotFoundException.class)
    public void shouldReturnDataNotFoundExceptionForInvalidImageId() throws IOException, ValidationException {
        byte[] clickedImageFile = Files.readAllBytes(clickedImage.toPath());
        when(imageReference.getImageFromS3(anyString()))
                .thenThrow(new DataNotFoundException(REF_IMAGE_NOT_FOUND));
        images.put(CLICKED_IMAGE, clickedImageFile);
        images.put(IMAGE_ID, imageId);
        imageHandler.handleRequest(images, context);
    }

    @Test(expected = ValidationException.class)
    public void shouldReturnValidationExceptionForInvalidImageId() throws IOException {
        byte[] clickedImageFile = Files.readAllBytes(clickedImage.toPath());
        images.put(CLICKED_IMAGE, clickedImageFile);
        images.put(IMAGE_ID, null);
        imageHandler.validateUserData(images);
        imageHandler.handleRequest(images, context);
    }


    @Test(expected = ValidationException.class)
    public void shouldReturnDataNotFoundException() {
        Map<String, byte[]> images = new HashMap<>();
        images.put(CLICKED_IMAGE, null);
        images.put(IMAGE_ID, null);
        imageHandler.validateUserData(images);
    }
}