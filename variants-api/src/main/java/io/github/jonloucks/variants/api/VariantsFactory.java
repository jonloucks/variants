package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.AutoClose;
import io.github.jonloucks.contracts.api.AutoOpen;
import io.github.jonloucks.contracts.api.Contract;
import io.github.jonloucks.contracts.api.Repository;

import java.util.function.Consumer;

/**
 * Responsibility: Creating new instances of Variants
 */
public interface VariantsFactory {
    /**
     * Used to promise and claim the VariantsFactory implementation
     */
    Contract<VariantsFactory> CONTRACT = Contract.create(VariantsFactory.class);
    
    /**
     * Create a new instance of Variants
     * <p>
     *     Note: caller is responsible for calling {@link AutoOpen#open()} and calling
     *     the {@link AutoClose#close() when done}
     * </p>
     * @param config the Variants configuration for the new instance
     * @return the new Variants instance
     * @throws IllegalArgumentException if config is null or when configuration is invalid
     */
    Variants create(Variants.Config config);
    
    /**
     * Create a new instance of Variants
     *
     * @param builderConsumer the config builder consumer callback
     * @return the new Variants instance
     * @throws IllegalArgumentException if builderConsumer is null or when configuration is invalid
     */
    Variants create(Consumer<Variants.Config.Builder> builderConsumer);
    
    /**
     * Install all the requirements and promises to the given Contracts Environment.
     * Include Variants#CONTRACT which will private a unique
     *
     * @param config the Variants config
     * @param repository the repository to add requirements and promises to
     * @throws IllegalArgumentException if config is null, config is invalid, or repository is null
     */
    void install(Variants.Config config, Repository repository);
}
