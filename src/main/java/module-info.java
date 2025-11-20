/**
 * Includes all default components of the Concurrency library needed for a working deployment.
 */
module io.github.jonloucks.variants {
    requires transitive io.github.jonloucks.variants.api;
    requires transitive io.github.jonloucks.variants.impl;
    
    exports io.github.jonloucks.variants;
}