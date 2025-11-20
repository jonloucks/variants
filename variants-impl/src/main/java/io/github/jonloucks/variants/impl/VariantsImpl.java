package io.github.jonloucks.variants.impl;

import io.github.jonloucks.contracts.api.AutoClose;
import io.github.jonloucks.contracts.api.Repository;
import io.github.jonloucks.variants.api.Variants;

import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.jonloucks.contracts.api.Checks.configCheck;
import static io.github.jonloucks.contracts.api.Checks.nullCheck;

final class VariantsImpl implements Variants {
    
    @Override
    public AutoClose open() {
        if (openState.compareAndSet(false, true)) {
            return realOpen();
        }
        return AutoClose.NONE;
    }
    
    VariantsImpl(Config config, Repository repository, boolean autoOpen) {
        //noinspection ResultOfMethodCallIgnored
        configCheck(config);
        final Repository validRepository = nullCheck(repository, "Repository must be present.");
        this.closeRepository = autoOpen ? validRepository.open() : AutoClose.NONE;
    }
    
    private AutoClose realOpen() {
        return this::close;
    }

    private void close() {
        if (openState.compareAndSet(true, false)) {
            realClose();
        }
    }
    
    private void realClose() {
        closeRepository.close();
    }
    
    private final AutoClose closeRepository;
    private final AtomicBoolean openState = new AtomicBoolean();
}
