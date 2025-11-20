package io.github.jonloucks.variants.test;

import io.github.jonloucks.contracts.api.Repository;
import io.github.jonloucks.contracts.test.BadContractsFactoryTests;
import io.github.jonloucks.variants.api.Variants;
import io.github.jonloucks.variants.api.VariantsFactory;

import java.util.function.Consumer;

/**
 * Used to introduce errors.
 * 1. Class is not public
 * 2. create throws an exception
 * 3. Constructor is not public
 * @see BadContractsFactoryTests
 */
final class BadVariantsFactory implements VariantsFactory {
    @Override
    public Variants create(Variants.Config config) {
        throw new UnsupportedOperationException("Not supported ever.");
    }
    
    @Override
    public Variants create(Consumer<Variants.Config.Builder> builderConsumer) {
        throw new UnsupportedOperationException("Not supported ever.");
    }
    
    @Override
    public void install(Variants.Config config, Repository repository) {
        throw new UnsupportedOperationException("Not supported ever.");
    }
    
    // constructor is purposely not public
    BadVariantsFactory() {
    }
}
