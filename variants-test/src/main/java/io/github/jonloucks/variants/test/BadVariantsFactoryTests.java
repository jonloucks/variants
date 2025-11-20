package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.Variants;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static io.github.jonloucks.contracts.test.Tools.assertThrown;
import static org.junit.jupiter.api.Assertions.assertFalse;

public interface BadVariantsFactoryTests {
    @Test
    default void badContractsFactory_HasProtectedConstructor() throws Throwable {
        final Class<?> klass = Class.forName(BadVariantsFactory.class.getCanonicalName());
        final Constructor<?> constructor = klass.getDeclaredConstructor();
        constructor.setAccessible(true);
        final int modifiers = constructor.getModifiers();
        
        assertFalse(Modifier.isPublic(modifiers), "constructor should not be public.");
    }
    
    @Test
    default void badContractsFactory_HasPrivateConstructor() throws Throwable {
        final BadVariantsFactory badContractsFactory = new BadVariantsFactory();
        final Variants.Config config = new Variants.Config(){};
        assertThrown(Exception.class, () -> badContractsFactory.create(config));
    }
}
