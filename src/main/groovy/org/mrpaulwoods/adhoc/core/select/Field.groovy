package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Field implements Serializable {

    static final long serialVersionUID = 0

    String table
    String column
    String expression
    Aggregate aggregate
    String title

    void validate() {

        if (!table) {
            throw new TableNotSetException()
        }

        if (!column) {
            throw new ColumnNotSetException()
        }
    }

    static class TableNotSetException extends AdhocException {
        TableNotSetException() {
            super("Table not set in field.")
        }
    }

    static class ColumnNotSetException extends AdhocException {
        ColumnNotSetException() {
            super("Column not set in field.")
        }
    }

}
