package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Variant;
import io.github.jonloucks.variants.api.VariantSource;

import java.util.List;
import java.util.Optional;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static io.github.jonloucks.variants.api.Checks.variantCheck;

/**
 * Responsibility: Find a Variant value from a list of sources
 * @param <T> the type of Variant value
 */
final class FindVariantImpl<T> {
    
    Optional<T> findVariance() {
        for (VariantSource source : sources) {
            final Optional<T> match = findVariantInSource(targetVariant, source);
            if (match.isPresent()) {
                return match;
            }
        }
        return findFirstFallback(targetVariant);
    }
    
    FindVariantImpl(List<VariantSource> sources, Variant<T> variant) {
        this.sources = nullCheck(sources, "Sources must be present.");
        this.targetVariant = variantCheck(variant);
    }
    
    private Optional<T> findVariantInSource(Variant<T> variant, VariantSource source) {
        for (String key : variant.getKeys()) {
            final Optional<CharSequence> optionalText = source.getSourceText(key);
            if (optionalText.isPresent()) {
                final Optional<T> optionalVariance = variant.of(optionalText.get());
                if (optionalVariance.isPresent()) {
                    return optionalVariance;
                }
            }
        }
        return findLinkVariantInSource(variant, source);
    }
    
    private Optional<T> findFirstFallback(Variant<T> variant) {
        final Optional<T> optionalFallback = variant.getFallback();
        if (optionalFallback.isPresent()) {
            return optionalFallback;
        }
        if (variant.getLink().isPresent()) {
            return findFirstFallback(variant.getLink().get());
        }
        return Optional.empty();
    }

    private Optional<T> findLinkVariantInSource(Variant<T> variant, VariantSource source) {
        if (variant.getLink().isPresent()) {
            final Optional<T> match = findVariantInSource(variant.getLink().get(), source);
            if (match.isPresent()) {
                return match;
            }
        }
        return Optional.empty();
    }
    
    private final List<VariantSource> sources;
    private final Variant<T> targetVariant;
}
