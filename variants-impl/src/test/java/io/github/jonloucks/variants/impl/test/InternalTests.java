package io.github.jonloucks.variants.impl.test;

import io.github.jonloucks.variants.impl.Stub;
import org.junit.jupiter.api.Test;

import static io.github.jonloucks.contracts.test.Tools.assertInstantiateThrows;

public interface InternalTests {
    
    @Test
    default void variant_create_WithDefaultValues_Works() {
        assertInstantiateThrows(Stub.class);
    }
    
    @Test
    default void internalTests() {
        Stub.validate();
    }
}
