package io.github.jonloucks.variants.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static io.github.jonloucks.variants.api.Checks.textCheck;

final class OfListImpl<T> {
    
    OfListImpl(Function<CharSequence, Optional<T>> of, String delimiter) {
        this.of = nullCheck(of, "Text conversion must be present.");
        this.delimiter = nullCheck(delimiter, "Delimiter must be present.");
    }
    
    Function<CharSequence, Optional<List<T>>> compile() {
        return text -> {
            final CharSequence validChars = textCheck(text);
            final String[] parts = validChars.toString().split(delimiter);
            final ArrayList<T> list = new ArrayList<>(parts.length);
            for (String part : parts) {
                of.apply(part).ifPresent(list::add);
            }
            if (list.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(list);
        };
    }
    
    private final Function<CharSequence, Optional<T>> of;
    private final String delimiter;
}
