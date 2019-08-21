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

package io.bootique.cayenne.test.experiment;

import io.bootique.BQRuntime;
import io.bootique.cayenne.test.CayenneTableManager;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.exp.Property;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.EntityResolver;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @since 0.24
 */
public class CayenneTestDataManagerBuilderExperiment {

    private BQRuntime runtime;
    private boolean deleteData;
    private boolean refreshCayenneCaches;
    private Set<DbEntity> dbEntities;
    private Set<DbEntity> dbEntityGraphRoots;

    private EntityResolver resolver;

    CayenneTestDataManagerBuilderExperiment(BQRuntime runtime) {
        this.runtime = runtime;
        this.deleteData = true;
        this.refreshCayenneCaches = true;

        ServerRuntime serverRuntime = runtime.getInstance(ServerRuntime.class);
        this.resolver = serverRuntime.getDataDomain().getEntityResolver();
        this.dbEntities = new HashSet<>();
        this.dbEntityGraphRoots = new HashSet<>();
    }

    public CayenneTestDataManager build() {

        Set<DbEntity> mergedEntities = mergeEntities();

        return new CayenneTestDataManager(
                runtime.getInstance(ServerRuntime.class),
                runtime.getInstance(CayenneTableManager.class),
                deleteData,
                refreshCayenneCaches,
                CayenneModelUtilsExperiment.tablesInInsertOrder(runtime, mergedEntities));
    }

    public CayenneTestDataManagerBuilderExperiment doNotDeleteData() {
        this.deleteData = false;
        return this;
    }

    public CayenneTestDataManagerBuilderExperiment doNoRefreshCayenneCaches() {
        this.refreshCayenneCaches = false;
        return this;
    }

    /**
     * Creates Tables for all entities present in the runtime.
     *
     * @return this builder instance
     * @since 1.0.RC1
     */
    public CayenneTestDataManagerBuilderExperiment allEntities() {
        dbEntities.addAll(resolver.getDbEntities());
        return this;
    }

    public CayenneTestDataManagerBuilderExperiment entity(Class<?> entityType) {
        return entities(entityType);
    }

    public CayenneTestDataManagerBuilderExperiment entities(Class<?>... entityTypes) {

        Objects.requireNonNull(entityTypes);
        for (Class<?> type : entityTypes) {
            dbEntities.add(CayenneModelUtilsExperiment.getDbEntity(resolver, type));
        }

        return this;
    }

    public CayenneTestDataManagerBuilderExperiment entitiesAndDependencies(Class<?>... entityTypes) {

        Objects.requireNonNull(entityTypes);
        for (Class<?> type : entityTypes) {
            dbEntityGraphRoots.add(CayenneModelUtilsExperiment.getDbEntity(resolver, type));
        }

        return this;
    }

    public CayenneTestDataManagerBuilderExperiment relatedTables(Class<?> entityType, Property<?> relationship) {
        CayenneModelUtilsExperiment.getRelatedDbEntities(resolver, entityType, relationship).forEach(dbEntities::add);
        return this;
    }

    public CayenneTestDataManagerBuilderExperiment table(String tableName) {
        return tables(tableName);
    }

    public CayenneTestDataManagerBuilderExperiment tables(String... tableNames) {

        Objects.requireNonNull(tableNames);
        for (String table : tableNames) {
            dbEntities.add(CayenneModelUtilsExperiment.getDbEntity(resolver, table));
        }

        return this;
    }

    public CayenneTestDataManagerBuilderExperiment tablesAndDependencies(String... tableNames) {

        Objects.requireNonNull(tableNames);
        for (String table : tableNames) {
            dbEntityGraphRoots.add(CayenneModelUtilsExperiment.getDbEntity(resolver, table));
        }

        return this;
    }

    private Set<DbEntity> mergeEntities() {
        if (dbEntityGraphRoots.isEmpty()) {
            return dbEntities;
        }

        Set<DbEntity> merged = ModelDependencyResolverExperiment.resolve(dbEntityGraphRoots);
        merged.addAll(dbEntities);
        return merged;
    }

}
