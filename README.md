# Variants

A flexible, type-safe Java configuration library for managing application settings across multiple sources.

## Overview

Variants provides a powerful abstraction for configuration management with three core concepts:

1. **VariantSource** - Retrieves configuration text from any source (properties files, environment variables, system properties, or custom implementations)
2. **Environment** - Discovers configuration values by performing breadth-first search across multiple sources in priority order
3. **Variant** - A typed configuration identifier that specifies keys, parsers, fallback values, and cascading defaults

## Features

- **Type-safe configuration** - Parse values to any Java type (String, Duration, Integer, custom types, etc.)
- **Multiple sources** - Combine system properties, environment variables, properties files, maps, and custom sources
- **Priority ordering** - Sources are searched in the order they are added
- **Cascading defaults** - Link variants together for hierarchical configuration
- **Flexible key mapping** - Support multiple keys per variant with fallback search order
- **Optional values** - Built-in support for optional configurations with fallback values

## Quick Start

### Simple Properties Example

```java
// Create properties for the example
Properties properties = new Properties();
properties.setProperty("greeting", "Hello");

// Create an environment
Environment environment = GlobalVariants.createEnvironment(
    b -> b.addPropertiesSource(properties));

// Create a variant with the appropriate key and parser
Variant<String> greeting = GlobalVariants.createVariant(
    b -> b
        .key("greeting")                      // the property key
        .parser(CharSequence::toString)       // CharSequence to String
);

// Retrieve the value
assertEquals("Hello", environment.getVariance(greeting));
```java
// A custom source returns text for specific keys
VariantSource customSource = key -> 
    "YOUR_TIMEOUT".equals(key) ? Optional.of("PT30S") : Optional.empty();
  
// Create an environment with multiple sources (searched in order)
Environment environment = GlobalVariants.createEnvironment(
    b -> b
        .addSystemEnvironmentSource()           // System.getenv
        .addSystemPropertiesSource()            // System.getProperty
        .addPropertiesSource(new Properties())  // Properties file
        .addMapSource(new HashMap())            // Map<String, ?>
        .addSingletonSource("key", "value")     // Single key-value pair
        .addSource(customSource)                // Custom source
);

// Create a general timeout variant with a fallback
Variant<Duration> generalTimeout = GlobalVariants.createVariant(
    b -> b
        .keys("sun.net.client.defaultConnectTimeout")
        .parser(c -> Duration.ofMillis(parseInt(c.toString())))
        .fallback(() -> Duration.ofMinutes(5))  // Default if not found
);

// Create a specific timeout that cascades to the general timeout
Variant<Duration> yourTimeout = GlobalVariants.createVariant(
    b -> b
        .name("Your timeout")                                      // User-facing name
        .description("How long you should wait for anything.")     // Description
        .keys("YOUR_TIMEOUT", "your.timeout")                      // Multiple keys to search
        .parser(Duration::parse)                                   // Parse ISO-8601 duration
        .link(generalTimeout)                                      // Use generalTimeout if not found
);

// Retrieves "PT30S" from customSource
assertEquals(Duration.ofSeconds(30), environment.getVariance(yourTimeout)); no value is the link value is used. Good for cascading defaults
);Key Concepts

### Configuration Sources
Sources are searched in the order they are added. The first source that returns a non-empty value for a key wins:
- **System Environment** - `System.getenv()`
- **System Properties** - `System.getProperty()`
- **Properties** - `java.util.Properties`
- **Map** - Any `Map<String, ?>`
- **Custom** - Implement `VariantSource` interface

### Variant Resolution Order
When resolving a variant, the library:
1. Searches all sources for each key (in key order)
2. If no value found, checks the linked variant (if any)
3. If still no value, uses the fallback (if specified)
4. Otherwise returns empty or throws exception (depending on method used)

### Type Safety
Variants are strongly typed and use parser functions to convert text to the desired type:
```java
Variant<Integer> port = GlobalVariants.createVariant(
    b -> b.key("server.port").parser(Integer::parseInt)
);

Variant<Duration> timeout = GlobalVariants.createVariant(
    b -> b.key("timeout").parser(Duration::parse)
);
```

## Documentation and Reports

- **[Java API Documentation](https://jonloucks.github.io/variants/javadoc/)** - Complete JavaDoc reference
- **[Test Coverage Report](https://jonloucks.github.io/variants/jacoco/)** - JaCoCo coverage metrics

## Project Status and Reports
[Java API](https://jonloucks.github.io/variants/javadoc/)

[Java Test Coverage](https://jonloucks.github.io/variants/jacoco/)

## Badges
[![OpenSSF Best Practices](https://www.bestpractices.dev/projects/11504/badge)](https://www.bestpractices.dev/projects/11504)
[![Coverage Badge](https://raw.githubusercontent.com/jonloucks/variants/refs/heads/badges/main-coverage.svg)](https://jonloucks.github.io/variants/jacoco/)
[![Javadoc Badge](https://raw.githubusercontent.com/jonloucks/variants/refs/heads/badges/main-javadoc.svg)](https://jonloucks.github.io/variants/javadoc/)
