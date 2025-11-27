package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static io.github.jonloucks.contracts.api.GlobalContracts.claimContract;
import static io.github.jonloucks.contracts.test.Tools.assertObject;
import static io.github.jonloucks.contracts.test.Tools.assertThrown;
import static io.github.jonloucks.variants.test.Tools.withVariants;
import static org.junit.jupiter.api.Assertions.*;

public interface EnvironmentTests {
    
    @Test
    default void environment_create_WithNullConfig_Throws() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            assertThrown(IllegalArgumentException.class,
                () -> factory.createEnvironment((Environment.Config)null),
                "Config must be present.");
        });
    }
    
    @Test
    default void environment_create_WithNullConfigBuilderConsumer_Throws() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            assertThrown(IllegalArgumentException.class,
                () -> factory.createEnvironment((Consumer<Environment.Config.Builder>)null),
                "Builder consumer must be present.");
        });
    }
    
    @Test
    default void environment_create_ConfigWithNoSources_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final Environment.Config config = new Environment.Config() {};
            
            final Environment environment = factory.createEnvironment(config);
            
            assertObject(environment);
        });
    }
    
    @Test
    default void environment_create_BuilderWithNoSources_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            
            final Environment environment = factory.createEnvironment(b -> {});
            
            assertObject(environment);
        });
    }
    
    @Test
    default void environment_create_BuilderWithNoNullMap_Throws() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final Environment environment = factory.createEnvironment(
                b -> assertThrown(IllegalArgumentException.class,
                () -> b.addMapSource(null),
                "Map must be present."));
            assertObject(environment);
        });
    }
    
    @Test
    default void environment_create_BuilderWithNullKey_Throws() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            
            factory.createEnvironment(
                b -> assertThrown(IllegalArgumentException.class,
                    () -> b.addSingletonSource(null, "text"),
                    "Key must be present."));
        });
    }
    
    @Test
    default void environment_create_BuilderWithNullText_Throws() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            
            factory.createEnvironment(
                b -> assertThrown(IllegalArgumentException.class,
                    () -> b.addSingletonSource("key", null),
                    "Text must be present."));
        });
    }
    
    @Test
    default void environment_create_BuilderWithNoNullProperties_Throws() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            
            final Environment environment = factory.createEnvironment(
                b -> assertThrown(IllegalArgumentException.class,
                    () -> b.addPropertiesSource(null),
                    "Properties must be present."));
            
            assertObject(environment);
        });
    }
    
    @Test
    default void environment_findVariance_WithNoSources_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final Environment.Config config = new Environment.Config() {};
            final Variant<Duration> variant = new Variant<>() {};
            
            final Environment environment = factory.createEnvironment(config);
            
            assertFalse(environment.findVariance(variant).isPresent(),
                "findVariance should not have found a matching variance.");
            assertThrown(VariantException.class,
                () -> environment.getVariance(variant));
        });
    }
    
    @Test
    default void environment_findVariance_WithSystemEnvironment_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<String> variant = variantFactory.createVariant(b -> b
                    .key(UUID.randomUUID().toString())
                    .parser(Object::toString)
                );
            
            final Environment environment = factory.createEnvironment(b -> {
                final Environment.Config.Builder returnBuilder = b.addSystemEnvironmentSource();
                assertEquals(b, returnBuilder);
            });
            
            assertFalse(environment.findVariance(variant).isPresent(),
                "findVariance should not have found a matching variance.");
            assertThrown(VariantException.class,
                () -> environment.getVariance(variant));
        });
    }
    
    @Test
    default void environment_Variance_WithMap_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final String key = "key";
            final Variant<String> variant = variantFactory.createVariant(b -> b
                .key(key)
                .parser(Object::toString)
            );
            
            final Environment environment = factory.createEnvironment(b -> b
                .addSingletonSource(key, "value"));
            
            assertTrue(environment.findVariance(variant).isPresent(),
                "findVariance should have found a matching variance.");
            assertEquals("value", environment.findVariance(variant).get());
            assertEquals("value", environment.getVariance(variant));
        });
    }
    
    @Test
    default void environment_Variance_WithFickleParser_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final String key = "key";
            final Variant<String> variant = variantFactory.createVariant(b -> b
                .key(key)
                .parser(x -> null)
            );
            
            final Environment environment = factory.createEnvironment(b -> b
                .addSingletonSource(key, "value"));
            
            assertFalse(environment.findVariance(variant).isPresent(),
                "findVariance should have not found a matching variance.");
        });
    }
    
    @Test
    default void environment_Variance_WithProperties_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final String key = "key";
            final Variant<String> variant = variantFactory.createVariant(b -> b
                .key(key)
                .parser(Object::toString)
            );
            final Properties properties = new Properties();
            properties.put("key", "value");
            
            
            final Environment environment = factory.createEnvironment(b -> {
                final Environment.Config.Builder returnBuilder = b.addPropertiesSource(properties);
                assertEquals(b, returnBuilder);
            });
  
            assertTrue(environment.findVariance(variant).isPresent(),
                "findVariance should have found a matching variance.");
            assertEquals("value", environment.findVariance(variant).get());
            assertEquals("value", environment.getVariance(variant));
        });
    }
    
    @Test
    default void environment_findVariance_WithSystemProperties_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final String key = "java.version";
            final Variant<String> variant = variantFactory.createVariant(b -> b
                .key(key)
                .parser(Object::toString)
            );
            
            final Environment environment = factory.createEnvironment(b -> {
                final Environment.Config.Builder returnBuilder = b.addSystemPropertiesSource();
                assertEquals(b, returnBuilder);
            });
            
            assertTrue(environment.findVariance(variant).isPresent(),
                "findVariance should have found a matching variance.");
        });
    }
    
    @Test
    default void environment_findVariance_WithMaps_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final String key = "key";
            final Variant<String> variant = variantFactory.createVariant(b -> b
                .key(key)
                .parser(Object::toString)
            );
            
            final Environment environment = factory.createEnvironment(b -> b
                .addSingletonSource(key, "value1")
                .addSingletonSource(key, "value2")
                .addSingletonSource(key, "value3"));
            
            assertTrue(environment.findVariance(variant).isPresent(),
                "findVariance should have found a matching variance.");
            assertEquals("value1", environment.findVariance(variant).get());
        });
    }
    
    @Test
    default void environment_findVariance_WithMixedMaps_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final String key = "key";
            final Variant<String> variant = variantFactory.createVariant(b -> b
                .key(key)
                .parser(Object::toString)
            );
            
            final Environment environment = factory.createEnvironment(b -> b
                .addSingletonSource("unknown", "value1")
                .addSingletonSource(key, "value2")
                .addSingletonSource(key, "value3"));
            
            assertTrue(environment.findVariance(variant).isPresent(),
                "findVariance should have found a matching variance.");
            assertEquals("value2", environment.findVariance(variant).get());
        });
    }
    
    @Test
    default void environment_findVariance_WithLink_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<String> linkVariant = variantFactory.createVariant(b -> b
                .key("linkKey")
                .parser(Object::toString)
            );
            final Variant<String> variant = variantFactory.createVariant(b -> b
                .key("key")
                .parser(Object::toString)
                .link(linkVariant)
            );
            
            final Environment environment = factory.createEnvironment(b -> b
                .addSingletonSource("unknownKey", "unknownValue")
                .addSingletonSource("linkKey", "linkValue"));
            
            assertTrue(environment.findVariance(variant).isPresent(),
                "findVariance should have found a matching variance.");
            assertEquals("linkValue", environment.findVariance(variant).get());
        });
    }
    
    @Test
    default void environment_findVariance_WithLinkFallback_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final VariantFactory variantFactory = claimContract(VariantFactory.CONTRACT);
            final Variant<String> rootVariant = variantFactory.createVariant(b -> b
                .fallback(() -> "rootValue")
            );
            
            final AtomicReference<Variant<String>> lastVariant = new AtomicReference<>(rootVariant);
            for (int n = 0; n < 100; n++) {
                final String key = "key" + n;
                final Variant<String> currentVariant = variantFactory.createVariant(b -> b
                    .key(key)
                    .parser(Object::toString)
                    .link(lastVariant.get())
                );
                lastVariant.set(currentVariant);
            }
            final Variant<String> variant = lastVariant.get();
            
            final Environment environment = factory.createEnvironment(b -> {});
            
            assertTrue(environment.findVariance(variant).isPresent(),
                "findVariance should have found a matching variance.");
            assertEquals("rootValue", environment.findVariance(variant).get());
        });
    }
    
    @Test
    default void environment_getVariance_WithNoSources_Works() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final Environment.Config config = new Environment.Config() {};
            final Variant<Duration> variant = new Variant<>() {};

            final Environment environment = factory.createEnvironment(config);

            assertFalse(environment.findVariance(variant).isPresent());
            assertThrown(VariantException.class,() -> environment.getVariance(variant));
        });
    }
    
    @Test
    default void environment_findVariance_WithNullVariant_Throws() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final Environment.Config config = new Environment.Config() {};
            final Environment environment = factory.createEnvironment(config);
            
            assertThrown(IllegalArgumentException.class,
                () -> environment.findVariance(null),
                "Variant must be present.");
        });
    }
    
    @Test
    default void environment_getVariance_WithNullVariant_Throws() {
        withVariants((contracts, variants) -> {
            final EnvironmentFactory factory = claimContract(EnvironmentFactory.CONTRACT);
            final Environment.Config config = new Environment.Config() {};
            final Environment environment = factory.createEnvironment(config);
            
            assertThrown(IllegalArgumentException.class,
                () -> environment.getVariance(null),
                "Variant must be present.");
        });
    }
}
