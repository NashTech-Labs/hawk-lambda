package com.knoldus.hawk;

import com.knoldus.hawk.exceptions.DataNotFoundException;

import java.util.concurrent.CompletableFuture;

public interface Reference {
    CompletableFuture<byte []> getImageFromS3(String referenceImageKey) throws DataNotFoundException;
}
