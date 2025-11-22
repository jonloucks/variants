package io.github.jonloucks.variants.test;

final class Internal {

    /**
     * Utility class instantiation protection
     * Test coverage not possible, java module protections in place
     */
    private Internal() {
        throw new AssertionError("Utility class instantiation.");
    }
}
