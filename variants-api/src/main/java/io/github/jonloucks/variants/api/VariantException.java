package io.github.jonloucks.variants.api;

import static io.github.jonloucks.contracts.api.Checks.messageCheck;
import static io.github.jonloucks.contracts.api.Checks.nullCheck;

/**
 * Runtime exception thrown for Variants related problems.
 * For example, when Variants fails to initialize.
 */
public class VariantException extends RuntimeException {

    /**
     * Passthrough for {@link RuntimeException#RuntimeException(String)}
     *
     * @param message the message for this exception
     */
    public VariantException(String message) {
        this(message, null);
    }
    
    /**
     * Passthrough for {@link RuntimeException#RuntimeException(String, Throwable)}
     *
     * @param message the message for this exception
     * @param thrown  the cause of this exception, null is allowed
     */
    public VariantException(String message, Throwable thrown) {
        super(messageCheck(message), thrown);
    }
    
    /**
     * Rethrows a caught exception or a VariantException if unchecked
     *
     * @param thrown the previously caught exception
     * @return Possibly a new VariantException which should be rethrown
     * @throws Error iif original thrown is an error
     * @throws RuntimeException when thrown is a RuntimeException
     * @throws VariantException if thrown is a checked exception
     */
    @SuppressWarnings("UnusedReturnValue")
    public static VariantException rethrow(Throwable thrown) throws Error, RuntimeException {
        final Throwable validThrown = nullCheck(thrown, "Thrown must be present.");

        if (validThrown instanceof Error) {
            throw (Error) validThrown;
        } else if (validThrown instanceof RuntimeException) {
            throw (RuntimeException) validThrown;
        }
        
        return new VariantException(validThrown.getMessage(), validThrown);
    }
    
    /**
     * Imposed serialization from RuntimeException
     */
    @SuppressWarnings({"serial", "RedundantSuppression"})
    private static final long serialVersionUID = 1L;
}
