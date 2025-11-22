/**
 * Module to run tests on the test tools
 */
module io.github.jonloucks.variants.test.run {
    requires transitive io.github.jonloucks.variants.test;
    
    uses io.github.jonloucks.variants.api.VariantsFactory;
    
    opens io.github.jonloucks.variants.test.run to org.junit.platform.commons;
}