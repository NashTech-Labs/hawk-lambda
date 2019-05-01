package com.knoldus.hawk.utilities;

import org.junit.Test;

import static com.knoldus.hawk.testConstants.TestConstants.*;
import static org.junit.Assert.assertEquals;

public class ImageUtilityTest {
    private ImageUtility imageUtility = new ImageUtility();

    @Test
    public void shouldReturnTrueForAcceptedThreshold() {
        assertEquals(IMAGE_RECOGNIZED, imageUtility.checkImageThreshold(MATCHED_IMAGES_SCORE));
    }

    @Test
    public void shouldReturnFalseForRejectedThreshold() {
        assertEquals(IMAGE_NOT_RECOGNIZED, imageUtility.checkImageThreshold(UNMATCHED_IMAGES_SCORE));
    }
}