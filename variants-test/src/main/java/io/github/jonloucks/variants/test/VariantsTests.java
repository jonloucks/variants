package io.github.jonloucks.variants.test;

import io.github.jonloucks.contracts.api.AutoClose;
import io.github.jonloucks.variants.api.Variants;
import io.github.jonloucks.variants.api.VariantsFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static io.github.jonloucks.contracts.test.Tools.*;
import static io.github.jonloucks.variants.test.Tools.*;

@SuppressWarnings("CodeBlock2Expr")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public interface VariantsTests {
    
    @Test
    default void variants_isValidObject() {
        withVariants((contracts, variants) -> {
            assertObject(variants);
        });
    }
    
    @Test
    default void variants_open_isIdempotent() {
        withContracts(contracts -> {
            final VariantsFactory factory = getVariantsFactory();
            final Variants variants = factory.create(b -> {
                b.contracts(contracts);
            });
            try (AutoClose closeVariants = variants.open()) {
                ignore(closeVariants);
                try (AutoClose closeVariants2 = variants.open()) {
                    ignore(closeVariants2);
                }
                // add Variants method to test
                assertObject(variants);
            }
        });
    }
    
    @Test
    default void variants_close_isIdempotent() {
        withContracts(contracts -> {
            final VariantsFactory factory = getVariantsFactory();
            final Variants variants = factory.create(b -> {
                b.contracts(contracts);
            });
            try (AutoClose closeVariants = variants.open()) {
                assertIdempotent(closeVariants);
            }
        });
    }
}
