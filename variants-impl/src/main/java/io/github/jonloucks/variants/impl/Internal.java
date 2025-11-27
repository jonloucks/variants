package io.github.jonloucks.variants.impl;

import java.util.Optional;
import java.util.function.Supplier;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;

/**
 * Responsibility: Internal shared utilities
 */
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
    
    /**
     * Alternative for `Optional.or`
     * if language level 9 this can be replaced with Optional.or
     * @param optional the optional
     * @param supplier the optional supplier to be executed if first optional is empty
     * @return the logical or of the two optionals
     * @param <T> the Optional value type
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T> Optional<T> optionalOr(Optional<T> optional, Supplier<? extends Optional<? extends T>> supplier) {
        final Optional<T> validOptional = nullCheck(optional, "Optional must be present.");
        if (validOptional.isPresent()) {
            return validOptional;
        } else {
            return supplier.get().map(v -> v);
        }
    }
}
