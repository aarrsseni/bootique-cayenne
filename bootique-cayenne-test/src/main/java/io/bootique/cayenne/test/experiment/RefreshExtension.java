package io.bootique.cayenne.test.experiment;

import io.bootique.BQRuntime;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class RefreshExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        CayenneTestDataManager cayenneTestDataManager = (CayenneTestDataManager) extensionContext
                .getStore(ExtensionContext.Namespace.GLOBAL)
                .get(CayenneTestDataManager.class);

        cayenneTestDataManager.before();
    }
}
