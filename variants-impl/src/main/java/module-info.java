/**
 * The implementation module for Variants
 */
module io.github.jonloucks.variants.impl {
    requires transitive io.github.jonloucks.contracts.api;
    requires transitive io.github.jonloucks.variants.api;
    
    exports io.github.jonloucks.variants.impl;
    
    opens io.github.jonloucks.variants.impl to io.github.jonloucks.variants.api;
    
    provides io.github.jonloucks.variants.api.VariantsFactory with io.github.jonloucks.variants.impl.VariantsFactoryImpl;
}