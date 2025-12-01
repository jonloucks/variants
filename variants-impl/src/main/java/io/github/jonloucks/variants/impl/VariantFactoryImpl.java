package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Parsers;
import io.github.jonloucks.variants.api.Variant;
import io.github.jonloucks.variants.api.VariantFactory;
import io.github.jonloucks.variants.api.Variants;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.github.jonloucks.contracts.api.Checks.builderConsumerCheck;
import static io.github.jonloucks.contracts.api.Checks.configCheck;

/**
 * Responsibility: VariantFactory implementation
 */
final class VariantFactoryImpl implements VariantFactory {
    
    @Override
    public <T> Variant<T> createVariant(Consumer<Variant.Config.Builder<T>> builderConsumer) {
        final Consumer<Variant.Config.Builder<T>> validBuildConsumer = builderConsumerCheck(builderConsumer);
        final VariantBuilderImpl<T> variantBuilder = new VariantBuilderImpl<>();
        validBuildConsumer.accept(variantBuilder);
        return createVariant(variantBuilder);
    }
    
    @Override
    public <T> Variant<T> createVariant(BiConsumer<Variant.Config.Builder<T>, Parsers> builderConsumer) {
        final BiConsumer<Variant.Config.Builder<T>,Parsers> validBuildConsumer = builderConsumerCheck(builderConsumer);
        final Parsers parsers = config.contracts().claim(Parsers.CONTRACT);
        final VariantBuilderImpl<T> variantBuilder = new VariantBuilderImpl<>();
        validBuildConsumer.accept(variantBuilder, parsers);
        return createVariant(variantBuilder);
    }
    
    @Override
    public <T> Variant<T> createVariant(Variant.Config<T> config) {
        return new VariantImpl<>(config);
    }
    
    
    VariantFactoryImpl(Variants.Config config) {
        this.config = configCheck(config);
    }
    
    private final Variants.Config config;
}
