package io.github.jonloucks.variants.impl;

import java.util.Arrays;
import java.util.function.Function;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;

final class SplitByRegex {
    
    SplitByRegex(String delimiter) {
        this.delimiter = nullCheck(delimiter, "Delimiter must be present.");
    }
    
    Function<CharSequence, Iterable<CharSequence>> compile() {
        return text -> {
            final CharSequence[] parts = text.toString().split(delimiter);
            return Arrays.asList(parts);
        };
    }
    
    private final String delimiter;
}
