package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.Checks;
import io.github.jonloucks.variants.api.VariantException;
import io.github.jonloucks.contracts.api.GlobalContracts;
import org.junit.jupiter.api.Test;

import static io.github.jonloucks.variants.test.Tools.withVariants;
import static io.github.jonloucks.contracts.test.Tools.assertThrown;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("CodeBlock2Expr")
public interface ValidateTests {
    
    @Test
    default void validate_WithNullContracts_Throws() {
        withVariants((contracts, variants) -> {
            assertThrown(IllegalArgumentException.class,
                () -> Checks.validateVariants(null, variants),
                "Contracts must be present.");
        });
    }
    
    @Test
    default void validate_WithNullVariants_Throws() {
        withVariants((contracts, variants) -> {
            assertThrown(IllegalArgumentException.class,
                () -> Checks.validateVariants(contracts, null),
                "Variants must be present.");
        });
    }
    
    @Test
    default void validate_WithVariantsClaimDifferent_Throws() {
        withVariants((contracts, variants) -> {
            assertThrown(VariantException.class,
                () -> Checks.validateVariants(GlobalContracts.getInstance(), variants),
                "Variants.CONTRACT claim is different.");
        });
    }
    
    @Test
    default void validate_Valid_Works() {
        withVariants((contracts, variants) -> {
            assertDoesNotThrow(() -> Checks.validateVariants(contracts, variants));
        });
    }
}
