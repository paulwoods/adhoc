package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException
import org.mrpaulwoods.adhoc.core.schema.Comparison

class Where implements Serializable {

    static final long serialVersionUID = 0

    int sequence = 0
    String table
    String column
    Comparison comparison
    final List<Object> values = []
    Conjunction conjunction
    boolean isOpen = false
    boolean isClose = false

    void validate() {

        if (!isOpen && !isClose && !table) {
            throw new TableNotSetException()
        }

        if (!isOpen && !isClose && !column) {
            throw new ColumnNotSetException()
        }

        if (!isOpen && !isClose && !comparison) {
            throw new ComparisonNotSetException()
        }

        if (!isOpen && !isClose && !conjunction) {
            throw new ConjunctionNotSetException()
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

    static class ComparisonNotSetException extends AdhocException {
        ComparisonNotSetException() {
            super("Comparison not set in field.")
        }
    }

    static class ConjunctionNotSetException extends AdhocException {
        ConjunctionNotSetException() {
            super("Conjunction not set in field.")
        }
    }

}
