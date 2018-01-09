package org.mrpaulwoods.adhoc.core.schema

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Table implements Serializable {

    static final long serialVersionUID = 0

    String name
    final List<Column> columns = []

    // dijkstra variables - don't persist
    int distance
    Boolean known
    Table path

    void validate() {

        if (!name) {
            throw new InvalidTableNameException(name)
        }

        if (columns.isEmpty()) {
            throw new NoColumnsException(name)
        }

        columns.each { it.validate() }

        columns.countBy { it.name }.each { k, v ->
            if (v > 1) {
                throw new DuplicateColumnException(k)
            }
        }

    }

    static class InvalidTableNameException extends AdhocException {
        InvalidTableNameException(String name) {
            super("The table name is invalid: " + name)
        }
    }

    static class NoColumnsException extends AdhocException {
        NoColumnsException(String name) {
            super("Table has no columns: " + name)
        }
    }

    static class DuplicateColumnException extends AdhocException {
        DuplicateColumnException(String name) {
            super("Column exists more than once: " + name)
        }
    }

}
