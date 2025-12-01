package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Variant;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.jonloucks.contracts.api.Checks.configCheck;
import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static java.util.Collections.unmodifiableList;

/**
 * Responsibility: Immutable Variant implementation.
 * @param <T> The type of Variant value
 */
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
        return of.apply(rawText);
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
        this.name = nullCheck(validConfig.getName(), "Optional name must be set.");
        this.description = nullCheck(validConfig.getDescription(), "Optional description must be set.");
        this.link = nullCheck(validConfig.getLink(), "Optional link must be set.");
        this.fallback = nullCheck(validConfig.getFallback(), "Optional fallback must be set.");
        this.of = nullCheck(validConfig.getOf(), "Of method must be set.");
    }
    
    // Opting out of the best practice of not using Optionals as instance variables
    // Reason:  Remove redundant operations checking nullability, which reduces memory and CPU usage
    // Is it safe? Yes, since they are only assigned internally after a null checks
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<String> name;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<String> description;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<T> fallback;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Variant<T>> link;
    private final Function<CharSequence,Optional<T>> of;
    private final List<String> keys;
}
