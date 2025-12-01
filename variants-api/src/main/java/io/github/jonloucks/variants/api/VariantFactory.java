package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.Contract;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Responsibility: The creation of a Variant
 */
public interface VariantFactory {
    /**
     * The Contract for the VariantFactory
     */
    Contract<VariantFactory> CONTRACT = Contract.create(VariantFactory.class, b -> b.name("Variant Factory"));
    
    /**
     * Create a Variant by configuration builder callback
     *
     * @param builderConsumer receives the configuration builder
     * @return the new Variant
     * @param <T> the Variant value type
     * @throws IllegalArgumentException when arguments are null or invalid
     */
    <T> Variant<T> createVariant(Consumer<Variant.Config.Builder<T>> builderConsumer);
    
    
    /**
     * Create a Variant by configuration builder callback
     *
     * @param builderConsumer receives the configuration builder
     * @return the new Variant
     * @param <T> the Variant value type
     * @throws IllegalArgumentException when arguments are null or invalid
     */
    <T> Variant<T> createVariant(BiConsumer<Variant.Config.Builder<T>, Parsers> builderConsumer);
    
    /**
     *
     * @param config the configuration for creating the Variant
     * @return the new Variant
     * @param <T> the Variant value type
     * @throws IllegalArgumentException when arguments are null or invalid
     */
    <T> Variant<T> createVariant(Variant.Config<T> config);
}
