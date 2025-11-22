package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.Variants;
import io.github.jonloucks.variants.api.VariantsFactoryFinder;
import org.junit.jupiter.api.Test;

import static io.github.jonloucks.contracts.test.Tools.assertThrown;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public interface VariantsFactoryFinderTests {
    
    @Test
    default void variantsFactoryFinder_WithNullConfig_Throws() {
        assertThrown(IllegalArgumentException.class,
            () -> new VariantsFactoryFinder(null),
            "Config must be present.");
    }
    
    @Test
    default void variantsFactoryFinder_WithConfig_DoesNotThrow() {
        final Variants.Config config = new Variants.Config(){
            @Override
            public boolean useReflection() {
                return false;
            }
        };
        final VariantsFactoryFinder finder = new VariantsFactoryFinder(config);
        
        assertNotNull(finder.find());
    }
}
