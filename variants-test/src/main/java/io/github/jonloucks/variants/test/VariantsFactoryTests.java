package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.Variants;
import io.github.jonloucks.variants.api.VariantsFactory;
import io.github.jonloucks.contracts.api.AutoClose;
import io.github.jonloucks.contracts.api.Contracts;
import io.github.jonloucks.contracts.api.Repository;
import org.junit.jupiter.api.Test;

import static io.github.jonloucks.variants.test.Tools.*;
import static io.github.jonloucks.contracts.test.Tools.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public interface VariantsFactoryTests {
    
    @Test
    default void variantsFactory_install_WithNullConfig_Throws() {
        withContracts(contracts -> {
            final Variants.Config config = new Variants.Config() {
                @Override
                public Contracts contracts() {
                    return contracts;
                }
            };
            final Repository repository = contracts.claim(Repository.FACTORY).get();
            final VariantsFactory variantsFactory = getVariantsFactory(config);
            
            assertThrown(IllegalArgumentException.class,
                () -> variantsFactory.install(null, repository));
        });
    }
    
    @Test
    default void variantsFactory_install_WithNullRepository_Throws() {
        withContracts(contracts -> {
            final Variants.Config config = new Variants.Config() {
                @Override
                public Contracts contracts() {
                    return contracts;
                }
            };
            final VariantsFactory variantsFactory = getVariantsFactory(config);
            
            assertThrown(IllegalArgumentException.class,
                () -> variantsFactory.install(config, null));
        });
    }
    
    @Test
    default void variantsFactory_install_AlreadyBound_DoesNotThrow   () {
        withVariants(b -> {}, (contracts, variants)-> {
            final Variants.Config config = new Variants.Config() {
                @Override
                public Contracts contracts() {
                    return contracts;
                }
            };
            
            final Repository repository = contracts.claim(Repository.FACTORY).get();
            final VariantsFactory variantsFactory = getVariantsFactory(config);
            
            assertDoesNotThrow(() -> variantsFactory.install(config, repository));
        });
    }
    
    @Test
    default void variantsFactory_install_WithValid_Works() {
        withContracts(contracts -> {
            final Variants.Config config = new Variants.Config() {
                @Override
                public Contracts contracts() {
                    return contracts;
                }
            };
            
            final Repository repository = contracts.claim(Repository.FACTORY).get();
            final VariantsFactory variantsFactory = getVariantsFactory(config);
            
            variantsFactory.install(config, repository);
            
            try (AutoClose closeRepository = repository.open()) {
                ignore(closeRepository);
                assertObject(contracts.claim(Variants.CONTRACT));
            }
        });
    }
}
