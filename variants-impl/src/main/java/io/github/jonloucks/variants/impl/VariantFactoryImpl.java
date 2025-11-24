package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Variant;
import io.github.jonloucks.variants.api.VariantFactory;

import java.util.function.Consumer;

import static io.github.jonloucks.contracts.api.Checks.builderConsumerCheck;

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
    public <T> Variant<T> createVariant(Variant.Config<T> config) {
        return new VariantImpl<>(config);
    }
}
