package io.bootique.cayenne.test.experiment;

import io.bootique.BQRuntime;
import io.bootique.cayenne.test.CayenneTableManager;
import io.bootique.jdbc.test.Table;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.exp.Property;

public class CayenneTestDataManager extends TestDataManager {

    private CayenneTableManager tableManager;
    private ServerRuntime runtime;
    private boolean refreshCayenneCaches;

    protected CayenneTestDataManager(
            ServerRuntime runtime,
            CayenneTableManager tableManager,
            boolean deleteData,
            boolean refreshCayenneCaches,
            Table... tablesInInsertOrder) {

        super(deleteData, tablesInInsertOrder);

        this.runtime = runtime;
        this.tableManager = tableManager;
        this.refreshCayenneCaches = refreshCayenneCaches;
    }

    /**
     * Creates a builder of CayenneTestDataManager.
     *
     * @param runtime {@link BQRuntime} used in the test.
     * @return a new instance of CayenneTestDataManager builder.
     * @since 0.24
     */
    public static CayenneTestDataManagerBuilderExperiment builder(BQRuntime runtime) {
        return new CayenneTestDataManagerBuilderExperiment(runtime);
    }

    public static Table createTableModel(BQRuntime runtime, Class<?> entityType) {
        return CayenneModelUtilsExperiment.createTableModel(runtime, entityType);
    }

    public static Table createTableModel(BQRuntime runtime, String tableName) {
        return CayenneModelUtilsExperiment.createTableModel(runtime, tableName);
    }

    @Override
    public void beforeAll() {
        super.beforeAll();
        if (refreshCayenneCaches) {
            refreshCayenneCaches();
        }
    }

    public Table getTable(Class<?> entityType) {
        return tableManager.getTable(entityType);
    }

    /**
     * Returns a Table related to a given entity via the specified relationship. Useful for navigation to join tables
     * that are not directly mapped to Java classes.
     *
     * @param entityType
     * @param relationship
     * @param tableIndex   An index in a list of tables spanned by 'relationship'. Index of 0 corresponds to the target
     *                     DbEntity of the first object in a chain of DbRelationships for a given ObjRelationship.
     * @return a Table related to a given entity via the specified relationship.
     * @since 0.24
     */
    public Table getRelatedTable(Class<?> entityType, Property<?> relationship, int tableIndex) {
        return tableManager.getRelatedTable(entityType, relationship, tableIndex);
    }

    /**
     * @param entityType
     * @param relationship
     * @return a Table related to a given entity via the specified relationship.
     * @since 0.24
     */
    public Table getRelatedTable(Class<?> entityType, Property<?> relationship) {
        return tableManager.getRelatedTable(entityType, relationship, 0);
    }

    /**
     * @return Cayenne {@link ServerRuntime} underlying this data manager.
     * @since 1.0.RC1
     */
    public ServerRuntime getRuntime() {
        return runtime;
    }

    public void refreshCayenneCaches() {

        DataDomain domain = runtime.getDataDomain();

        if (domain.getSharedSnapshotCache() != null) {
            domain.getSharedSnapshotCache().clear();
        }

        if (domain.getQueryCache() != null) {
            // note that this also flushes per-context caches .. at least with JCache implementation
            domain.getQueryCache().clear();
        }
    }

}
