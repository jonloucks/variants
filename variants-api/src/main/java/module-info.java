/**
 * The API module for Variants
 */
module io.github.jonloucks.variants.api {
    requires transitive io.github.jonloucks.contracts.api;
    
    uses io.github.jonloucks.variants.api.VariantsFactory;
    
    exports io.github.jonloucks.variants.api;
}