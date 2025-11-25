# variants
### Java Variants Configuration Library, providing features not in the Java runtime
#### 1. VariantSource, Retrieve variance in text format from any source
#### 2. Environment, Discovers configuration values across multiple sources
#### 3. Variant, Identifier providing information on how to retrieve a configuration value

##### Java properties example
````
// Create properties for the example
Properties properties = new Properties();
properties.setProperty("greeting", "Hello");

// Create an example environment
Environment environment = GlobalVariants.createEnvironment(
    b -> b.addPropertiesSource(properties));

// Create a variant with the appropriate key and parser
Variant<String> greeting = GlobalVariants.createVariant(
    b -> b                                    // builder
        .key("greeting")                      // the property key
        .parser(CharSequence::toString)       // CharSequence to String
);

// Expecting "Hello"
assertEquals("Hello", environment.getVariance(greeting));
````

##### Try everything example:
``` 
// A source takes a key and returns non-null value or empty.
VariantSource customSource = key -> "YOUR_TIMEOUT".equals(key) ? Optional.of("PT30S") : Optional.empty();
  
// Create an example environment
Environment environment = GlobalVariants.createEnvironment(
    b -> b // builder
        .addSystemEnvironmentSource()                               // opt-in; add System.getenv source
        .addSystemPropertiesSource()                                // opt-in; add System.getProperty source
        .addPropertiesSource(new Properties())                      // opt-in; add properties source
        .addMapSource(Collections.singletonMap("key", "value"))     // opt-in; add map source
        .addSource(customSource)                                    // opt-in; custom source
);

// create a general timeout
Variant<Duration> generalTimeout = GlobalVariants.createVariant(
    b -> b // builder
        .keys("sun.net.client.defaultConnectTimeout")               // search keys
        .parser(c -> Duration.ofMillis(parseInt(c.toString())))     // parse from millis
        .fallback(() -> Duration.ofMinutes(5))                      // if java ever removes the property
);

// create a specific timeout that defaults to the general timeout
Variant<Duration> yourTimeout = GlobalVariants.createVariant(
    b -> b // builder
        .name("Your timeout")                                       // opt-in; specify a user facing name
        .description("How long you should wait for anything.")      // opt-in; description
        .keys("YOUR_TIMEOUT", "your.timeout")                       // opt-in; zero or more keys
        .parser(Duration::parse)                                    // opt-in; when using keys, the parser is required to convert values
        .link(generalTimeout)                                       // opt-in; if no value is the link value is used. Good for cascading defaults
);
// expecting 30 minute timeout
assertEquals(Duration.ofSeconds(30), environment.getVariance(yourTimeout));      
```

## Documentation and Reports
[Java API](https://jonloucks.github.io/variants/javadoc/)

[Java Test Coverage](https://jonloucks.github.io/variants/jacoco/)

## Badges
[![Coverage Badge](https://raw.githubusercontent.com/jonloucks/variants/refs/heads/badges/main-coverage.svg)](https://jonloucks.github.io/variants/jacoco/)
[![Javadoc Badge](https://raw.githubusercontent.com/jonloucks/variants/refs/heads/badges/main-javadoc.svg)](https://jonloucks.github.io/variants/javadoc/)
