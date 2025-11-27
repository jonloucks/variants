package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.Contract;

import java.util.function.Consumer;

/**
 * Responsibility: The creation of an Environment
 */
public interface EnvironmentFactory {
    /**
     * The Contract for the EnvironmentFactory
     */
    Contract<EnvironmentFactory> CONTRACT = Contract.create(EnvironmentFactory.class,
        b -> b.name("Environment Factory"));
    
    /**
     * Create an Environment by configuration
     *
     * @param config the configuration
     * @return the new Environment
     * @throws IllegalArgumentException when arguments are null or invalid
     */
    Environment createEnvironment(Environment.Config config);
    
    /**
     * Create an Environment by configuration builder callback
     *
     * @param builderConsumer receives the configuration builder
     * @return the new Environment
     * @throws IllegalArgumentException when arguments are null or invalid
     */
    Environment createEnvironment(Consumer<Environment.Config.Builder> builderConsumer);
}
