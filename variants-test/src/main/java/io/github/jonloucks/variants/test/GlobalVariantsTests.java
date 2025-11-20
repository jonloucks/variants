package io.github.jonloucks.variants.test;

import io.github.jonloucks.contracts.api.GlobalContracts;
import io.github.jonloucks.variants.api.GlobalVariants;
import io.github.jonloucks.variants.api.VariantException;
import io.github.jonloucks.variants.api.Variants;
import io.github.jonloucks.variants.api.VariantsFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.jonloucks.contracts.test.Tools.*;
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
