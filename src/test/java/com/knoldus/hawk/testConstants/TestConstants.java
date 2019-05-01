package com.knoldus.hawk.testConstants;

/**
 * This class contains all the constants which are used in the test cases of the current application.
 */

public class TestConstants {

    /**
     * Image Id or object key of the image in S3 Bucket.
     */
    public static final String IMAGE_ID = "image_id";

    /**
     * Object Key of the actual image sent for recognition.
     */
    public static final String CLICKED_IMAGE = "clicked_image";

    /**
     * Returned error if the image is not found in the S3 Bucket for reference.
     */
    public static final String REF_IMAGE_NOT_FOUND = "No reference Image found";

    /**
     * Error Message if the image sent for recognition is not received by the AWS Lambda.
     */
    public static final String CLICKED_IMAGE_NOT_FOUND = "clicked_image not found";

    /**
     * String value to return when Image is recognized successfully.
     */
    public static final String IMAGE_RECOGNIZED = "Image Recognized";

    /**
     * String value to return when Image is not recognized successfully.
     */
    public static final String IMAGE_NOT_RECOGNIZED = "Image Not Recognized";

    /**
     * String value for Clicked Image file path.
     */
    public static final String CLICKED_IMAGE_FILE_PATH = "src/test/resources/source_image.jpg";

    /**
     * String value for Matching Reference Image file path.
     */
    public static final String MATCHING_REFERENCE_IMAGE_FILE_PATH = "src/test/resources/target_image_similar_to_source.png";

    /**
     * String value for Different Reference Image file path.
     */
    public static final String DIFFERENT_REFERENCE_IMAGE_FILE_PATH = "src/test/resources/target_image_different_from_source.jpg";

    /**
     * String value for Clicked Image that does not exist.
     */
    public static final String NO_CLICKED_IMAGE = "No Clicked Image";

    /**
     * String value for Employee code for reference.
     */
    public static final String EMPLOYEE_CODE = "Employee code";

    /**
     * Image Similarity value for matched images.
     */
    public static final Float MATCHED_IMAGES_SCORE = 95.0f;

    /**
     * Image Similarity value for unmatched images.
     */
    public static final Float UNMATCHED_IMAGES_SCORE = 20.0f;
}