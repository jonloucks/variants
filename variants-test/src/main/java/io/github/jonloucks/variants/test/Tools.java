package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.*;
import io.github.jonloucks.contracts.api.AutoClose;
import io.github.jonloucks.contracts.api.Contracts;
import org.opentest4j.TestAbortedException;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static io.github.jonloucks.variants.api.GlobalVariants.findVariantsFactory;
import static io.github.jonloucks.contracts.test.Tools.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public final class Tools {
    
    public static void clean() {
        io.github.jonloucks.contracts.test.Tools.clean();
        sanitize(()-> {
            final Variants.Config config = new Variants.Config() {};
            if (config.useServiceLoader()) {
                final ServiceLoader<? extends VariantsFactory> loader = ServiceLoader.load(config.serviceLoaderClass());
                loader.reload();
            }
        });
    }
    
    public static void withVariants(Consumer<Variants.Config.Builder> builderConsumer, BiConsumer<Contracts,Variants> block) {
        withContracts(contracts -> {
            final VariantsFactory factory = getVariantsFactory();
            final Variants variants = factory.create(b -> {
                b.contracts(contracts);
                builderConsumer.accept(b);
            });
            try (AutoClose closeVariants = variants.open()) {
                ignore(closeVariants);
                block.accept(contracts, variants);
            }
        });
    }
    
    public static void withVariants(BiConsumer<Contracts,Variants> block) {
        withVariants( b->{}, block);
    }
    
    public static VariantsFactory getVariantsFactory() {
        return getVariantsFactory(Variants.Config.DEFAULT);
    }
    
    public static VariantsFactory getVariantsFactory(Variants.Config config) {
        return findVariantsFactory(config)
            .orElseThrow(() -> new TestAbortedException("Variants Factory not found."));
    }
    
    /**
     * Assert close can be called more than once without producing an exception
     *
     * @param autoClose the AutoClose to close
     * @throws IllegalArgumentException when arguments are null
     */
    public static void assertIdempotent(AutoClose autoClose) {
        final AutoClose validClose = nullCheck(autoClose, "AutoClose must be present.");
        for (int n = 0; n < 7; n++) {
            assertDoesNotThrow(() -> implicitClose(validClose), "AutoClose should be idempotent.");
            assertObject(autoClose); // should not become a landmine
        }
    }
    
    /**
     * Utility class instantiation protection
     */
    private Tools() {
        throw new AssertionError("Illegal constructor call.");
    }
}
