package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.ContractException;
import io.github.jonloucks.contracts.api.Contracts;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static io.github.jonloucks.contracts.api.Checks.validateContracts;

/**
 * Responsibility: Common argument checking and validation
 */
public final class Checks {
    
    /**
     * Check if given Variants is not null or invalid
     *
     * @param variants the Variants to check
     * @return a valid Variants
     * @throws IllegalArgumentException when invalid
     */
    public static Variants variantsCheck(Variants variants) {
        return nullCheck(variants, "Variants must be present.");
    }
    
    /**
     * Quickly validates a Contracts and Variants
     *
     * @param contracts the Contracts to validate
     * @param variants the Variants to validate
     *
     * @throws ContractException when invalid
     * @throws VariantException when invalid
     * @throws IllegalArgumentException when invalid
     */
    public static void validateVariants(Contracts contracts, Variants variants) {
        final Variants validVariants = variantsCheck(variants);
        validateContracts(contracts);
        
        final Variants promisedVariants = contracts.claim(Variants.CONTRACT);
        if (validVariants != promisedVariants) {
            throw new VariantException("Variants.CONTRACT claim is different.");
        }

        contracts.claim(VariantsFactory.CONTRACT);
    }
    
    /**
     * Check if given Variant is not null or invalid
     *
     * @param variant the Variant to check
     * @return a valid Variant
     * @param <T> the Variant value type
     * @throws IllegalArgumentException when invalid
     */
    public static <T> Variant<T> variantCheck(Variant<T> variant) {
        return nullCheck(variant, "Variant must be present.");
    }
    
    /**
     * Check if given key is not null or invalid
     *
     * @param key the key to check
     * @return a valid key
     * @throws IllegalArgumentException when arguments are null or invalid
     */
    public static String keyCheck(String key) {
        return nullCheck(key, "Key must be present.");
    }
    
    /**
     * Check if given is not null or invalid
     *
     * @param keys the keys to check
     * @return valid keys
     * @param <T> the type of keys
     * @throws IllegalArgumentException when arguments are null or invalid
     */
    public static <T> T keysCheck(T keys) {
        return nullCheck(keys, "Keys must be present.");
    }
    
    private Checks() {
    
    }
}
