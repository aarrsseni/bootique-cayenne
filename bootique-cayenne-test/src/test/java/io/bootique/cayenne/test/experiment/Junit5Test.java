package io.bootique.cayenne.test.experiment;

import io.bootique.cayenne.test.persistence.Table1;
import io.bootique.cayenne.test.persistence.Table2;
import io.bootique.jdbc.test.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class Junit5Test {

    @RegisterExtension
    static BQRuntimeExtension bqRuntimeExtension = new BQRuntimeExtensionBuilder()
            .args("-c", "classpath:config2.yml")
            .build();

    @RegisterExtension
    static CayenneServerRuntimeExtension cayenneServerRuntimeExtension = new CayenneServerRuntimeExtension();

    @Test
    public void test() {
        CayenneTestDataManager cayenneTestDataManager =
                cayenneServerRuntimeExtension.getCayenneTestDataManager();
        Table t1 = cayenneTestDataManager.getTable(Table1.class);
        Table t2 = cayenneTestDataManager.getTable(Table2.class);

        t1.matcher().assertNoMatches();
        t2.matcher().assertNoMatches();

        t1.insert(1, 2, 3);
        t2.insert(5, "x");

        t1.matcher().assertOneMatch();
        t2.matcher().assertOneMatch();
    }
}
