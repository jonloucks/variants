package io.github.jonloucks.variants.api;

import java.util.Optional;

/**
 * Responsibility: Retrieve variance in text format
 */
@FunctionalInterface
public interface VariantSource {
    
    /**
     * Get optional source text for parsing variance value
     * @param key the variance key to lookup
     * @return the optional text
     */
    Optional<CharSequence> getSourceText(String key);
    
}
