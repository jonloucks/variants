package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.Contract;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.jonloucks.variants.api.Checks.parserCheck;
import static io.github.jonloucks.variants.api.Checks.textCheck;
import static java.util.Optional.ofNullable;

/**
 * Responsibility: Parsers to assist source text conversion for a Variant
 */
public interface Parsers {
    /**
     * Contract for Parsers
     */
    Contract<Parsers> CONTRACT = Contract.create(Parsers.class, b -> b.name("Variant Parsers"));
    
    /**
     * @return a parser that converts a valid text into a String
     */
    default Function<CharSequence, String> stringParser() {
        return text -> textCheck(text).toString();
    }
    
    /**
     * No trimming or skipping empty values
     * @return a text conversion to a String instance
     */
    default Function<CharSequence, Optional<String>> ofRawString() {
        return text -> ofNullable(text).map(CharSequence::toString);
    }
    
    /**
     * Input is trimmed and empty values are skipped
     *
     * @return a text conversion to a String instance
     */
    default Function<CharSequence, Optional<String>> ofString() {
        return ofTrimAndSkipEmpty(stringParser());
    }
    
    /**
     * @return a parser that converts a valid text value into a Boolean instance
     */
    default Function<CharSequence, Boolean> booleanParser() {
        return string(Boolean::parseBoolean);
    }
    
    /**
     * Input is trimmed and empty values are skipped
     *
     * @return a text conversion to a Boolean instance
     */
    default Function<CharSequence, Optional<Boolean>> ofBoolean() {
        return ofTrimAndSkipEmpty(booleanParser());
    }
    
    /**
     * @return a parser that converts a valid text value into a Float instance
     */
    default Function<CharSequence, Float> floatParser() {
        return string(Float::parseFloat);
    }
    
    /**
     * Input is trimmed and empty values are skipped
     *
     * @return a text conversion to a Float instance
     */
    default Function<CharSequence, Optional<Float>> ofFloat() {
        return ofTrimAndSkipEmpty(floatParser());
    }
    
    /**
     * @return a parser that converts a valid text value into a Double instance
     */
    default Function<CharSequence, Double> doubleParser() {
        return string(Double::parseDouble);
    }
    
    /**
     * Input is trimmed and empty values are skipped
     *
     * @return a text conversion to a Double instance
     */
    default Function<CharSequence, Optional<Double>> ofDouble() {
        return ofTrimAndSkipEmpty(doubleParser());
    }
    
    /**
     * @return a parser that converts a valid text value into an Integer instance
     */
    default Function<CharSequence, Integer> integerParser() {
        return string(Integer::parseInt);
    }
    
    /**
     * Input is trimmed and empty values are skipped
     *
     * @return a text conversion to a Integer instance
     */
    default Function<CharSequence, Optional<Integer>> ofInteger() {
        return ofTrimAndSkipEmpty(integerParser());
    }
    
    /**
     * @return a parser that converts a valid text value into a Long instance
     */
    default Function<CharSequence, Long> longParser() {
        return string(Long::parseLong);
    }
    
    /**
     * Input is trimmed and empty values are skipped
     *
     * @return a text conversion to a Long instance
     */
    default Function<CharSequence, Optional<Long>> ofLong() {
        return ofTrimAndSkipEmpty(longParser());
    }

    /**
     * @return a parser that converts a valid text value into a Boolean instance
     */
    default Function<CharSequence, Duration> durationParser() {
        return text -> Duration.parse(textCheck(text).toString());
    }
    
    /**
     * Input is trimmed and empty values are skipped
     *
     * @return a text conversion to a Duration instance
     */
    default Function<CharSequence, Optional<Duration>> ofDuration() {
        return ofTrimAndSkipEmpty(durationParser());
    }
    
    /**
     * A parser that converts a valid text value into a Boolean instance
     *
     * @param enumClass the Enum class
     * @return A parser that converts a valid text value into a Boolean instance
     * @param <T> the type of Enum
     */
    <T extends Enum<T>> Function<CharSequence, T> enumParser(Class<T> enumClass);
    
    /**
     * text conversion to a Enm instance
     *
     * @param enumClass the enum class
     * Input is trimmed and empty values are skipped
     * @return a text conversion to a Duration instance
     * @param <T> the type of Enum
     */
    default <T extends Enum<T>> Function<CharSequence, Optional<T>> ofEnum(Class<T> enumClass) {
        return ofTrimAndSkipEmpty(enumParser(enumClass));
    }
    
    /**
     * trim leading and trailing white space
     *
     * @param text the text to trim
     * @return the trimmed text
     */
    CharSequence trim(CharSequence text);

    /**
     * A parser that converts text to a String
     *
     * @param parser the parser that accepts the String
     * @return the new parser
     * @param <T> the return type of the given parser
     */
    default <T> Function<CharSequence, T> string(Function<String, T> parser) {
        final Function<String, T> validParser = parserCheck(parser);
        return chars -> validParser.apply(textCheck(chars).toString());
    }
    
    /**
     * Text to parser helper.
     * Trims text
     * Skips empty values
     *
     * @param parser the delegate parser
     * @return the new 'of' function
     * @param <T> the return type of the given parser
     */
    default <T> Function<CharSequence, Optional<T>> ofTrimAndSkipEmpty(Function<CharSequence, T> parser) {
        final Function<CharSequence, T> validParser = parserCheck(parser);
        return text -> {
            if (ofNullable(text).isPresent()) {
                final CharSequence trimmed = trim(text);
                return trimmed.length() == 0 ? Optional.empty() : Optional.of(validParser.apply(trimmed));
            } else {
                return Optional.empty();
            }
        };
    }
    
    /**
     * Split the input text and parse each part into a list
     *
     * @param of the delegate text to value function
     * @param delimiter the string delimiter. See {@link String#split(String)}
     * @return the new parser
     * @param <T> the return type of the given parser
     */
    <T> Function<CharSequence, Optional<List<T>>> ofList(Function<CharSequence, Optional<T>> of, String delimiter);
}
