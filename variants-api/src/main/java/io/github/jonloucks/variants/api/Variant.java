package io.github.jonloucks.variants.api;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.github.jonloucks.variants.api.Checks.keyCheck;
import static io.github.jonloucks.variants.api.Checks.keysCheck;
import static java.util.Collections.singletonList;

/**
 * Responsibility: Provide information on how to retrieve a configuration value.
 *
 * @param <T> the type of configuration value
 */
public interface Variant<T> {
    
    /**
     * The primary use case of getting a property value by key
     * @return the ordered list of keys used to search for values.
     */
    default List<String> getKeys() {
        return Collections.emptyList();
    }
    
    /**
     * Note: The name is not used to look up values!
     * Note: if name is not set the first key might be used
     * @return The user facing name of this Variant
     */
    default Optional<String> getName() {
        return Optional.empty();
    }
    
    /**
     * @return the optional description
     */
    default Optional<String> getDescription() {
        return Optional.empty();
    }
    
    /**
     * @return the optional fallback value. aka default value
     */
    default Optional<T> getFallback() {
        return Optional.empty();
    }
    
    /**
     * Links can be helpful when a Variant which does not
     * have an explicit value can default the value of another Variant.
     * @return The optional link to another Variant
     */
    default Optional<Variant<T>> getLink() {
        return Optional.empty();
    }
    
    /**
     * Parses the
     * Note: Required if keys are used to retrieve a value.
     * Note: implementations of this function should return empty on null values.
     * @param rawText the text to be parsed
     * @return the optional Variance
     */
    default Optional<T> of(CharSequence rawText) {
        return Optional.empty();
    }
    
    /**
     * Responsibility: Configuration for creating a new Variant.
     *
     * @param <T> type Variant value type
     */
    interface Config<T> {
        
        /**
         * The primary use case of getting a property value by key
         * @return the ordered list of keys used to search for values.
         */
        default List<String> getKeys() {
            return Collections.emptyList();
        }
        
        /**
         * Note: The name is not used to look up values!
         * @return The user facing name of this Variant
         */
        default Optional<String> getName() {
            return Optional.empty();
        }
        
        /**
         * @return the optional description
         */
        default Optional<String> getDescription() {
            return Optional.empty();
        }
        
        /**
         * @return the optional fallback value. aka default value
         */
        default Optional<T> getFallback() {
            return Optional.empty();
        }
        
        /**
         * Links can be helpful when a Variant which does not
         * have an explicit value can default the value of another Variant.
         * @return The optional link to another Variant
         */
        default Optional<Variant<T>> getLink() {
            return Optional.empty();
        }
        
        /**
         * @return The parser used to convert raw text into a Variant value
         */
        default Optional<Function<CharSequence, T>> getParser() {
            return Optional.empty();
        }

        /**
         * Responsibility: Builder a configuration used to create a new Variant
         *
         * @param <T> type Variant value type
         */
        interface Builder<T> extends Config<T> {
            
            /**
             * Add a new key
             *
             * @param key the key to add
             * @return this Builder
             */
            default Builder<T> key(String key) {
                return keys(singletonList(keyCheck(key)));
            }
            
            /**
             * Add a variable number of keys
             *
             * @param keys the keys to add
             * @return this Builder
             */
            default Builder<T> keys(String... keys) {
                return keys(Arrays.asList(keysCheck(keys)));
            }
            
            /**
             * Add a collection of keys
             *
             * @param keys the keys to add
             * @return this Builder
             */
            Builder<T> keys(Collection<String> keys);
            
            /**
             * Assign the name
             *
             * @param name the new name
             * @return this Builder
             */
            Builder<T> name(String name);
            
            /**
             *  Assign the parser to convert text into Variant values
             *  A parser can return null for any reason, it is processed as unassigned
             *
             * @param parser the parser
             * @return this Builder
             */
            Builder<T> parser(Function<CharSequence, T> parser);
            
            /**
             * Assign the description
             *
             * @param description the description
             * @return this Builder
             */
            Builder<T> description(String description);
            
            /**
             * Assign the fallback value supplier
             *
             * @param fallback the fallback value supplier
             * @return this Builder
             */
            Builder<T> fallback(Supplier<T> fallback);
            
            /**
             * Assign a link
             * @param link the link
             * @return this Builder
             */
            Builder<T> link(Variant<T> link);
        }
    }
}
