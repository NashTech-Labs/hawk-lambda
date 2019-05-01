package com.knoldus.hawk;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.knoldus.hawk.exceptions.ValidationException;
import com.knoldus.hawk.utilities.ImageUtility;
import com.knoldus.hawk.utilities.TypeConverter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static com.knoldus.hawk.constants.Constants.IMAGE_ID;
import static com.knoldus.hawk.constants.Constants.CLICKED_IMAGE;
import static com.knoldus.hawk.constants.Constants.INTERRUPT_FLAG;
import static com.knoldus.hawk.constants.Constants.TIMEOUT;
import static com.knoldus.hawk.constants.Constants.EMPTY_IMAGE_PAYLOAD;

/**
 * The ImageHandler handles the Http Request and hit AWS rekognition for image comparison.
 */
public class ImageHandler implements RequestHandler<Map<String, byte[]>, String> {

    private static final Logger logger = Logger.getLogger(ImageHandler.class.getName());
    private CompareFacesResult compareFacesResult;
    private List<CompareFacesMatch> compareFacesMatches;
    private AmazonS3 amazonS3;
    private ImageUtility imageUtility;
    private ImageComparison imageComparison;
    private ImageReference imageReference;
    private TypeConverter typeConverter;
    private AmazonRekognition amazonRekognition;
    private CompareFacesRequest compareFacesRequest;

    /**
     * Constructor for class ImageHandler.
     */
    public ImageHandler() {
        typeConverter = new TypeConverter();
        compareFacesRequest = new CompareFacesRequest();
        amazonRekognition = AmazonRekognitionClientBuilder.defaultClient();
        amazonS3 = new AmazonS3Client();
        imageUtility = new ImageUtility();
        imageComparison = new ImageComparison(typeConverter, compareFacesRequest, amazonRekognition,
                compareFacesResult, compareFacesMatches);
        imageReference = new ImageReference(amazonS3);
    }

    /**
     * Another constructor for class ImageHandler.
     *
     * @param imageComparison This is the mocked reference of class ImageComparison
     * @param imageUtility    This is the mocked reference of class ImageUtility
     * @param imageReference  This is the mocked reference of class ImageReference
     */
    ImageHandler(ImageComparison imageComparison, ImageUtility imageUtility, ImageReference imageReference) {
        this.imageComparison = imageComparison;
        this.imageUtility = imageUtility;
        this.imageReference = imageReference;
    }

    /**
     * This method is used to handle the http request.
     *
     * @param images  This is the key-value pair which contains image and image id
     * @param context This is the context of hawk lambda runtime
     * @return String This returns actual resultant string
     */
    @Override
    public String handleRequest(Map<String, byte[]> images, Context context) {
        logger.info("Entry Point is hit successfully");
        final UserData userData = new UserData(images.get(IMAGE_ID), images.get(CLICKED_IMAGE));

        try {
            validateUserData(images);
        } catch (Exception exception) {
            return exception.getMessage();
        }

        logger.info("Image and RFID of employee is valid");

        CompletableFuture<String> faceMatchResult = imageReference.getImageFromS3(new String(userData.getRFID()))
                .thenCompose(reference_image ->
                        imageComparison.getSimilarity(userData.getClickedImage(), reference_image))
                .thenApply(imageThreshold ->
                        imageUtility.checkImageThreshold(imageThreshold))
                .exceptionally(throwable -> {
                    Throwable cause = throwable.getCause();
                    return cause.getMessage();
                });

        try {
            return faceMatchResult.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException interruptedException) {
            logger.severe(INTERRUPT_FLAG + interruptedException);
            return INTERRUPT_FLAG;
        } catch (ExecutionException executionException) {
            logger.severe(INTERRUPT_FLAG + executionException);
            return INTERRUPT_FLAG;
        } catch (TimeoutException exception) {
            logger.severe(TIMEOUT);
            return TIMEOUT;
        }
    }

    /**
     * This method validate user data.
     *
     * @param employeeData This is the key-value pair which contains image and image id
     * @return byte[] This returns byte stream data asynchronously.
     */
    public void validateUserData(Map<String, byte[]> employeeData) throws ValidationException {
        logger.info("validateUserData is hit successfully");
        Optional.ofNullable(employeeData.values()).ifPresent(userData -> userData.forEach(data -> {
            if (data == null)
                throw new ValidationException(EMPTY_IMAGE_PAYLOAD);
        }));
    }
}