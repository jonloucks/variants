package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Variant;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.jonloucks.contracts.api.Checks.configCheck;
import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static java.util.Collections.unmodifiableList;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
final class VariantImpl<T> implements Variant<T> {
    @Override
    public List<String> getKeys() {
        return this.keys;
    }
    
    @Override
    public Optional<String> getName() {
        return this.name;
    }
    
    @Override
    public Optional<String> getDescription() {
        return this.description;
    }
    
    @Override
    public Optional<T> getFallback() {
        return this.fallback;
    }
    
    @Override
    public Optional<Variant<T>> getLink() {
        return this.link;
    }
    
    @Override
    public Optional<T> of(String value) {
        return Optional.ofNullable(parser.apply(value));
    }
    
    VariantImpl(Variant.Config<T> config) {
        final Variant.Config<T> validConfig = configCheck(config);
        if (validConfig.getKeys().isEmpty()) {
            throw new IllegalArgumentException("Keys must be present.");
        }
        this.keys = unmodifiableList(validConfig.getKeys());
        this.name = validConfig.getName().isPresent() ? validConfig.getName() : Optional.of(keys.get(0));
        this.description = validConfig.getDescription();
        this.link = validConfig.getLink();
        this.fallback = validConfig.getFallback();
        this.parser = nullCheck(validConfig.getParser(), "Parser should be present.");
    }
    
    private final List<String> keys;
    private final Optional<String> name;
    private final Optional<String> description;
    private final Optional<T> fallback;
    private final Optional<Variant<T>> link;
    private final Function<CharSequence,T> parser;
}
