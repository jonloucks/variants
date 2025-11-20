package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.ContractException;
import io.github.jonloucks.contracts.api.Contracts;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static io.github.jonloucks.contracts.api.Checks.validateContracts;

/**
 * Checks used internally and supported for external use.
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
    
    private Checks() {
    
    }
}
