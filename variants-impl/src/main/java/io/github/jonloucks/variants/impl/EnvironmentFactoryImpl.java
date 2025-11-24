package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Environment;
import io.github.jonloucks.variants.api.EnvironmentFactory;

import java.util.function.Consumer;

import static io.github.jonloucks.contracts.api.Checks.builderConsumerCheck;

/**
 * Responsibility: EnvironmentFactory implementation
 */
final class EnvironmentFactoryImpl implements EnvironmentFactory {
    @Override
    public Environment createEnvironment(Environment.Config config) {
        return new EnvironmentImpl(config);
    }
    
    @Override
    public Environment createEnvironment(Consumer<Environment.Config.Builder> builderConsumer) {
        final Consumer<Environment.Config.Builder> validBuilderConsumer = builderConsumerCheck(builderConsumer);
        final EnvironmentBuilderImpl builder = new EnvironmentBuilderImpl();
        validBuilderConsumer.accept(builder);
        return new EnvironmentImpl(builder);
    }
}
