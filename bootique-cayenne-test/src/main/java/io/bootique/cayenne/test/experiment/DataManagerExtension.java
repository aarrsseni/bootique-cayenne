package io.bootique.cayenne.test.experiment;

import io.bootique.BQRuntime;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DataManagerExtension implements BeforeAllCallback {

    private Set<Class<?>> entityTypes;
    private CayenneTestDataManager cayenneTestDataManager;
    private boolean allEntities;

    public DataManagerExtension() {
        this.entityTypes = new HashSet<>();
        this.allEntities = false;
    }

    public DataManagerExtension withEntities(Class<?>... entityTypes) {
        this.entityTypes.addAll(Arrays.asList(entityTypes));
        return this;
    }

    public DataManagerExtension withAllEntities() {
        allEntities = true;
        return this;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        ExtensionContext.Store store = extensionContext
                .getStore(ExtensionContext.Namespace.GLOBAL);
        BQRuntime bqRuntime = (BQRuntime) store.get(BQRuntime.class);

        SchemaCreator schemaCreator = bqRuntime.getInstance(SchemaCreator.class);
        schemaCreator.createSchemas();

        CayenneTestDataManagerBuilderExperiment cayenneTestDataManagerBuilderExperiment =
                new CayenneTestDataManagerBuilderExperiment(bqRuntime);
        if(allEntities) {
            cayenneTestDataManagerBuilderExperiment.allEntities();
        } else {
            cayenneTestDataManagerBuilderExperiment.entities(entityTypes.toArray(new Class<?>[0]));
        }

        cayenneTestDataManager = cayenneTestDataManagerBuilderExperiment.build();
        store.put(CayenneTestDataManager.class, cayenneTestDataManager);
    }

    public CayenneTestDataManager getCayenneTestDataManager() {
        return cayenneTestDataManager;
    }
}
