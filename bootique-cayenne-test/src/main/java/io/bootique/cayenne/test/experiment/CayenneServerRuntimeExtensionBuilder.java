package io.bootique.cayenne.test.experiment;

public class CayenneServerRuntimeExtensionBuilder {

    private Class<?>[] entityTypes;

    public CayenneServerRuntimeExtensionBuilder allEntities() {

        return this;
    }

    public CayenneServerRuntimeExtensionBuilder entities(Class<?>... entityTypes) {
        this.entityTypes = entityTypes;
        return this;
    }

    public CayenneServerRuntimeExtension build() {
        return new CayenneServerRuntimeExtension(entityTypes);
    }
}
