package io.bootique.cayenne.test.experiment;

import com.google.inject.Module;
import io.bootique.BQRuntime;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Set;

public class BQRuntimeExtension implements BeforeAllCallback {

    private BQTestFactory bqTestFactory;
    private String[] args;
    private Set<Module> moduleSet;
    private BQRuntime bqRuntime;

    public BQRuntimeExtension(String[] args, Set<Module> modules) {
        this.args = args;
        this.moduleSet = modules;
        this.bqTestFactory = new BQTestFactory();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        BQTestFactory.Builder builder = bqTestFactory.app(args).autoLoadModules();
        moduleSet.forEach(builder::module);
        bqRuntime = builder.createRuntime();
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(BQRuntime.class, bqRuntime);
    }

    public BQRuntime getBqRuntime() {
        return bqRuntime;
    }
}
