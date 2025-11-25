package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.AutoClose;

import java.util.Optional;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;

/**
 * Responsibility: Globally shared Variants
 */
public final class GlobalVariants {
    
    /**
     * Return the global instance of Contracts
     * @return the instance
     */
    public static Variants getInstance() {
        return INSTANCE.variants;
    }
    
    /**
     * Create a new Variants instance for customized deployments.
     * Note: GlobalVariants has everything feature, this api
     * allows creation of more than once instance of Variants.
     *
     * @param config the Variants configuration
     * @return the new Variants
     * @see VariantsFactory#create(Variants.Config)
     * Note: Services created from this method are destink any that used internally
     * <p>
     * Caller is responsible for invoking open() before use and close when no longer needed
     * </p>
     * @throws IllegalArgumentException if config is null or invalid
     */
    public static Variants createVariants(Variants.Config config) {
        final VariantsFactory factory = findVariantsFactory(config)
            .orElseThrow(() -> new VariantException("Variants factory must be present."));
   
        return nullCheck(factory.create(config), "Variants could not be created.");
    }
    
    /**
     * Finds the VariantsFactory implementation
     *
     * @param config the configuration used to find the factory
     * @return the factory if found
     * @throws IllegalArgumentException if config is null or invalid
     */
    public static Optional<VariantsFactory> findVariantsFactory(Variants.Config config) {
        return new VariantsFactoryFinder(config).find();
    }
    
    private GlobalVariants() {
        this.variants = createVariants(Variants.Config.DEFAULT);
        this.close = variants.open();
    }
    
    private static final GlobalVariants INSTANCE = new GlobalVariants();
    
    private final Variants variants;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final AutoClose close;
}
