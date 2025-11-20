package io.github.jonloucks.variants;

import io.github.jonloucks.variants.api.Variants;
import io.github.jonloucks.variants.api.VariantException;
import io.github.jonloucks.variants.api.GlobalVariants;
import io.github.jonloucks.contracts.api.ContractException;
import io.github.jonloucks.contracts.api.Contracts;
import io.github.jonloucks.contracts.api.GlobalContracts;

import static io.github.jonloucks.variants.api.Checks.validateVariants;

/**
 * A placeholder class to make sure dependencies are correct for api and implementation.
 */
public final class Stub {
    
    /**
     * Utility class instantiation protection
     * Test coverage not possible, java module protections in place
     */
    private Stub() {
        // conflicting standards.  100% code coverage vs throwing exception on instantiation of utility class.
        // Java modules protects agents invoking private methods.
        // There are unit tests that will fail if this constructor is not private
    }
    
    /**
     * Quickly validates Global Contracts and Variants
     *
     * @throws ContractException when invalid
     * @throws VariantException when invalid
     * @throws IllegalArgumentException when invalid
     */
    public static void validate() {
        validate(GlobalContracts.getInstance(), GlobalVariants.getInstance());
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
    public static void validate(Contracts contracts, Variants variants) {
        validateVariants(contracts, variants);
    }
}
