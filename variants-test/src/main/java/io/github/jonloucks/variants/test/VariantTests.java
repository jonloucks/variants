package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.Variant;
import io.github.jonloucks.variants.api.VariantFactory;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Consumer;

import static io.github.jonloucks.contracts.api.GlobalContracts.claimContract;
import static io.github.jonloucks.contracts.test.Tools.assertObject;
import static io.github.jonloucks.contracts.test.Tools.assertThrown;
import static io.github.jonloucks.variants.test.Tools.withVariants;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public interface VariantTests {
    
    @Test
    default void variant_Variant_WithDefaults_Works() {
        final Variant<Duration> variant = new Variant<>() {};
        
        assertTrue(variant.getKeys().isEmpty(), "By default keys should not be present." );
        assertFalse(variant.getDescription().isPresent(), "By default description should not be present.");
        assertFalse(variant.getFallback().isPresent(), "By default fallback should not be present.");
        assertFalse(variant.getLink().isPresent(), "By default link should not be present.");
        assertFalse(variant.getName().isPresent(), "By default name should not be present.");
        assertFalse(variant.of("hello").isPresent(), "By default of() should return empty.");
    }
    
    @Test
    default void variant_Config_WithDefaults_Works() {
        final Variant.Config<Duration> variant = new Variant.Config<>() {};
        
        assertTrue(variant.getKeys().isEmpty(), "By default keys should not be present." );
        assertFalse(variant.getDescription().isPresent(), "By default description should not be present.");
        assertFalse(variant.getFallback().isPresent(), "By default fallback should not be present.");
        assertFalse(variant.getLink().isPresent(), "By default link should not be present.");
        assertFalse(variant.getName().isPresent(), "By default name should not be present.");
        assertFalse(variant.getParser().isPresent(), "By parser should not be present.");
    }
    
    @Test
    default void variant_ConfigBuilder_Key_WithNull_Throws() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            assertThrown(IllegalArgumentException.class,
                () -> variantFactory.createVariant(b -> b.key(null))
                , "Key must be present.");
        });
    }
    
    @Test
    default void variant_ConfigBuilder_Keys_WithNullCollection_Throws() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            assertThrown(IllegalArgumentException.class,
                () -> variantFactory.createVariant(b -> b.keys((Collection<String>) null))
                , "Keys must be present.");
        });
    }
    
    @Test
    default void variant_ConfigBuilder_Keys_WithNullArray_Throws() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            assertThrown(IllegalArgumentException.class,
                () -> variantFactory.createVariant(b -> b.keys((String[]) null))
                , "Keys must be present.");
        });
    }
    
    @Test
    default void variant_ConfigBuilder_Keys_WithArray_Works() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<Integer> variant = variantFactory.createVariant(
                b -> b
                    .keys("key1", "key2", "key3")
                    .parser(c -> Integer.valueOf(c.toString())));
            
            assertEquals(asList("key1", "key2", "key3"), variant.getKeys(), "Keys should match.");
        });
    }
    
    @Test
    default void variant_create_WithOverrides_Works() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<Duration> link = variantFactory.createVariant(b -> b
                .key("xyz")
                .parser(Duration::parse)
            );
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
                b.key("key");
                assertObject(b);
                b.name("name");
                assertObject(b);
                b.parser(Duration::parse);
            });
            assertObject(variant);
        });
    }
    
    @Test
    default void variant_of_SkipsNulls() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<Duration> variant = variantFactory.createVariant(b -> b.parser(Duration::parse));
            assertFalse(variant.of(null).isPresent(), "Parse null should be skipped.");
        });
    }
    
    @Test
    default void variant_create_WithoutName_DefaultsToFirstKey() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<Duration> variant = variantFactory.createVariant(b -> b
                .key("key")
                .parser(Duration::parse));
            assertTrue(variant.getName().isPresent(), "Name should be present.");
            assertEquals("key", variant.getName().get(), "Name should match.");
        });
    }
    
    @Test
    default void variant_create_WithNullBuilderConsumer_Throws() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            
            assertThrown(IllegalArgumentException.class,
                () -> variantFactory.createVariant((Consumer<Variant.Config.Builder<String>>)null),
                "Builder consumer must be present.");
        });
    }
    
    @Test
    default void variant_create_WithNullConfig_Throws() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            
            assertThrown(IllegalArgumentException.class,
                () -> variantFactory.createVariant((Variant.Config<String>)null),
                "Config must be present.");
        });
    }
    
    @Test
    default void variant_create_BuilderWithoutKeys_Works() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            
            final Variant<Duration> variant = variantFactory.createVariant(b -> {});
            
            assertObject(variant);
        });
    }
    
    @Test
    default void variant_create_WithKeyAndNoParser_Throws() {
        withVariants((contracts, variants) -> {
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);

            assertThrown(IllegalArgumentException.class,
                () -> variantFactory.createVariant(b -> b.keys("key")),
                "Parser is required when keys are present.");
        });
    }
}
