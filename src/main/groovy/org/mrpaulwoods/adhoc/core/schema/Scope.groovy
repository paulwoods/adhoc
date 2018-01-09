package org.mrpaulwoods.adhoc.core.schema

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Scope implements Serializable {

    static final long serialVersionUID = 0

    String name

    private Scope() {}

    static final scopes = Arrays.asList(
            new Scope(name: "NONE"),
            new Scope(name: "ONE"),
            new Scope(name: "TWO"),
            new Scope(name: "LIST"))

    static Scope findByName(String name) {
        Scope scope = scopes.find { it.name == name }
        if (scope) {
            scope
        } else {
            throw new NotFoundException(name)
        }
    }

    static class NotFoundException extends AdhocException {
        NotFoundException(String name) {
            super("Scope not found: " + name)
        }
    }

}
