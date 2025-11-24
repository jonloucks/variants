package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Environment;
import io.github.jonloucks.variants.api.VariantSource;

import java.util.ArrayList;
import java.util.List;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;

final class EnvironmentBuilderImpl implements Environment.Config.Builder {
    
    @Override
    public EnvironmentBuilderImpl addSource(VariantSource source) {
        final VariantSource validVariantSource = nullCheck(source, "Source must be present.");
        this.sources.add(validVariantSource);
        return this;
    }
    
    @Override
    public List<VariantSource> getSources() {
        return sources;
    }
    
    EnvironmentBuilderImpl() {
    }
    
    
    private final List<VariantSource> sources = new ArrayList<>();
}
