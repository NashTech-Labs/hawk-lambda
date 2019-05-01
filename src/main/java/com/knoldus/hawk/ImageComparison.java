package com.knoldus.hawk;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.knoldus.hawk.exceptions.DataNotFoundException;
import com.knoldus.hawk.utilities.TypeConverter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static com.knoldus.hawk.constants.Constants.DEFAULT_IMAGE_SIMILARITY;
import static com.knoldus.hawk.constants.Constants.INVALID_IMAGE;

/**
 * The ImageComparison compare two faces.
 */
class ImageComparison implements Comparison {

    private static final Logger logger = Logger.getLogger(ImageComparison.class.getName());
    private TypeConverter typeConverter;
    private CompareFacesRequest compareFacesRequest;
    private AmazonRekognition amazonRekognition;
    private CompareFacesResult compareFacesResult;
    private List<CompareFacesMatch> compareFacesMatches;

    /**
     * Constructor for class ImageComparison.
     *
     * @param typeConverter       This is the instance of class TypeConverter
     * @param compareFacesRequest This is the instance of class CompareFacesRequest
     * @param amazonRekognition   This is the rekognition client of class AmazonRekognitionClientBuilder
     * @param compareFacesResult  This is the reference of class CompareFacesResult
     * @param compareFacesMatches This is the reference of class List of CompareFacesMatch
     */
    ImageComparison(TypeConverter typeConverter, CompareFacesRequest compareFacesRequest,
                    AmazonRekognition amazonRekognition, CompareFacesResult compareFacesResult,
                    List<CompareFacesMatch> compareFacesMatches) {
        this.typeConverter = typeConverter;
        this.compareFacesRequest = compareFacesRequest;
        this.amazonRekognition = amazonRekognition;
        this.compareFacesResult = compareFacesResult;
        this.compareFacesMatches = compareFacesMatches;
    }

    /**
     * This method is used to generate the request for image comparison and fetch image similarity.
     *
     * @param clickedImage   This is the byte[] of clicked image
     * @param referenceImage This is the byte[] of reference image
     * @return Float This returns similarity score of image comparison
     */
    @Override
    public CompletableFuture<Float> getSimilarity(byte[] clickedImage, byte[] referenceImage) {
        logger.info("getImageSimilarity is hit successfully");
        return CompletableFuture.supplyAsync(() -> {
            compareFacesRequest
                    .withSourceImage(new Image().withBytes(typeConverter.byteArrayToByteBuffer(clickedImage)))
                    .withTargetImage(new Image().withBytes(typeConverter.byteArrayToByteBuffer(referenceImage)));
            return amazonRekognition.compareFaces(compareFacesRequest);
        }).thenApply(compareFacesResult ->
                compareFacesResult.getFaceMatches().stream()
                        .map(CompareFacesMatch::getSimilarity)
                        .filter(imageMatch -> !imageMatch.equals(DEFAULT_IMAGE_SIMILARITY))
                        .findFirst()
                        .orElse(DEFAULT_IMAGE_SIMILARITY))
                .exceptionally(throwable -> {
                    throw new DataNotFoundException(INVALID_IMAGE);
                });
    }
}
