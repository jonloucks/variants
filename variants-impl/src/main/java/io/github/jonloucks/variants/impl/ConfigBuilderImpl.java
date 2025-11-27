package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Variants;
import io.github.jonloucks.variants.api.VariantsFactory;
import io.github.jonloucks.contracts.api.Contracts;

import static io.github.jonloucks.contracts.api.Checks.contractsCheck;
import static io.github.jonloucks.contracts.api.Checks.nullCheck;

/**
 * Responsibility: Variants.Config.Builder implementation
 */
final class ConfigBuilderImpl implements Variants.Config.Builder {
    @Override
    public Builder useReflection(boolean useReflection) {
        this.useReflection = useReflection;
        return this;
    }
    
    @Override
    public Builder useServiceLoader(boolean useServiceLoader) {
        this.useServiceLoader = useServiceLoader;
        return this;
    }
    
    @Override
    public Builder contracts(Contracts contracts) {
        this.contracts = contractsCheck(contracts);
        return this;
    }

    @Override
    public Builder reflectionClassName(String reflectionClassName) {
        this.reflectionClassName = nullCheck(reflectionClassName, "Reflection class name must be present.");
        return this;
    }
    
    @Override
    public Builder serviceLoaderClass(Class<? extends VariantsFactory> serviceLoaderClass) {
        this.serviceLoaderClass = nullCheck(serviceLoaderClass, "Service loader class must be present.");
        return this;
    }
    
    @Override
    public boolean useReflection() {
        return useReflection;
    }
    
    @Override
    public String reflectionClassName() {
        return reflectionClassName;
    }
    
    @Override
    public boolean useServiceLoader() {
        return useServiceLoader;
    }
    
    @Override
    public Class<? extends VariantsFactory> serviceLoaderClass() {
        return serviceLoaderClass;
    }
    
    @Override
    public Contracts contracts() {
        return contracts;
    }

    ConfigBuilderImpl() {
    
    }
    
    private boolean useReflection = DEFAULT.useReflection();
    private boolean useServiceLoader = DEFAULT.useServiceLoader();
    private Contracts contracts = DEFAULT.contracts();
    private String reflectionClassName = DEFAULT.reflectionClassName();
    private Class<? extends VariantsFactory> serviceLoaderClass = DEFAULT.serviceLoaderClass();
}
