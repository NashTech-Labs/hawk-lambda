package com.knoldus.hawk;

import java.util.concurrent.CompletableFuture;

public interface Comparison {
    CompletableFuture<Float> getSimilarity(byte[] clickedImage, byte[] referenceImage);
}


