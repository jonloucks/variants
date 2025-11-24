package io.github.jonloucks.variants.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static java.util.Optional.ofNullable;

/**
 * Responsibility: Locating variances from multiple sources.
 * <p>
 * A breadth first search into each source looking for provided value for a specific Variant
 * Each key in the Variant is search in order of insertion.
 * Fallback values (defaults) are only used if no values are found in any of the sources.
 * </p>
 */
public interface Environment {
    
    /**
     * Find a variance if it exists.
     *
     * @param variant the Variant
     * @return the optional value
     * @param <T> the type of variance value
     */
    <T> Optional<T> findVariance(Variant<T> variant);
    
    /**
     * Get required variance or throw an exception.
     *
     * @param variant the Variant
     * @return the variance
     * @throws VariantException if not found
     * @param <T> the type of variance value
     */
    default <T> T getVariance(Variant<T> variant) {
        return findVariance(variant).orElseThrow(() -> new VariantException("Variant not found. " + variant + "."));
    }
    
    /**
     * Responsibility: Configuration for creating a new Environment
     */
    interface Config {
        
        /**
         * @return the list of sources. An empty list is allowed.
         */
        List<VariantSource> getSources();
        
        /**
         * Responsibility: Builder a configuration used to create a new Environment
         */
        interface Builder extends Environment.Config {
            
            /**
             * Add a new source.
             *
             * @param source the source to add
             * @return this builder
             */
            Builder addSource(VariantSource source);
            
            /**
             * Add a new source based on a java.util.Map.
             *
             * @param map the Map
             * @return this builder
             */
            default Builder addMapSource(Map<String, ?> map) {
                final Map<String, ?> validMap = nullCheck(map, "Map must be present.");
                return addSource(k -> Optional.ofNullable(validMap.get(k)).map(Object::toString));
            }
            
            /**
             * Add a new source based on a java.util.Properties.
             *
             * @param properties the Properties
             * @return this builder
             */
            default Builder addPropertiesSource(Properties properties) {
                final Properties validProperties = nullCheck(properties, "Properties must be present.");
                return addSource(k -> ofNullable(validProperties.getProperty(k)));
            }
            
            /**
             * Add System.getProperty as a source.
             *
             * @return this builder
             */
            default Builder addSystemPropertiesSource() {
                addSource(k -> ofNullable(System.getProperty(k)));
                return this;
            }
            
            /**
             * Add System.getenv as a source.
             *
             * @return this builder
             */
            default Builder addSystemEnvironmentSource() {
                addSource(k -> ofNullable(System.getenv(k)));
                return this;
            }
        }
    }
}
