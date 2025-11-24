package io.github.jonloucks.variants.api;

import java.util.Optional;

@FunctionalInterface
public
interface VariantSource {
    Optional<CharSequence> getSource(String key);
}
