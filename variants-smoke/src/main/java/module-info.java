/**
 * Includes all components for the smoke app
 */
module io.github.jonloucks.variants.smoke {
    requires transitive io.github.jonloucks.contracts;
    requires transitive io.github.jonloucks.variants;
    
    uses io.github.jonloucks.contracts.api.ContractsFactory;
    uses io.github.jonloucks.variants.api.VariantsFactory;
    
    exports io.github.jonloucks.variants.smoke;
}