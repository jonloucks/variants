package io.github.jonloucks.variants.api;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Variant<T> {
    
    default List<String> getKeys() {
        return Collections.emptyList();
    }
    
    default Optional<String> getName() {
        return Optional.empty();
    }
    
    default Optional<String> getDescription() {
        return Optional.empty();
    }
    
    default Optional<T> getFallback() {
        return Optional.empty();
    }
    
    default Optional<Variant<T>> getLink() {
        return Optional.empty();
    }
    
    default Optional<T> of(CharSequence value) {
        return Optional.empty();
    }
    
    interface Config<T> {
        
        List<String> getKeys();
        
        default Optional<Function<CharSequence, T>> getParser() {
            return Optional.empty();
        }
        
        default Optional<String> getName() {
            return Optional.empty();
        }
        
        default Optional<String> getDescription() {
            return Optional.empty();
        }
        
        default Optional<T> getFallback() {
            return Optional.empty();
        }
        
        default Optional<Variant<T>> getLink() {
            return Optional.empty();
        }
        
        interface Builder<T> extends Config<T> {
            
            Builder<T> keys(String... keys);
            
            Builder<T> name(String name);
            
            Builder<T> parser(Function<CharSequence, T> parser);
            
            Builder<T> description(String description);
            
            Builder<T> fallback(Supplier<T> fallback);
            
            Builder<T> link(Variant<T> link);
        }
    }
}
