package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Variant;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static io.github.jonloucks.variants.api.Checks.keysCheck;
import static io.github.jonloucks.variants.impl.Internal.optionalOr;
import static java.util.Optional.ofNullable;

/**
 * Responsibility: Variant.Config.Builder implementation
 * @param <T> the value type of Variant
 */
final class VariantBuilderImpl<T> implements Variant.Config.Builder<T> {
    
    @Override
    public VariantBuilderImpl<T> name(String name) {
        this.name = name;
        return this;
    }
    
    @Override
    public VariantBuilderImpl<T> parser(Function<CharSequence, T> parser) {
        this.parser = parser;
        return this;
    }
    
    @Override
    public VariantBuilderImpl<T> description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
    public VariantBuilderImpl<T> keys(Collection<String> keys) {
        this.keys.addAll(keysCheck(keys));
        return this;
    }
    
    @Override
    public VariantBuilderImpl<T> fallback(Supplier<T> fallback) {
        this.fallback = nullCheck(fallback, "Fallback must be present.");
        return this;
    }
    
    @Override
    public VariantBuilderImpl<T> link(Variant<T> link) {
        this.link = link;
        return this;
    }
    
    @Override
    public Optional<String> getName() {
        return optionalOr(ofNullable(name), () -> keys.stream().findFirst());
    }
    
    @Override
    public Optional<String> getDescription() {
        return ofNullable(description);
    }
    
    @Override
    public Optional<T> getFallback() {
        return ofNullable(fallback.get());
    }
    
    @Override
    public Optional<Variant<T>> getLink() {
        return ofNullable(link);
    }
    
    @Override
    public List<String> getKeys() {
        return new ArrayList<>(keys);
    }
    
    @Override
    public Optional<Function<CharSequence, T>> getParser() {
        return ofNullable(parser);
    }
    
    VariantBuilderImpl() {
    }
    
    private String name;
    private String description;
    private Supplier<T> fallback = () -> null;
    private final LinkedHashSet<String> keys = new LinkedHashSet<>() ;
    private Function<CharSequence,T> parser;
    private Variant<T> link;
}
