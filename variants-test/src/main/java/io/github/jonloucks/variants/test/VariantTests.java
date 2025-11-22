package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.Variant;
import io.github.jonloucks.variants.api.VariantFactory;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.jonloucks.contracts.api.GlobalContracts.claimContract;
import static io.github.jonloucks.contracts.test.Tools.assertObject;
import static io.github.jonloucks.contracts.test.Tools.assertThrown;
import static io.github.jonloucks.variants.test.Tools.withVariants;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

public interface VariantTests {
    
    @Test
    default void variant_Variant_WithDefaults_Works() {
        final List<String> keys = new ArrayList<>();
        keys.add("key");
        final Variant<Duration> variant = () -> keys;
        
        assertFalse(variant.getDescription().isPresent(), "By default description should not be present.");
        assertFalse(variant.getFallback().isPresent(), "By default fallback should not be present.");
        assertFalse(variant.getLink().isPresent(), "By default link should not be present.");
        assertFalse(variant.getName().isPresent(), "By default name should not be present.");
        assertFalse(variant.of("hello").isPresent(), "By default of() should return empty.");
    }
    
    @Test
    default void variant_Config_WithDefaults_Works() {
        final List<String> keys = new ArrayList<>();
        keys.add("key");
        final Variant.Config<Duration> variant = () -> keys;
        
        assertFalse(variant.getDescription().isPresent(), "By default description should not be present.");
        assertFalse(variant.getFallback().isPresent(), "By default fallback should not be present.");
        assertFalse(variant.getLink().isPresent(), "By default link should not be present.");
        assertFalse(variant.getName().isPresent(), "By default name should not be present.");
        assertNull(variant.getParser(), "By parser should not be present.");
    }
    
    @Test
    default void variant_create_WithOverrides_Works() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<Duration> link = () -> singletonList("xyz");
            final Variant<Duration> variant = variantFactory.createVariant(b -> b
                .description("description")
                .name("name")
                .parser(Duration::parse)
                .link(link)
                .fallback(() -> Duration.ZERO)
                .keys("key1", "key2", "key3")
            );
            assertObject(variant);
            final Duration duration = Duration.ofSeconds(7);
            assertTrue(variant.getName().isPresent(), "Name should be present.");
            assertEquals("name", variant.getName().get(), "Name should match.");
            assertTrue(variant.getDescription().isPresent(), "Description should be present.");
            assertEquals("description", variant.getDescription().get(), "Description should match.");
            assertEquals(asList("key1", "key2", "key3"), variant.getKeys(), "Keys should match.");
            assertTrue(variant.getFallback().isPresent(), "Fallback should be present.");
            assertEquals(Duration.ZERO, variant.getFallback().get(), "Fallback should match.");
            assertTrue(variant.getLink().isPresent(), "Link should be present.");
            assertEquals(link, variant.getLink().get(), "Link should match.");
            assertTrue(variant.of(duration.toString()).isPresent(), "Parsed value should match.");
            assertEquals(duration, variant.of(duration.toString()).get(), "Parsed value should match.");
        });
    }
    
    @Test
    default void variant_create_Builder_isObject() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<Duration> variant = variantFactory.createVariant(b -> {
                assertObject(b);
                b.keys("key");
                assertObject(b);
                b.name("name");
                assertObject(b);
                b.parser(Duration::parse);
            });
            assertObject(variant);
        });
    }
    
    @Test
    default void variant_create_WithoutName_DefaultsToFirstKey() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<Duration> variant = variantFactory.createVariant(b -> b
                .keys("key")
                .parser(Duration::parse));
            assertTrue(variant.getName().isPresent(), "Name should be present.");
            assertEquals("key", variant.getName().get(), "Name should match.");
        });
    }
    
    @Test
    default void variant_create_BuilderWithoutKeys_Throws() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            assertThrown(IllegalArgumentException.class,
                () -> variantFactory.createVariant(b -> {}),
                "Keys must be present.");
        });
    }
}
