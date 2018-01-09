package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Order implements Serializable {

    static final long serialVersionUID = 0

    String table
    String column
    Direction direction

    void validate() {

        if (!table) {
            throw new TableNotSetException()
        }

        if (!column) {
            throw new ColumnNotSetException()
        }

        if (!direction) {
            throw new DirectionNotSetException()
        }

    }

    static class TableNotSetException extends AdhocException {
        TableNotSetException() {
            super("Table not set in order.")
        }
    }

    static class ColumnNotSetException extends AdhocException {
        ColumnNotSetException() {
            super("Column not set in order.")
        }
    }

    static class DirectionNotSetException extends AdhocException {
        DirectionNotSetException() {
            super("Direction not set in order.")
        }
    }

}
