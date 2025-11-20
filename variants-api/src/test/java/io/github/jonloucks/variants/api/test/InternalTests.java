package io.github.jonloucks.variants.api.test;

import io.github.jonloucks.variants.api.GlobalVariants;
import org.junit.jupiter.api.Test;

import static io.github.jonloucks.contracts.test.Tools.assertInstantiateThrows;

public interface InternalTests {
    
    @Test
    default void api_InstantiateGlobalVariants_Throws() {
        assertInstantiateThrows(GlobalVariants.class);
    }
}
