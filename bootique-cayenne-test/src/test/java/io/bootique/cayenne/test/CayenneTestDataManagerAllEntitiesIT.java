package io.bootique.cayenne.test;

import io.bootique.cayenne.test.experiment.CayenneTestDataManager;
import io.bootique.cayenne.test.experiment.*;
import io.bootique.cayenne.test.persistence.Table1;
import io.bootique.cayenne.test.persistence.Table2;
import io.bootique.jdbc.test.Table;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CayenneTestDataManagerAllEntitiesIT {

    @RegisterExtension
    static BQRuntimeExtension bqRuntimeExtension = new BQRuntimeExtensionBuilder()
            .args("-c", "classpath:config2.yml")
            .build();

    @RegisterExtension
    static DataManagerExtension dataManagerExtension = new DataManagerExtension()
            .withAllEntities();

    @RegisterExtension
    static RefreshExtension refreshExtension = new RefreshExtension();

    private static CayenneTestDataManager dataManager;

    @BeforeAll
    public static void getDataManager() {
        dataManager = dataManagerExtension.getCayenneTestDataManager();
    }

    @Test
    public void testNoSuchTable() {
        assertThrows(IllegalArgumentException.class, () -> dataManager.getTable(String.class));
    }

    @Test
    public void test1() {

        Table t1 = dataManager.getTable(Table1.class);
        Table t2 = dataManager.getTable(Table2.class);

        t1.matcher().assertNoMatches();
        t2.matcher().assertNoMatches();

        t1.insert(1, 2, 3);
        t2.insert(5, "x");

        t1.matcher().assertOneMatch();
        t2.matcher().assertOneMatch();
    }

    @Test
    public void test2() {

        Table t1 = dataManager.getTable(Table1.class);
        Table t2 = dataManager.getTable(Table2.class);

        t1.matcher().assertNoMatches();
        t2.matcher().assertNoMatches();

        t1.insert(4, 5, 6);
        t2.insert(7, "y");

        t1.matcher().assertOneMatch();
        t2.matcher().assertOneMatch();
    }
}
