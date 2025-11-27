package io.github.jonloucks.variants.api;

import java.util.Optional;
import java.util.ServiceLoader;

import static io.github.jonloucks.contracts.api.Checks.configCheck;
import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static java.util.Optional.ofNullable;

/**
 * Responsibility: Locating and creating the VariantsFactory for a deployment.
 */
public final class VariantsFactoryFinder {
    
    /**
     * Create a new Variants Factory Finder
     * @param config the Variants configuration
     * @throws IllegalArgumentException when arguments are null or invalid
     */
    public VariantsFactoryFinder(Variants.Config config) {
        this.config = configCheck(config);
    }
    
    /**
     * Find a Variants factory
     * @return the optional Variant Factory
     */
    public Optional<VariantsFactory> find() {
        final Optional<VariantsFactory> byReflection = createByReflection();
        return byReflection.isPresent() ? byReflection : createByServiceLoader();
    }
    
    private Optional<VariantsFactory> createByServiceLoader() {
        if (config.useServiceLoader()) {
            try {
                for (VariantsFactory factory : ServiceLoader.load(getServiceFactoryClass())) {
                    return Optional.of(factory);
                }
            } catch (Throwable ignored) {
            }
        }
        return Optional.empty();
    }
    
    private Class<? extends VariantsFactory> getServiceFactoryClass() {
        return nullCheck(config.serviceLoaderClass(), "Variants Service Loader class must be present.");
    }
    
    private Optional<VariantsFactory> createByReflection() {
        if (config.useReflection()) {
            return getReflectionClassName().map(this::createNewInstance);
        }
        return Optional.empty();
    }
    
    private VariantsFactory createNewInstance(String className) {
        try {
            return (VariantsFactory)Class.forName(className).getConstructor().newInstance();
        } catch (Throwable thrown) {
            return null;
        }
    }

    private Optional<String> getReflectionClassName() {
        return ofNullable(config.reflectionClassName()).filter(x -> !x.isEmpty());
    }
    
    private final Variants.Config config;
}
