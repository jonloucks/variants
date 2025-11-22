package io.github.jonloucks.variants.impl;

import io.github.jonloucks.contracts.api.GlobalContracts;
import io.github.jonloucks.variants.api.Checks;
import io.github.jonloucks.variants.api.GlobalVariants;


/**
 * Responsibility: Quickly validate runtime
 * Utility class instantiation protection
 * Test coverage not possible, java module protections in place
 */
public final class Stub {
    
    // conflicting standards.  100% code coverage vs throwing exception on instantiation of utility class.
    // Java modules protects agents invoking private methods.
    // There are unit tests that will fail if this constructor is not private
    private Stub() {
    }

    /**
     * Provides runtime validation
     */
    public static void validate() {
        Checks.validateVariants(GlobalContracts.getInstance(), GlobalVariants.getInstance());
    }
}
