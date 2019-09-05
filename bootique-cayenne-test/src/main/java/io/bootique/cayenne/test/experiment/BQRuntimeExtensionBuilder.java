package io.bootique.cayenne.test.experiment;

import io.bootique.BootiqueException;

public class BQRuntimeExtensionBuilder {

    private String[] args;

    public BQRuntimeExtensionBuilder args(String... args) {
        this.args = args;
        return this;
    }

    public BQRuntimeExtension build() {
        if(args == null || args.length == 0) {
            throw new BootiqueException(1, "Try to pass empty config to bq runtime.");
        }
        return new BQRuntimeExtension(args);
    }
}
