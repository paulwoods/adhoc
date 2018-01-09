package org.mrpaulwoods.adhoc.core.schema

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Column implements Serializable {

    static final long serialVersionUID = 0

    Table table
    String name
    ColumnType columnType
    Integer size
    Boolean nullable = true
    String title

    void validate() {
        if (!name) {
            throw new InvalidColumnNameException(name)
        }

        if (!columnType) {
            throw new ColumnTypeNotSetException(name)
        }

    }

    static class InvalidColumnNameException extends AdhocException {
        InvalidColumnNameException(String name) {
            super("The column name is invalid: " + name)
        }
    }

    static class ColumnTypeNotSetException extends AdhocException {
        ColumnTypeNotSetException(String name) {
            super("The column type is not set: " + name)
        }
    }


}
