package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.Variants.Config.Builder;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static io.github.jonloucks.variants.api.Variants.Config.DEFAULT;
import static io.github.jonloucks.variants.test.Tools.withVariants;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public interface VariantsConfigTests {
    
    @Test
    default void variantsConfig_Defaults() {
        withVariants( (contracts,variants)-> {
            final Builder builder = contracts.claim(Builder.FACTORY).get();
            
            assertEquals(DEFAULT.useReflection(), builder.useReflection());
            assertEquals(DEFAULT.useServiceLoader(), builder.useServiceLoader());
            assertEquals(DEFAULT.contracts(), builder.contracts());
            assertEquals(DEFAULT.serviceLoaderClass(), builder.serviceLoaderClass());
            assertEquals(DEFAULT.reflectionClassName(), builder.reflectionClassName());
        });
    }
    
    @Test
    default void variantsConfig_Modify() {
        withVariants( (contracts,variants)-> {
            final Builder builder = contracts.claim(Builder.FACTORY).get();

            final Function<Builder,Builder> assertBuilder = b -> {
                assertSame(builder, b);
                return builder;
            };
            
            assertBuilder.apply(builder.useReflection(!DEFAULT.useReflection()));
            assertBuilder.apply(builder.useServiceLoader(!DEFAULT.useServiceLoader()));
            assertBuilder.apply(builder.contracts(contracts));
            assertBuilder.apply(builder.serviceLoaderClass(BadVariantsFactory.class));
            assertBuilder.apply(builder.reflectionClassName("MyReflectionClassName"));
            
            assertEquals(!DEFAULT.useReflection(), builder.useReflection());
            assertEquals(!DEFAULT.useServiceLoader(), builder.useServiceLoader());
            assertEquals(contracts, builder.contracts());
            assertEquals(BadVariantsFactory.class, builder.serviceLoaderClass());
            assertEquals("MyReflectionClassName", builder.reflectionClassName());
        });
    }
}
