package com.knoldus.hawk.constants;

/**
 * This class contains all the constants which are used in the current application.
 * The class and the data members are package-private
 */

public class Constants {

    /**
     * Bucket Name used to fetch the reference image.
     */
    public static final String REFERENCE_BUCKET_NAME = System.getenv("reference_bucket");

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
     * Error message if the image payload is null.
     */
    public static final String EMPTY_IMAGE_PAYLOAD = "Image Payload is Empty";

    /**
     * Error message if timeout.
     */
    public static final String TIMEOUT = "Timeout occurred while waiting for response from AWS Rekognition";

    /**
     * Error message if trouble in execution.
     */
    public static final String INTERRUPT_FLAG = "Failed to recognize due to : ";

    /**
     * Threshold value for image similarity.
     */
    public static final Float THRESHOLD = 80.0f;

    /**
     * Default Image Similarity value.
     */
    public static final Float DEFAULT_IMAGE_SIMILARITY = 0.0f;

    /**
     * Set invalid length.
     */
    public static final int INVALID_LENGTH = 0;

    /**
     * Error message for converting S3 object to byte array.
     */
    public static final String PARSING_ERROR = "Error while parsing S3 object to byte array";

    /**
     * String value to return when Image is recognized successfully.
     */
    public static final String IMAGE_RECOGNIZED = "Image Recognized";

    /**
     * String value to return when Image is not recognized successfully.
     */
    public static final String IMAGE_NOT_RECOGNIZED = "Image Not Recognized";

    /**
     * Error code of InvalidParameterException.
     */
    public static final String INVALID_IMAGE = "No face discovered";

}
