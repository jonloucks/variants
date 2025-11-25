package io.github.jonloucks.variants.test;

import io.github.jonloucks.contracts.api.GlobalContracts;
import io.github.jonloucks.variants.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static io.github.jonloucks.contracts.test.Tools.*;
import static io.github.jonloucks.variants.test.Tools.withVariants;
import static java.lang.Integer.parseInt;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public interface GlobalVariantsTests {
    
    @Test
    default void globalVariants_Instantiate_Throws() {
        assertInstantiateThrows(GlobalVariants.class);
    }
    
    @Test
    default void globalVariants_getInstance_Works() {
        assertObject(GlobalVariants.getInstance());
    }

    @Test
    default void globalVariants_DefaultConfig() {
        final Variants.Config config = new Variants.Config() {
        };
        
        assertAll(
            () -> assertTrue(config.useReflection(), "config.useReflection() default."),
            () -> assertTrue(config.useServiceLoader(), "config.useServiceLoader() default."),
            () -> assertNotNull(config.reflectionClassName(), "config.reflectionClassName() was null."),
            () -> assertEquals(VariantsFactory.class, config.serviceLoaderClass(), "config.serviceLoaderClass() default."),
            () -> assertEquals(GlobalContracts.getInstance(), config.contracts(), "config.contracts()  default.")
        );
    }
    
    @ParameterizedTest
    @MethodSource("io.github.jonloucks.variants.test.GlobalVariantsTests$GlobalVariantsTestsTools#validConfigs")
    default void globalVariants_findVariantsFactory(Variants.Config config) {
        final Optional<VariantsFactory> factory = GlobalVariants.findVariantsFactory(config);
        
        assertTrue(factory.isPresent());
        assertObject(factory.get());
    }
    
    @ParameterizedTest
    @MethodSource("io.github.jonloucks.variants.test.GlobalVariantsTests$GlobalVariantsTestsTools#invalidConfigs")
    default void globalVariants_createVariants_Invalid(Variants.Config config) {
        assertThrown(VariantException.class,
            () -> GlobalVariants.createVariants(config));
    }
    
    @ParameterizedTest
    @MethodSource("io.github.jonloucks.variants.test.GlobalVariantsTests$GlobalVariantsTestsTools#invalidConfigs")
    default void globalVariants_SadPath(Variants.Config config) {
        assertThrown(VariantException.class,
            () -> GlobalVariants.createVariants(config));
    }

    @Test
    default void globalVariants_InternalCoverage() {
        assertInstantiateThrows(GlobalVariantsTestsTools.class);
    }
    
    @Test
    default void globalVariants_createVariant_WithNullConfig_Throws() {
        withVariants((contracts, variants) ->
            assertThrown(IllegalArgumentException.class,
                () -> GlobalVariants.createVariant((Variant.Config<String>) null),
                "Config must be present."));
    }
    
    @Test
    default void globalVariants_createVariant_WithConfig_Works() {
        final Variant.Config<Integer> config = new Variant.Config<>() {
            @Override
            public Optional<Integer> getFallback() {
                return Optional.of(7);
            }
        };
        final Variant<Integer> variant =  GlobalVariants.createVariant(config);
        
        assertObject(variant);
        assertTrue(variant.getFallback().isPresent(), "Fallback value must be present.");
        assertEquals(7, variant.getFallback().get());
    }
    
    @Test
    default void globalVariants_createVariant_WithNullConfigBuilder_Throws() {
        assertThrown(IllegalArgumentException.class,
            () -> GlobalVariants.createVariant((Consumer<Variant.Config.Builder<String>>) null),
            "Builder consumer must be present.");
    }
    
    @Test
    default void globalVariants_createVariant_WithConfigBuilder_Works() {
        final Variant<Integer> variant =  GlobalVariants.createVariant(b -> b.fallback(() -> 7));
        
        assertObject(variant);
        assertTrue(variant.getFallback().isPresent(), "Fallback value must be present.");
        assertEquals(7, variant.getFallback().get());
    }
    
    @Test
    default void globalVariants_createEnvironment_WithNullConfig_Throws() {
        assertThrown(IllegalArgumentException.class,
            () -> GlobalVariants.createEnvironment((Environment.Config) null),
            "Config must be present.");
    }
    
    @Test
    default void globalVariants_createEnvironment_WithConfig_Works() {
        final Environment.Config config = new Environment.Config() {};
        final Environment environment = GlobalVariants.createEnvironment(config);
        final Variant<Integer> variant = GlobalVariants.createVariant(b -> b.fallback(() -> 7));
        
        assertObject(environment);
        assertTrue(environment.findVariance(variant).isPresent(), "Variance must be present.");
        assertEquals(7, environment.getVariance(variant), "Variance must match.");
    }
    
    @Test
    default void globalVariants_createEnvironment_WithNullConfigBuilder_Throws() {
        assertThrown(IllegalArgumentException.class,
            () -> GlobalVariants.createEnvironment((Consumer<Environment.Config.Builder>) null),
            "Builder consumer must be present.");
    }
    
    @Test
    default void globalVariants_createEnvironment_WithConfigBuilder_Works() {
        final Environment environment = GlobalVariants.createEnvironment(b -> {});
        final Variant<Integer> variant = GlobalVariants.createVariant(b -> b.fallback(() -> 7));
        
        assertObject(environment);
        assertTrue(environment.findVariance(variant).isPresent(), "Variance must be present.");
        assertEquals(7, environment.getVariance(variant), "Variance must match.");
    }
    
    @Test
    default void globalVariants_properties_example() {
        final Properties properties = new Properties();
        properties.setProperty("greeting", "Hello");
        
        final Environment environment = GlobalVariants.createEnvironment(b -> b.addPropertiesSource(properties));
        
        final Variant<String> greeting = GlobalVariants.createVariant(
            b -> b                      // builder
                .key("greeting")                      // the property key
                .parser(CharSequence::toString)       // CharSequence to String
        );
        
        // Expecting "Hello"
        assertEquals("Hello", environment.getVariance(greeting));
    }
        
        
        @Test
    default void globalVariants_everything_example() {
        
        // A source takes a key and returns non-null value or empty.
        final VariantSource customSource = key -> "YOUR_TIMEOUT".equals(key) ? Optional.of("PT30S") : Optional.empty();
        
        // Create an example environment
        final Environment environment = GlobalVariants.createEnvironment(
            b -> b                                               // builder
                .addSystemEnvironmentSource()                           // opt-in; add System.getenv source
                .addSystemPropertiesSource()                            // opt-in; add System.getProperty source
                .addPropertiesSource(new Properties())                  // opt-in; add properties source
                .addMapSource(singletonMap("key", "value"))             // opt-in; add map source
                .addSource(customSource)                                // opt-in; custom source
        );
        
        final Variant<Duration> generalTimeout = GlobalVariants.createVariant(
            b -> b // builder
                .keys("sun.net.client.defaultConnectTimeout")                          // jvm property
                .parser(c -> Duration.ofMillis(parseInt(c.toString())))   // parse from millis
                .fallback(() -> Duration.ofMinutes(5))                                 // if java ever removes the property
        );

        final Variant<Duration> yourTimeout = GlobalVariants.createVariant(
            b -> b                                                  // builder
                .name("Your timeout")                                               // opt-in; specify a user facing name
                .description("How long you should wait for anything.")              // opt-in; description
                .keys("YOUR_TIMEOUT", "your.timeout")                               // opt-in; zero or more keys
                .parser(Duration::parse)                                            // opt-in; when using keys, the parser is required to convert values
                .link(generalTimeout)                                               // opt-in; if no value is the link value is used. Good for cascading defaults
        );

        assertEquals(Duration.ofSeconds(30), environment.getVariance(yourTimeout));
    }
    
    final class GlobalVariantsTestsTools {
        private GlobalVariantsTestsTools() {
            throw new AssertionError("Illegal constructor.");
        }
        
        @SuppressWarnings("RedundantMethodOverride")
        static Stream<Arguments> validConfigs() {
            return Stream.of(
                Arguments.of(new Variants.Config() {
                }),
                Arguments.of(new Variants.Config() {
                    @Override
                    public boolean useServiceLoader() {
                        return false;
                    }
                    
                    @Override
                    public boolean useReflection() {
                        return true;
                    }
                }),
                Arguments.of(new Variants.Config() {
                    @Override
                    public boolean useServiceLoader() {
                        return true;
                    }
                    
                    @Override
                    public boolean useReflection() {
                        return false;
                    }
                })
            );
        }
        
        @SuppressWarnings("RedundantMethodOverride")
        static Stream<Arguments> invalidConfigs() {
            return Stream.of(
                Arguments.of(new Variants.Config() {
                    @Override
                    public boolean useServiceLoader() {
                        return false;
                    }
                    
                    @Override
                    public boolean useReflection() {
                        return false;
                    }
                }),
                Arguments.of(new Variants.Config() {
                    @Override
                    public boolean useServiceLoader() {
                        return true;
                    }

                    @Override
                    public boolean useReflection() {
                        return false;
                    }

                    @Override
                    public Class<? extends VariantsFactory> serviceLoaderClass() {
                        return BadVariantsFactory.class;
                    }
                }),
                Arguments.of(new Variants.Config() {
                    @Override
                    public boolean useServiceLoader() {
                        return false;
                    }

                    @Override
                    public boolean useReflection() {
                        return true;
                    }

                    @Override
                    public String reflectionClassName() {
                        return BadVariantsFactory.class.getName();
                    }
                }),
                Arguments.of(new Variants.Config() {
                    @Override
                    public boolean useServiceLoader() {
                        return false;
                    }

                    @Override
                    public boolean useReflection() {
                        return true;
                    }

                    @Override
                    public String reflectionClassName() {
                        return "";
                    }
                })
            );
        }
    }
}
