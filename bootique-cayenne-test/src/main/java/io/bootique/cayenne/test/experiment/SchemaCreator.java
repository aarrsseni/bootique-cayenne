package io.bootique.cayenne.test.experiment;

import java.util.Collection;

import com.google.inject.Inject;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.access.DbGenerator;
import org.apache.cayenne.configuration.server.ServerRuntime;

public class SchemaCreator {

    @Inject
    private ServerRuntime serverRuntime;

    public void createSchemas() {
        Collection<DataNode> nodes = serverRuntime.getDataDomain().getDataNodes();
        nodes.forEach(this::createSchema);
    }

    protected void createSchema(DataNode node) {

        // not using Cayenne CreateIfNoSchemaStrategy, as we do not need to check table presence. Rather copy/pasting
        // relevant code from it....
        node.getDataMaps().forEach(map -> {

            DbGenerator generator = new DbGenerator(node.getAdapter(), map, node.getJdbcEventLogger());
            generator.setShouldCreateTables(true);
            generator.setShouldDropTables(false);
            generator.setShouldCreateFKConstraints(true);
            generator.setShouldCreatePKSupport(true);
            generator.setShouldDropPKSupport(false);

            try {
                generator.runGenerator(node.getDataSource());
            } catch (Exception e) {
                throw new RuntimeException("Error creating schema for DataNode: " + node.getName(), e);
            }
        });
    }
}
