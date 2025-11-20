package io.github.jonloucks.variants.test;

import io.github.jonloucks.variants.api.VariantException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.github.jonloucks.contracts.test.Tools.assertIsSerializable;
import static io.github.jonloucks.contracts.test.Tools.assertThrown;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"ThrowableNotThrown", "CodeBlock2Expr"})
public interface ExceptionTests {
    
    @Test
    default void exception_rethrow_WithNullThrown_Throws() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            VariantException.rethrow(null);
        });
        
        assertThrown(thrown, "Thrown must be present.");
    }
 
    @Test
    default void exception_rethrow_RuntimeException_ThrowsOriginal() {
        final RuntimeException problem = new RuntimeException("Oh My.");
        final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            throw VariantException.rethrow(problem);
        });
        
        assertSame(problem, thrown);
    }
    
    @Test
    default void exception_rethrow_Error_ThrowsOriginal() {
        final Error problem = new Error("Oh My.");
        final Error thrown = assertThrows(Error.class, () -> {
            throw VariantException.rethrow(problem);
        });
        
        assertSame(problem, thrown);
    }
    
    @Test
    default void exception_rethrow_Unchecked_ThrowsVariantException() {
        final IOException problem = new IOException("Oh My.");
        final VariantException thrown = assertThrows(VariantException.class, () -> {
            throw VariantException.rethrow(problem);
        });
        
        assertThrown(thrown, problem,"Oh My.");
    }

    @Test
    default void exception_VariantException_WithNullMessage_Throws() {
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new VariantException(null);
        });
        
        assertThrown(thrown);
    }
    
    @Test
    default void exception_VariantException_IsSerializable() {
        assertIsSerializable(VariantException.class);
    }
    
    @Test
    default void exception_VariantException_WithValid_Works() {
        final VariantException exception = new VariantException("Abc.");
        
        assertThrown(exception, "Abc.");
    }
    
    @Test
    default void exception_VariantException_WithCause_Works() {
        final OutOfMemoryError cause = new OutOfMemoryError("Out of memory.");
        final VariantException exception = new VariantException("Abc.", cause);
        
        assertThrown(exception, cause, "Abc.");
    }
}
