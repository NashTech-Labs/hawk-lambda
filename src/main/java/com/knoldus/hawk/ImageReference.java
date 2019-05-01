package com.knoldus.hawk;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.util.IOUtils;
import com.knoldus.hawk.exceptions.DataNotFoundException;
import com.knoldus.hawk.exceptions.S3Exception;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static com.knoldus.hawk.constants.Constants.PARSING_ERROR;
import static com.knoldus.hawk.constants.Constants.REFERENCE_BUCKET_NAME;
import static com.knoldus.hawk.constants.Constants.REF_IMAGE_NOT_FOUND;


/**
 * The ImageReference gives reference image from S3.
 */
class ImageReference implements Reference {
    private static final Logger logger = Logger.getLogger(ImageReference.class.getName());
    private AmazonS3 s3Client;

    /**
     * Constructor for class ImageReference.
     *
     * @param s3Client This is the instance of class AmazonS3Client
     */
    ImageReference(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * This method is used to get reference image from S3 bucket.
     *
     * @param referenceImageKey This is image id
     * @return byte[] This return reference image in byte[]
     */
    @Override
    public CompletableFuture<byte[]> getImageFromS3(String referenceImageKey) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("getImageFromS3 is hit successfully");
            return new GetObjectRequest(REFERENCE_BUCKET_NAME, referenceImageKey);
        }).thenApply(objectRequest ->
                s3Client.getObject(objectRequest)
        ).thenApply(s3Object -> {
            try {
                return IOUtils.toByteArray(s3Object.getObjectContent());
            } catch (IOException exception) {
                logger.severe(PARSING_ERROR);
                return PARSING_ERROR.getBytes();
            }
        }).exceptionally(throwable -> {
            Throwable cause = throwable.getCause();
            if (cause instanceof AmazonS3Exception) {
                throw new DataNotFoundException(REF_IMAGE_NOT_FOUND);
            } else {
                throw new S3Exception(cause.getMessage());
            }
        });
    }
}
