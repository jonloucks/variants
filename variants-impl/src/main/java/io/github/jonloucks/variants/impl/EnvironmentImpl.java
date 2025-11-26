package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Environment;
import io.github.jonloucks.variants.api.Variant;
import io.github.jonloucks.variants.api.VariantSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.jonloucks.contracts.api.Checks.configCheck;

/**
 * Responsibility: Environment implementation
 */
final class EnvironmentImpl implements Environment {

    @Override
    public <T> Optional<T> findVariance(Variant<T> variant) {
        return new FindVariantImpl<>(sources, variant).findVariance();
    }
    
    EnvironmentImpl(Environment.Config config) {
        sources.addAll(configCheck(config).getSources());
    }
    
    private final List<VariantSource> sources = new ArrayList<>();
}
