package io.github.jonloucks.variants.test;

import io.github.jonloucks.contracts.api.AutoClose;
import io.github.jonloucks.variants.api.Environment;
import io.github.jonloucks.variants.api.Variant;
import io.github.jonloucks.variants.api.Variants;
import io.github.jonloucks.variants.api.VariantsFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;
import java.util.function.Consumer;

import static io.github.jonloucks.contracts.test.Tools.*;
import static io.github.jonloucks.variants.test.Tools.*;
import static org.junit.jupiter.api.Assertions.*;

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
    
    @Test
    default void variants_createVariant_WithNullConfig_Throws() {
        withVariants((contracts, variants) -> {
            assertThrown(IllegalArgumentException.class,
                () -> variants.createVariant((Variant.Config<String>) null),
                "Config must be present.");
        });
    }
    
    @Test
    default void variants_createVariant_WithConfig_Works() {
        withVariants((contracts, variants) -> {
            final Variant.Config<Integer> config = new Variant.Config<>() {
                @Override
                public Optional<Integer> getFallback() {
                    return Optional.of(7);
                }
            };
            final Variant<Integer> variant =  variants.createVariant(config);
            
            assertObject(variant);
            assertTrue(variant.getFallback().isPresent(), "Fallback value must be present.");
            assertEquals(7, variant.getFallback().get());
        });
    }
    
    @Test
    default void variants_createVariant_WithNullConfigBuilder_Throws() {
        withVariants((contracts, variants) -> {
            assertThrown(IllegalArgumentException.class,
                () -> variants.createVariant((Consumer<Variant.Config.Builder<String>>) null),
                "Builder consumer must be present.");
        });
    }
    
    @Test
    default void variants_createVariant_WithConfigBuilder_Works() {
        withVariants((contracts, variants) -> {
            final Variant<Integer> variant =  variants.createVariant(b -> b.fallback(() -> 7));
            
            assertObject(variant);
            assertTrue(variant.getFallback().isPresent(), "Fallback value must be present.");
            assertEquals(7, variant.getFallback().get());
        });
    }
    
    @Test
    default void variants_createEnvironment_WithNullConfig_Throws() {
        withVariants((contracts, variants) -> {
            assertThrown(IllegalArgumentException.class,
                () -> variants.createEnvironment((Environment.Config) null),
                "Config must be present.");
        });
    }
    
    @Test
    default void variants_createEnvironment_WithConfig_Works() {
        withVariants((contracts, variants) -> {
            final Environment.Config config = new Environment.Config() {};
            final Environment environment = variants.createEnvironment(config);
            final Variant<Integer> variant = variants.createVariant(b -> b.fallback(() -> 7));
            
            assertObject(environment);
            assertTrue(environment.findVariance(variant).isPresent(), "Variance must be present.");
            assertEquals(7, environment.getVariance(variant), "Variance must match.");
        });
    }
    
    @Test
    default void variants_createEnvironment_WithNullConfigBuilder_Throws() {
        withVariants((contracts, variants) -> {
            assertThrown(IllegalArgumentException.class,
                () -> variants.createEnvironment((Consumer<Environment.Config.Builder>) null),
                "Builder consumer must be present.");
        });
    }
    
    @Test
    default void variants_createEnvironment_WithConfigBuilder_Works() {
        withVariants((contracts, variants) -> {
            final Environment environment = variants.createEnvironment(b -> {});
            final Variant<Integer> variant = variants.createVariant(b -> b.fallback(() -> 7));
            
            assertObject(environment);
            assertTrue(environment.findVariance(variant).isPresent(), "Variance must be present.");
            assertEquals(7, environment.getVariance(variant), "Variance must match.");
        });
    }
}
