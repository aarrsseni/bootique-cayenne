package io.bootique.cayenne.test.experiment;

import io.bootique.BQRuntime;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CayenneServerRuntimeExtension implements BeforeAllCallback {

    private CayenneTestDataManager cayenneTestDataManager;
    private Class<?>[] entityTypes;

    public CayenneServerRuntimeExtension(Class<?>... entityTypes) {
        this.entityTypes = entityTypes;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        BQRuntime bqRuntime = (BQRuntime) extensionContext
                .getStore(ExtensionContext.Namespace.GLOBAL)
                .get(BQRuntime.class);

        SchemaCreator schemaCreator = bqRuntime.getInstance(SchemaCreator.class);
        schemaCreator.createSchemas();

        cayenneTestDataManager =
                new CayenneTestDataManagerBuilderExperiment(bqRuntime)
                        .entities(entityTypes)
                        .build();
        cayenneTestDataManager.beforeAll();
    }

    public CayenneTestDataManager getCayenneTestDataManager() {
        return cayenneTestDataManager;
    }
}
