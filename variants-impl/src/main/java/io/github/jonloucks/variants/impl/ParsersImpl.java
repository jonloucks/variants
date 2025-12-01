package io.github.jonloucks.variants.impl;

import io.github.jonloucks.variants.api.Parsers;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

final class ParsersImpl implements Parsers {

    @Override
    public <T> Function<CharSequence, Optional<List<T>>> ofList(Function<CharSequence, Optional<T>> of, String delimiter) {
        return new OfListImpl<>(of, new SplitByRegex(delimiter).compile()).compile();
    }

    @Override
    public <T extends Enum<T>> Function<CharSequence,T> enumParser(Class<T> enumClass) {
        return new EnumParserImpl<>(enumClass).compile();
    }
    
    @Override
    public CharSequence trim(CharSequence text) {
        return new TrimTextImpl(text).trim();
    }
    
    ParsersImpl() {
    
    }
}
