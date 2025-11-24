package io.github.jonloucks.variants.impl;

import java.util.Optional;
import java.util.function.Supplier;

final class Internal {
    /**
     * Utility class instantiation protection
     * Test coverage not possible, java module protections in place
     */
    private Internal() {
        // conflicting standards.  100% code coverage vs throwing exception on instantiation of utility class.
        // Java modules protects agents invoking private methods.
        // There are unit tests that will fail if this constructor is not private
    }
    
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // if language level 9 this can be replaced with Optional.or
    static <T> Optional<T> optionalOr(Optional<T> optional, Supplier<? extends Optional<? extends T>> supplier) {
        if (optional.isPresent()) {
            return optional;
        } else {
            return supplier.get().map(v -> v);
        }
    }
}
