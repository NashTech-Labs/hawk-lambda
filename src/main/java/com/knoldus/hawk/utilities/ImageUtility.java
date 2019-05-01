package com.knoldus.hawk.utilities;

import java.util.logging.Logger;

import static com.knoldus.hawk.constants.Constants.IMAGE_RECOGNIZED;
import static com.knoldus.hawk.constants.Constants.IMAGE_NOT_RECOGNIZED;
import static com.knoldus.hawk.constants.Constants.THRESHOLD;

/**
 * The ImageUtility gives you final face match message.
 */
public class ImageUtility {
    private static final Logger logger = Logger.getLogger(ImageUtility.class.getName());

    /**
     * This method is used to give final face match message.
     *
     * @param imageSimilarity is the score of AWS Face Recognition
     * @return String This returns response message asynchronously
     */
    public String checkImageThreshold(Float imageSimilarity) {
        logger.info("checkImageThreshold is hit successfully");
        logger.info("imageSimilarity is " + imageSimilarity);
        return imageSimilarity >= THRESHOLD ? IMAGE_RECOGNIZED : IMAGE_NOT_RECOGNIZED;
    }
}