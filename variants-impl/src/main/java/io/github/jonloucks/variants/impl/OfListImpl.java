package io.github.jonloucks.variants.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static java.util.Optional.ofNullable;

final class OfListImpl<T> {
    
    OfListImpl(Function<CharSequence, Optional<T>> of, Function<CharSequence, Iterable<CharSequence>> split) {
        this.of = nullCheck(of, "Text conversion must be present.");
        this.split = nullCheck(split, "Split conversion must be present.");
    }
    
    Function<CharSequence, Optional<List<T>>> compile() {
        return text -> {
            if (ofNullable(text).isPresent()) {
                final List<T> list = toList(text);
                return list.isEmpty() ? Optional.empty() : Optional.of(list);
            } else {
                return Optional.empty();
            }
        };
    }
    
    private List<T> toList(CharSequence text) {
        final List<T> list = new LinkedList<>();
        for (CharSequence part : split.apply(text)) {
            of.apply(part).ifPresent(list::add);
        }
        return list;
    }
    
    private final Function<CharSequence, Optional<T>> of;
    private final Function<CharSequence, Iterable<CharSequence>> split;
}
