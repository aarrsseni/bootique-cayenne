/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.cayenne.test;

import io.bootique.cayenne.test.experiment.BQRuntimeExtension;
import io.bootique.cayenne.test.experiment.BQRuntimeExtensionBuilder;
import io.bootique.cayenne.test.experiment.DataManagerExtension;
import io.bootique.cayenne.test.persistence.Table1;
import io.bootique.jdbc.test.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class CayenneTestDataManagerTxIT {

    @RegisterExtension
    static BQRuntimeExtension bqRuntimeExtension = new BQRuntimeExtensionBuilder()
            .args("-c", "classpath:config-noautocommit.yml")
            .build();

    @RegisterExtension
    static DataManagerExtension dataManagerExtension = new DataManagerExtension();

    @Test
    public void testDataSourceDoesNotAutocommit() {
        CayenneTestDataManager dataManager = CayenneTestDataManager
                .builder(bqRuntimeExtension.getBqRuntime())
                .doNotDeleteData()
                .entities(Table1.class)
                .build();

        Table t1 = dataManager.getTable(Table1.class);

        t1.matcher().assertNoMatches();
        t1.insert(1, 2, 3);
        t1.matcher().assertOneMatch();
    }
}
