package io.bootique.cayenne.test.experiment;

import io.bootique.BQModuleProvider;
import io.bootique.BootiqueException;

import java.util.HashSet;
import java.util.Set;

public class BQRuntimeExtensionBuilder {

    private String[] args;
    private Set<com.google.inject.Module> bQModules;
    private Set<BQModuleProvider> bqModuleProviders;

    public BQRuntimeExtensionBuilder() {
        this.bQModules = new HashSet<>();
        this.bqModuleProviders = new HashSet<>();
    }

    public BQRuntimeExtensionBuilder args(String... args) {
        this.args = args;
        return this;
    }

    public BQRuntimeExtensionBuilder module(com.google.inject.Module module) {
        bQModules.add(module);
        return this;
    }

    public BQRuntimeExtensionBuilder module(BQModuleProvider bqModuleProvider) {
        bqModuleProviders.add(bqModuleProvider);
        return this;
    }

    public BQRuntimeExtension build() {
        if(args == null || args.length == 0) {
            throw new BootiqueException(1, "Try to pass empty config to bq runtime.");
        }
        return new BQRuntimeExtension(args, bQModules);
    }
}
