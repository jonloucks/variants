package io.github.jonloucks.variants.api;

import io.github.jonloucks.contracts.api.*;

import java.util.function.Supplier;

/**
 * The Variants API
 */
public interface Variants extends AutoOpen {
    /**
     * Access the current Variants implementation
     */
    Contract<Variants> CONTRACT = Contract.create(Variants.class);
    
    // load from disk. for example app.properties, app.yml
    // load from gradle project properties
    // load from System.env ( how to handle permission errors?)
    // load from System.getProperty (how to handle permission errors?)
    // auto naming schemes. APP_PROPERTY, app.property, ORG_GRADLE_PROJECT_
    // where do actual values get stored? or is that not the responsibility of Variants
    // are different sources opt-in or opt-out
    
    // how to handle links?

    // are links and fallbacks only executed if all scopes fail? yes
    
    /**
     * The configuration used to create a new Variants instance.
     */
    interface Config {
        
        /**
         * The default configuration used when creating a new Variants instance
         */
        Config DEFAULT = new Config() {};
        
        /**
         * @return if true, reflection might be used to locate the VariantsFactory
         */
        default boolean useReflection() {
            return true;
        }
        
        /**
         * @return the class name to use if reflection is used to find the VariantsFactory
         */
        default String reflectionClassName() {
            return "io.github.jonloucks.variants.impl.VariantsFactoryImpl";
        }
        
        /**
         * @return if true, the ServiceLoader might be used to locate the VariantsFactory
         */
        default boolean useServiceLoader() {
            return true;
        }
        
        /**
         * @return the class name to load from the ServiceLoader to find the VariantsFactory
         */
        default Class<? extends VariantsFactory> serviceLoaderClass() {
            return VariantsFactory.class;
        }
        
        /**
         * @return the contracts, some use case have their own Contracts instance.
         */
        default Contracts contracts() {
            return GlobalContracts.getInstance();
        }
        
        /**
         * The Variants configuration
         */
        interface Builder extends Config {
            
            /**
             * Variants Variant Builder
             */
            Contract<Supplier<Builder>> FACTORY = Contract.create("Variants Variant Builder Factory");
            
            /**
             * @param useReflection enables or disables locating VariantsFactory implementation by reflection
             * @return this builder
             */
            Builder useReflection(boolean useReflection);
            
            /**
             * @param useServiceLoader the ServiceLoader might be used to locate the VariantsFactory
             * @return this builder
             */
            Builder useServiceLoader(boolean useServiceLoader);
            
            /**
             * @param contracts the Contracts to use
             * @return this builder
             */
            Builder contracts(Contracts contracts);
   
            /**
             * @param reflectionClassName the class name to use if reflection is used to find the ContractsFactory
             * @return this builder
             */
            Builder reflectionClassName(String reflectionClassName);
            
            /**
             * @param serviceLoaderClass the class name to load from the ServiceLoader to find the ContractsFactory
             * @return this builder
             */
            Builder serviceLoaderClass(Class<? extends VariantsFactory> serviceLoaderClass);
        }
    }
}
