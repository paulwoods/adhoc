package org.mrpaulwoods.adhoc.core.schema

import org.mrpaulwoods.adhoc.core.exception.AdhocException
import org.mrpaulwoods.adhoc.core.select.JoinType
import org.mrpaulwoods.adhoc.core.select.On

class Link implements Serializable {

    static final long serialVersionUID = 0

    Table table1
    Table table2
    JoinType joinType
    final List<On> ons = []
    int cost

    void validate() {
        if (!table1) {
            throw new TableNotSetException("table1")
        }

        if (!table2) {
            throw new TableNotSetException("table2")
        }

        if (!joinType) {
            throw new JoinTypeNotSetException(table1.name, table2.name)
        }

        if (!cost) {
            throw new CostNotSetException(table1.name, table2.name)
        }

        if (table1.name.equalsIgnoreCase(table2.name)) {
            throw new DuplicateTableException(table1.name)
        }

        if (ons.isEmpty()) {
            throw new NoOnsException(table1.name, table2.name)
        }

        ons.each { it.validate() }

        ons.countBy { it.table1 + ", " + it.column1 + ", " + it.table2 + ", " + it.column2 }.each { k, v ->
            if (v > 1) {
                throw new DuplicateOnException(k)
            }
        }

    }
/*
    String table1
    String column1
    Comparison comparison
    String table2
    String column2

 */

    static class TableNotSetException extends AdhocException {
        TableNotSetException(String name) {
            super("Link does not have " + name + " set")
        }
    }

    static class JoinTypeNotSetException extends AdhocException {
        JoinTypeNotSetException(String table1, String table2) {
            super("Join type not set: " + table1 + ", " + table2)
        }
    }

    static class CostNotSetException extends AdhocException {
        CostNotSetException(String table1, String table2) {
            super("Cost not set: " + table1 + ", " + table2)
        }
    }

    static class NoOnsException extends AdhocException {
        NoOnsException(String table1, String table2) {
            super("Link has no ons: " + table1 + ", " + table2)
        }
    }

    static class DuplicateTableException extends AdhocException {
        DuplicateTableException(String name) {
            super("Link's table1 and table2 must be different tables: " + name)
        }
    }

    static class DuplicateOnException extends AdhocException {
        DuplicateOnException(String name) {
            super("On's must be different tables and columns: " + name)
        }
    }

}
