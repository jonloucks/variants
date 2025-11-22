package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.Contract;

import java.util.function.Consumer;

public interface VariantFactory {
    Contract<VariantFactory> CONTRACT = Contract.create(VariantFactory.class, b -> b.name("Variant Factory"));
    
    <T> Variant<T> createVariant(Consumer<Variant.Config.Builder<T>> builderConsumer);
    
    <T> Variant<T> createVariant(Variant.Config<T> config);
}
