/**
 * The Test module for Concurrency
 */
module io.github.jonloucks.variants.test {
    requires transitive io.github.jonloucks.variants.api;
    requires transitive io.github.jonloucks.contracts.api;
    requires transitive io.github.jonloucks.contracts.test;
    requires org.junit.jupiter.api;
    
    opens io.github.jonloucks.variants.test to org.junit.platform.commons;
    exports io.github.jonloucks.variants.test;
}