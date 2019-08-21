package io.bootique.cayenne.test.experiment;

import java.util.HashMap;
import java.util.Map;

import io.bootique.jdbc.test.Table;
import io.bootique.jdbc.test.matcher.TableMatcher;

public class TestDataManager {

    private Table[] tablesInInsertOrder;
    private Map<String, Table> tables;
    private boolean deleteData;


    public TestDataManager(boolean deleteData, Table... tablesInInsertOrder) {
        this.deleteData = deleteData;
        this.tablesInInsertOrder = tablesInInsertOrder != null ? tablesInInsertOrder : new Table[0];

        tables = new HashMap<>();
        for (Table t : this.tablesInInsertOrder) {
            tables.put(t.getName(), t);
        }
    }

    public void beforeAll() {
        if (deleteData) {
            deleteData();
        }
    }

    /**
     * Deletes data from the managed tables.
     *
     * @since 0.24
     */
    public void deleteData() {
        for (int i = tablesInInsertOrder.length - 1; i >= 0; i--) {
            tablesInInsertOrder[i].deleteAll();
        }
    }

    /**
     * @param tableName the name of a table whose data we'd like to check.
     * @return a new TableMatcher for the specified table.
     * @since 0.24
     */
    public TableMatcher matcher(String tableName) {
        return getTable(tableName).matcher();
    }

    public Table getTable(String tableName) {
        return tables.computeIfAbsent(tableName, n -> {
            throw new IllegalArgumentException("Unknown table name: " + n);
        });
    }

    /**
     * @return an array of registered tables in insert order.
     * @since 0.24
     */
    public Table[] getTablesInInsertOrder() {
        return tablesInInsertOrder;
    }
}
