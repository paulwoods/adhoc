package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException
import org.mrpaulwoods.adhoc.core.schema.Comparison

class On implements Serializable {

    static final long serialVersionUID = 0

    String table1
    String column1
    Comparison comparison
    String table2
    String column2

    void validate() {

        if (!table1) {
            throw new TableNotSetException("table1")
        }

        if (!column1) {
            throw new ColumnNotSetException("column1")
        }

        if (!comparison) {
            throw new ComparisonNotSetException()
        }

        if (!table2) {
            throw new TableNotSetException("table1")
        }

        if (!column2) {
            throw new ColumnNotSetException("column1")
        }

        if (table1.equalsIgnoreCase(table2)) {
            throw new TableDuplicateException(table1)
        }

    }

    static class TableNotSetException extends AdhocException {
        TableNotSetException(String name) {
            super("On does not have " + name + " set")
        }
    }

    static class ColumnNotSetException extends AdhocException {
        ColumnNotSetException(String name) {
            super("On does not have " + name + " set")
        }
    }

    static class ComparisonNotSetException extends AdhocException {
        ComparisonNotSetException(String name) {
            super("On does not have the comparison set")
        }
    }

    static class TableDuplicateException extends AdhocException {
        TableDuplicateException(String name) {
            super("On's table1 and table2 must be different tables: " + name)
        }
    }

}
