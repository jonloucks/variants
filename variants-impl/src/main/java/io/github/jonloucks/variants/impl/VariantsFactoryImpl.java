package io.github.jonloucks.variants.impl;

import io.github.jonloucks.contracts.api.BindStrategy;
import io.github.jonloucks.variants.api.*;
import io.github.jonloucks.contracts.api.Promisor;
import io.github.jonloucks.contracts.api.Repository;

import java.util.function.Consumer;

import static io.github.jonloucks.contracts.api.BindStrategy.IF_NOT_BOUND;
import static io.github.jonloucks.contracts.api.Checks.*;
import static io.github.jonloucks.contracts.api.GlobalContracts.lifeCycle;
import static io.github.jonloucks.contracts.api.GlobalContracts.singleton;

/**
 * Responsibility: Creation of Variants
 * Opt-in construction via reflection, ServiceLoader or directly.
 */
public final class VariantsFactoryImpl implements VariantsFactory {
    
    /**
     * Publicly accessible constructor as an entry point into this library.
     * It can be invoked via reflection, ServiceLoader or directly.
     */
    public VariantsFactoryImpl() {
    }
    
    @Override
    public Variants create(Variants.Config config) {
        final Variants.Config validConfig = configCheck(config);
        final Repository repository = validConfig.contracts().claim(Repository.FACTORY).get();
        
        installCore(validConfig, repository);
        
        final VariantsImpl variants = new VariantsImpl(validConfig, repository, true);
        repository.keep(Variants.CONTRACT, () -> variants, IF_NOT_BOUND);
        return variants;
    }
    
    @Override
    public Variants create(Consumer<Variants.Config.Builder> builderConsumer) {
        final Consumer<Variants.Config.Builder> validBuilderConsumer = builderConsumerCheck(builderConsumer);
        final ConfigBuilderImpl builder = new ConfigBuilderImpl();
        
        validBuilderConsumer.accept(builder);
        
        return create(builder);
    }
    
    @Override
    public void install(Variants.Config config, Repository repository) {
        final Variants.Config validConfig = configCheck(config);
        final Repository validRepository = nullCheck(repository, "Repository must be present.");
        
        installCore(validConfig, validRepository);
        
        final Promisor<Variants> variantsPromisor = lifeCycle(() -> new VariantsImpl(validConfig, validRepository, false));
        
        validRepository.keep(Variants.CONTRACT, variantsPromisor, IF_NOT_BOUND);
    }
    
    @SuppressWarnings("unused")
    private void installCore(Variants.Config config, Repository repository) {
        repository.require(Repository.FACTORY);
        
        final BindStrategy strategy = IF_NOT_BOUND;
        
        repository.keep(Variants.Config.Builder.FACTORY, singleton(() -> ConfigBuilderImpl::new), strategy);
        repository.keep(VariantsFactory.CONTRACT, lifeCycle(VariantsFactoryImpl::new), strategy);
        repository.keep(VariantFactory.CONTRACT, singleton(() -> new VariantFactoryImpl(config)), strategy);
        repository.keep(Parsers.CONTRACT, singleton(ParsersImpl::new), strategy);
        repository.keep(EnvironmentFactory.CONTRACT, singleton(EnvironmentFactoryImpl::new), strategy);
    }
}
