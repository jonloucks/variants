package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Variant;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.jonloucks.contracts.api.Checks.configCheck;
import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

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
    public Optional<T> of(CharSequence rawText) {
        if (ofNullable(rawText).isPresent()) {
            return parser.map(p -> p.apply(rawText));
        }
        return Optional.empty();
    }
    
    @Override
    public String toString() {
        if (getName().isPresent()) {
            return "Variant[name=" + getName().get() + "]";
        } else {
            return "Variant[]";
        }
    }
    
    VariantImpl(Variant.Config<T> config) {
        final Variant.Config<T> validConfig = configCheck(config);
        this.keys = unmodifiableList(validConfig.getKeys());
        this.name = validConfig.getName();
        this.description = validConfig.getDescription();
        this.link = validConfig.getLink();
        this.fallback = validConfig.getFallback();
        this.parser = validConfig.getParser();
        if (!keys.isEmpty() && !validConfig.getParser().isPresent()) {
            throw new IllegalArgumentException("Parser is required when keys are present.");
        }
    }
    
    private final List<String> keys;
    private final Optional<String> name;
    private final Optional<String> description;
    private final Optional<T> fallback;
    private final Optional<Variant<T>> link;
    private final Optional<Function<CharSequence,T>> parser;
}
