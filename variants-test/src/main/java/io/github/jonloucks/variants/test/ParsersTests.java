package io.github.jonloucks.variants.test;


import io.github.jonloucks.variants.api.Parsers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.jonloucks.contracts.test.Tools.assertObject;
import static io.github.jonloucks.contracts.test.Tools.assertThrown;
import static io.github.jonloucks.variants.test.Internal.toCharSequence;
import static io.github.jonloucks.variants.test.Tools.withVariants;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public interface ParsersTests {
    
    @Test
    default void parsers_instance_IsObject() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            
            assertObject(parsers);
        });
    }
    
    @Test
    default void parsers_stringParser_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, String> parser = parsers.stringParser();
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(null),
                "Text must be present.");
        });
    }
    
    @Test
    default void parsers_stringParser_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, String> parser = parsers.stringParser();
            
            assertEquals("", parser.apply(toCharSequence("")));
            assertEquals("green", parser.apply(toCharSequence("green")));
            assertEquals(" green ", parser.apply(toCharSequence(" green ")));
            assertEquals("green, blue, red", parser.apply(toCharSequence("green, blue, red")));
        });
    }
    
    @Test
    default void parsers_ofRawString_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<String>> of = parsers.ofRawString();
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofRawString_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<String>> of = parsers.ofRawString();
            
            assertEquals("", of.apply(toCharSequence("")).get());
            assertEquals("green", of.apply(toCharSequence("green")).get());
            assertEquals(" green ", of.apply(toCharSequence(" green ")).get());
            assertEquals("green, blue, red", of.apply(toCharSequence("green, blue, red")).get());
        });
    }
    
    @Test
    default void parsers_ofString_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<String>> of = parsers.ofRawString();
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofString_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<String>> of = parsers.ofString();
            
            assertFalse(of.apply(toCharSequence("")).isPresent());
            assertFalse(of.apply(toCharSequence("  ")).isPresent());
            assertEquals("green", of.apply(toCharSequence("green")).get());
            assertEquals("green", of.apply(toCharSequence(" green ")).get());
            assertEquals("green, blue, red", of.apply(toCharSequence("green, blue, red")).get());
        });
    }
    
    @Test
    default void parsers_booleanParser_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Boolean> parser = parsers.booleanParser();
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(null),
                "Text must be present.");
        });
    }
    
    @Test
    default void parsers_booleanParser_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Boolean> parser = parsers.booleanParser();
            
            assertEquals(Boolean.FALSE, parser.apply(toCharSequence("")));
            assertEquals(Boolean.TRUE,  parser.apply(toCharSequence("true")));
            assertEquals(Boolean.FALSE,  parser.apply(toCharSequence("false")));
            assertEquals(Boolean.FALSE,  parser.apply(toCharSequence("junk")));
        });
    }
    
    @Test
    default void parsers_ofBoolean_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Boolean>> of = parsers.ofBoolean();
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofBoolean_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Boolean>> of = parsers.ofBoolean();
            
            assertFalse(of.apply(toCharSequence("")).isPresent());
            assertEquals(Boolean.TRUE, of.apply(toCharSequence("true")).get());
            assertEquals(Boolean.FALSE, of.apply(toCharSequence("false")).get());
            assertEquals(Boolean.FALSE, of.apply(toCharSequence("junk")).get());
        });
    }
    
    @Test
    default void parsers_integerParser_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Integer> parser = parsers.integerParser();
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(null),
                "Text must be present.");
        });
    }
    
    @Test
    default void parsers_integerParser_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Integer> parser = parsers.integerParser();
            
            assertEquals(-1, parser.apply(toCharSequence("-1")));
            assertEquals(0, parser.apply(toCharSequence("0")));
            assertEquals(1, parser.apply(toCharSequence("1")));
        });
    }
    
    @Test
    default void parsers_ofInteger_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Integer>> of = parsers.ofInteger();
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofInteger_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Integer>> of = parsers.ofInteger();
            
            assertFalse(of.apply(toCharSequence("")).isPresent());
            assertEquals(-1, of.apply(toCharSequence("-1")).get());
            assertEquals(0, of.apply(toCharSequence("0")).get());
            assertEquals(1, of.apply(toCharSequence("1")).get());
        });
    }
    
    
    @Test
    default void parsers_longParser_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Long> parser = parsers.longParser();
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(null),
                "Text must be present.");
        });
    }
    
    @Test
    default void parsers_longParser_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Long> parser = parsers.longParser();
            
            assertEquals(-1L, parser.apply(toCharSequence("-1")));
            assertEquals(0L, parser.apply(toCharSequence("0")));
            assertEquals(1L, parser.apply(toCharSequence("1")));
        });
    }
    
    @Test
    default void parsers_ofLong_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Long>> of = parsers.ofLong();
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofLong_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Long>> of = parsers.ofLong();
            
            assertFalse(of.apply(toCharSequence("")).isPresent());
            assertEquals(-1L, of.apply(toCharSequence("-1")).get());
            assertEquals(0L, of.apply(toCharSequence("0")).get());
            assertEquals(1L, of.apply(toCharSequence("1")).get());
        });
    }
    
    @Test
    default void parsers_floatParser_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Float> parser = parsers.floatParser();
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(null),
                "Text must be present.");
        });
    }
    
    @Test
    default void parsers_floatParser_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Float> parser = parsers.floatParser();
            
            assertEquals(-1f, parser.apply(toCharSequence("-1")));
            assertEquals(0f, parser.apply(toCharSequence("0")));
            assertEquals(1f, parser.apply(toCharSequence("1")));
        });
    }
    
    @Test
    default void parsers_ofFloat_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Float>> of = parsers.ofFloat();
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofFloat_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Float>> of = parsers.ofFloat();
            
            assertFalse(of.apply(toCharSequence("")).isPresent());
            assertEquals(-1f, of.apply(toCharSequence("-1")).get());
            assertEquals(0f, of.apply(toCharSequence("0")).get());
            assertEquals(1f, of.apply(toCharSequence("1")).get());
        });
    }
    
    @Test
    default void parsers_doubleParser_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Double> parser = parsers.doubleParser();
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(null),
                "Text must be present.");
        });
    }
    
    @Test
    default void parsers_doubleParser_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Double> parser = parsers.doubleParser();
            
            assertEquals(-1d, parser.apply(toCharSequence("-1")));
            assertEquals(0d, parser.apply(toCharSequence("0")));
            assertEquals(1d, parser.apply(toCharSequence("1")));
        });
    }
    
    @Test
    default void parsers_ofDouble_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Double>> of = parsers.ofDouble();
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofDouble_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Double>> of = parsers.ofDouble();
            
            assertFalse(of.apply(toCharSequence("")).isPresent());
            assertEquals(-1d, of.apply(toCharSequence("-1")).get());
            assertEquals(0d, of.apply(toCharSequence("0")).get());
            assertEquals(1d, of.apply(toCharSequence("1")).get());
        });
    }
    
    @Test
    default void parsers_durationParser_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Duration> parser = parsers.durationParser();
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(null),
                "Text must be present.");
        });
    }
    
    @Test
    default void parsers_durationParser_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Duration> parser = parsers.durationParser();
            
            assertEquals(Duration.ofSeconds(1), parser.apply(Duration.ofSeconds(1).toString()));
            assertEquals(Duration.ofSeconds(0), parser.apply(Duration.ofSeconds(0).toString()));
            assertEquals(Duration.ofSeconds(-1), parser.apply(Duration.ofSeconds(-1).toString()));
        });
    }
    
    @Test
    default void parsers_ofDuration_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Duration>> of = parsers.ofDuration();
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofDuration_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Duration>> of = parsers.ofDuration();
            
            assertFalse(of.apply(toCharSequence("")).isPresent());
            assertEquals(Duration.ofSeconds(1), of.apply(Duration.ofSeconds(1).toString()).get());
            assertEquals(Duration.ofSeconds(0), of.apply(Duration.ofSeconds(0).toString()).get());
            assertEquals(Duration.ofSeconds(-1), of.apply(Duration.ofSeconds(-1).toString()).get());
        });
    }
    
    @Test
    default void parsers_enumParser_WithNullClass_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
     
            assertThrown(IllegalArgumentException.class,
                () -> parsers.enumParser(null),
                "Enum class must be present.");
        });
    }
    
    @Test
    default void parsers_enumParser_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Thread.State> parser = parsers.enumParser(Thread.State.class);
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(null),
                "Text must be present.");
        });
    }
    
    @Test
    default void parsers_enumParser_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Thread.State> parser = parsers.enumParser(Thread.State.class);
            
            assertEquals(Thread.State.BLOCKED, parser.apply(toCharSequence("BLOCKED")));
            assertEquals(Thread.State.BLOCKED, parser.apply(toCharSequence(Thread.State.BLOCKED.ordinal())));
        });
    }
    
    @Test
    default void parsers_enumParser_Illegal_Throws() {
        withVariants((contracts, variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Thread.State> parser = parsers.enumParser(Thread.State.class);
            
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(toCharSequence("")),
                "Enum text must not be empty.");
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(toCharSequence("INVALID")));
            assertThrown(IllegalArgumentException.class,
                () -> parser.apply(toCharSequence(" BLOCKED ")));
        });
    }
    
    @Test
    default void parsers_ofEnum_WithNullText_IsEmpty() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Thread.State>> of = parsers.ofEnum(Thread.State.class);
            
            assertFalse(of.apply(null).isPresent());
        });
    }
    
    @Test
    default void parsers_ofEnum_Works() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Thread.State>> of = parsers.ofEnum(Thread.State.class);
            
            assertFalse(of.apply(toCharSequence("")).isPresent());
            assertEquals(Thread.State.BLOCKED, of.apply(toCharSequence("BLOCKED")).get());
            assertEquals(Thread.State.BLOCKED, of.apply(toCharSequence(" BLOCKED ")).get());
            assertEquals(Thread.State.BLOCKED, of.apply(toCharSequence(Thread.State.BLOCKED.ordinal())).get());
        });
    }
    
    @Test
    default void parsers_trim_WithNullText_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            
            assertThrown(IllegalArgumentException.class,
                () -> parsers.trim(null),
                "Text must be present.");
        });
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", " x", "x ", " x ", " x y ", " ", "   ", "  x  ", "\r\n", "no-spaces"})
    default void parsers_trim_Works(CharSequence value) {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
    
            assertEquals(value.toString().trim(), parsers.trim(value));
        });
    }
    
    @Test
    default void parsers_string_WithNullParser_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            
            assertThrown(IllegalArgumentException.class,
                () -> parsers.string(null),
                "Parser must be present.");
        });
    }
    
    @Test
    default void parsers_string_WithObject_Works(@Mock Function<String,Integer> integerParser) {
        when(integerParser.apply("33")).thenReturn(33);
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence,Integer> wrappedParser = parsers.string(integerParser);
         
            final Integer integer = wrappedParser.apply(toCharSequence("33"));
            
            assertEquals(33, integer);
            verify(integerParser, times(1)).apply("33");
        });
    }
    
    @Test
    default void parsers_ofTrimAndSkipEmpty_WithNullParser_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            
            assertThrown(IllegalArgumentException.class,
                () -> parsers.ofTrimAndSkipEmpty(null),
                "Parser must be present.");
        });
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\r"})
    default void parsers_ofTrimAndSkipEmpty_WithEmpty_IsSkipped(String text) {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence,Duration> bombParser = t -> {
                throw new IllegalArgumentException("Should not be called.");
            };
            final Function<CharSequence,Optional<Duration>> wrappedParser = parsers.ofTrimAndSkipEmpty(bombParser);
            
            final Optional<Duration> optional = wrappedParser.apply(toCharSequence(text));
            
            assertFalse(optional.isPresent());
        });
    }
    
    @Test
    default void parsers_ofList_WithNullConverter_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            
            assertThrown(IllegalArgumentException.class,
                () -> parsers.ofList(null, ","),
                "Text conversion must be present.");
        });
    }
    
    @Test
    default void parsers_ofList_WithNullDelimiter_Throws() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Integer>> delegate = parsers.ofInteger();
            
            assertThrown(IllegalArgumentException.class,
                () -> parsers.ofList(delegate, null),
                "Delimiter must be present.");
        });
    }
    
    @Test
    default void parsers_ofList_WithNullText_IsSkipped() {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Integer>> delegate = parsers.ofInteger();
            final Function<CharSequence, Optional<List<Integer>>> wrappedParser =
                parsers.ofList(delegate, ",");
            
            assertFalse(wrappedParser.apply(null).isPresent());
        });
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\r", ",", ", ,"})
    default void parsers_ofList_WithEmptyText_IsSkipped(String text) {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Integer>> delegate = parsers.ofInteger();
            final Function<CharSequence, Optional<List<Integer>>> wrappedParser =
                parsers.ofList(delegate, ",");
            
            assertFalse(wrappedParser.apply(toCharSequence(text)).isPresent());
        });
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"1,2,3", " 1,2,3 ", " 1 , 2 , 3", "1, , 2 ,3"})
    default void parsers_ofList_WithValid_Works(CharSequence value) {
        withVariants((contracts,variants) -> {
            final Parsers parsers = contracts.claim(Parsers.CONTRACT);
            final Function<CharSequence, Optional<Integer>> delegate = parsers.ofInteger();
            final Function<CharSequence, Optional<List<Integer>>> wrappedParser =
                parsers.ofList(delegate, ",");
            final Optional<List<Integer>> optional = wrappedParser.apply(toCharSequence(value));
            assertTrue(optional.isPresent());
            assertEquals(Arrays.asList(1,2,3), optional.get());
        });
    }
}
