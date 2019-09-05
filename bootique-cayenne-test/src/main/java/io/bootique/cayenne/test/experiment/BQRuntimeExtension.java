package io.bootique.cayenne.test.experiment;

import io.bootique.BQRuntime;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BQRuntimeExtension implements BeforeAllCallback {

    private BQTestFactory bqTestFactory;
    private String[] args;

    public BQRuntimeExtension(String... args) {
        this.args = args;
        this.bqTestFactory = new BQTestFactory();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        BQRuntime bqRuntime = bqTestFactory.app(args)
                .autoLoadModules()
                .createRuntime();
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(BQRuntime.class, bqRuntime);
    }
}
