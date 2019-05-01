package com.knoldus.hawk;

import com.google.inject.AbstractModule;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(Comparison.class).to(ImageComparison.class);
        bind(Reference.class).to(ImageReference.class);
    }
}