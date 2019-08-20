package io.bootique.cayenne.test;

import io.bootique.cayenne.test.experiment.BQRuntimeExtension;
import io.bootique.cayenne.test.experiment.DataManagerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;


public class Junit5Test {

    @RegisterExtension
    static BQRuntimeExtension bqRuntimeExtension =
            new BQRuntimeExtension("classpath:config2.yml");

    @RegisterExtension
    static DataManagerExtension dataManagerExtension = new DataManagerExtension();

    @Test
    public void test() {
        System.out.println("Test from test");
    }

}
