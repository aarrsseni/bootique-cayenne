package io.bootique.cayenne.test.experiment;

import io.bootique.BQRuntime;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.managed.ManagedDataSource;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DataManagerExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        BQRuntime bqRuntime = (BQRuntime) extensionContext
                .getStore(ExtensionContext.Namespace.GLOBAL)
                .get(BQRuntime.class);

        ServerRuntime serverRuntime = bqRuntime.getInstance(ServerRuntime.class);
        SchemaCreator schemaCreator = bqRuntime.getInstance(SchemaCreator.class);
        schemaCreator.createSchemas(serverRuntime);
    }
}
