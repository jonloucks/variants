package io.github.jonloucks.variants.impl.test;

import io.github.jonloucks.variants.impl.Stub;
import org.junit.jupiter.api.Test;

public interface InternalTests {
    
    @Test
    default void internalTests() {
        Stub.validate();
    }
}
