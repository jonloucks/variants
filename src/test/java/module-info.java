/**
 * Includes all default components of the Concurrency library needed for a working deployment.
 */
module io.github.jonloucks.variants.runtests {
    requires transitive io.github.jonloucks.variants.api;
    requires transitive io.github.jonloucks.variants.impl;
    requires transitive io.github.jonloucks.variants.test;
    requires transitive io.github.jonloucks.variants;
    
    opens io.github.jonloucks.variants.runtests to org.junit.platform.commons;
    
    exports io.github.jonloucks.variants.runtests;
}