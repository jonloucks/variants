package io.github.jonloucks.variants.impl;

import io.github.jonloucks.contracts.api.AutoClose;
import io.github.jonloucks.contracts.api.Repository;
import io.github.jonloucks.variants.api.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static io.github.jonloucks.contracts.api.Checks.configCheck;
import static io.github.jonloucks.contracts.api.Checks.nullCheck;

final class VariantsImpl implements Variants {
    
    @Override
    public AutoClose open() {
        if (openState.compareAndSet(false, true)) {
            return realOpen();
        }
        return AutoClose.NONE;
    }
    
    VariantsImpl(Config config, Repository repository, boolean autoOpen) {
        this.config = configCheck(config);
        final Repository validRepository = nullCheck(repository, "Environment must be present.");
        this.closeRepository = autoOpen ? validRepository.open() : AutoClose.NONE;
    }
    
    private AutoClose realOpen() {
        variantFactory = config.contracts().claim(VariantFactory.CONTRACT);
        environmentFactory = config.contracts().claim(EnvironmentFactory.CONTRACT);
        return this::close;
    }

    private void close() {
        if (openState.compareAndSet(true, false)) {
            realClose();
        }
    }
    
    private void realClose() {
        closeRepository.close();
    }
    
    @Override
    public Environment createEnvironment(Environment.Config config) {
        return environmentFactory.createEnvironment(config);
    }
    
    @Override
    public Environment createEnvironment(Consumer<Environment.Config.Builder> builderConsumer) {
        return environmentFactory.createEnvironment(builderConsumer);
    }
    
    @Override
    public <T> Variant<T> createVariant(Consumer<Variant.Config.Builder<T>> builderConsumer) {
        return variantFactory.createVariant(builderConsumer);
    }
    
    @Override
    public <T> Variant<T> createVariant(Variant.Config<T> config) {
        return variantFactory.createVariant(config);
    }
    
    private final AutoClose closeRepository;
    private final AtomicBoolean openState = new AtomicBoolean();
    private final Config config;
    private VariantFactory variantFactory;
    private EnvironmentFactory environmentFactory;
}
