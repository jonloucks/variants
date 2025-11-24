package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Environment;
import io.github.jonloucks.variants.api.Variant;
import io.github.jonloucks.variants.api.VariantSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.jonloucks.contracts.api.Checks.configCheck;
import static io.github.jonloucks.variants.api.Checks.variantCheck;
import static io.github.jonloucks.variants.impl.Internal.optionalOr;

/**
 * Responsibility: Environment implementation
 */
final class EnvironmentImpl implements Environment {

    @Override
    public <T> Optional<T> findVariance(Variant<T> variant) {
        final Variant<T> validVariant = variantCheck(variant);
        for (VariantSource source : sources) {
            final Optional<T> match = findVariantOrLinkInSource(validVariant, source);
            if (match.isPresent()) {
                return match;
            }
        }
        return findVariantOrLinkFallback(validVariant);
    }
    
    EnvironmentImpl(Environment.Config config) {
        final Environment.Config validConfig = configCheck(config);
        sources.addAll(validConfig.getSources());
    }
    
    private <T> Optional<T> findVariantOrLinkFallback(Variant<T> variant) {
        return optionalOr(findByLinkFallback(variant), variant::getFallback);
    }
    
    private <T> Optional<T> findVariantOrLinkInSource(Variant<T> variant, VariantSource source) {
        return optionalOr(findVariantInSource(variant, source), () -> findLinkVariantInSource(variant, source));
    }
    
    private <T> Optional<T> findByLinkFallback(Variant<T> variant) {
        final Optional<Variant<T>> optionalLink = variant.getLink();
        if (optionalLink.isPresent()) {
            final Variant<T> link = optionalLink.get();
            return link.getFallback();
        }
        return Optional.empty();
    }
    
    private <T> Optional<? extends T> findLinkVariantInSource(Variant<T> variant, VariantSource source) {
        if (variant.getLink().isPresent()) {
            final Optional<T> match = findVariantInSource(variant.getLink().get(), source);
            if (match.isPresent()) {
                return match;
            }
        }
        return Optional.empty();
    }
    
    private <T> Optional<T> findVariantInSource(Variant<T> variant, VariantSource source) {
        for (String key : variant.getKeys()) {
            final Optional<CharSequence> optionalText = source.getSource(key);
            if (optionalText.isPresent()) {
                final Optional<T> optionalVariance = variant.of(optionalText.get());
                if (optionalVariance.isPresent()) {
                    return optionalVariance;
                }
            }
        }
        return Optional.empty();
    }
    
    
    private final List<VariantSource> sources = new ArrayList<>();
}
