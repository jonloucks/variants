package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Variant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static io.github.jonloucks.variants.impl.Internal.optionalOr;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

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
    public VariantBuilderImpl<T> keys(String... keys) {
        final String[] validKeys = nullCheck(keys, "Keys must be present");
        this.keys.addAll(asList(validKeys));
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
    
    @Override
    public String toString() {
        return ofNullable(name).orElse("***");
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
