package io.github.jonloucks.variants.impl;

import java.util.function.Function;

import static io.github.jonloucks.contracts.api.Checks.nullCheck;
import static io.github.jonloucks.variants.api.Checks.textCheck;
import static java.lang.Character.isDigit;

final class EnumParserImpl<T extends Enum<T>> {
    EnumParserImpl(Class<T> enumClass) {
        this.enumClass = nullCheck(enumClass, "Enum class must be present.");
        this.constants = nullCheck(enumClass.getEnumConstants(), "Unsupported enum class.");
    }
    
    Function<CharSequence, T> compile() {
        return chars -> {
            final CharSequence validChars = textCheck(chars);
            if (validChars.length() == 0) {
                throw new IllegalArgumentException("Enum text must not be empty.");
            }
            try {
                if (isDigit(validChars.charAt(0))) {
                    return constants[Integer.parseInt(validChars.toString())];
                } else {
                    return Enum.valueOf(enumClass, validChars.toString());
                }
            } catch (Exception thrown) {
                throw new IllegalArgumentException("Invalid " + enumClass.getSimpleName() + " value '" + validChars + "'.", thrown);
            }
        };
    }
    
    private final Class<T> enumClass;
    private final T[] constants;
}
