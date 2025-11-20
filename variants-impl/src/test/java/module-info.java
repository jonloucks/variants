/**
 * module-impl tests
 */
module io.github.jonloucks.variants.impl.test {
    requires transitive io.github.jonloucks.contracts;
    requires transitive io.github.jonloucks.contracts.test;
    requires transitive io.github.jonloucks.variants.api;
    requires transitive io.github.jonloucks.variants.test;
    requires transitive io.github.jonloucks.variants.impl;

    uses io.github.jonloucks.variants.api.VariantsFactory;
    
    opens io.github.jonloucks.variants.impl.test to org.junit.platform.commons;
}