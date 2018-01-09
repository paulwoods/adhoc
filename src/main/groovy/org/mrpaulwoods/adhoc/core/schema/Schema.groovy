package org.mrpaulwoods.adhoc.core.schema

import org.mrpaulwoods.adhoc.core.exception.AdhocException
import org.mrpaulwoods.adhoc.core.select.Field

class Schema implements Serializable {

    static final long serialVersionUID = 0

    final List<Table> tables = []
    final List<Link> links = []

    Table findTable(String name) {
        Table table = tables.find { it.name == name }
        if (table) {
            table
        } else {
            throw new TableNotFoundException(name)
        }
    }

    Column findColumn(Field field) {
        findColumn findTable(field.table), field.column
    }

    Column findColumn(Table table, String name) {
        Column column = table.columns.find { it.name == name }
        if (column) {
            column
        } else {
            throw new ColumnNotFoundException(name)
        }
    }

    void validate() {
        if (tables.isEmpty()) {
            throw new NoTablesException()
        }

        tables.each { it.validate() }

        links.each { it.validate() }

        tables.countBy { it.name }.each { k, v ->
            if (v > 1) {
                throw new DuplicateTableException(k)
            }
        }

        links.countBy { it.table1.name + ", " + it.table2.name }.each { k, v ->
            if (v > 1) {
                throw new DuplicateLinkException(k)
            }
        }

    }

    static class ColumnNotFoundException extends AdhocException {
        ColumnNotFoundException(String name) {
            super("Column not found: " + name)
        }
    }

    static class TableNotFoundException extends AdhocException {
        TableNotFoundException(String name) {
            super("Table not found: " + name)
        }
    }

    static class NoTablesException extends AdhocException {
        NoTablesException() {
            super("No tables were defined")
        }
    }

    static class DuplicateTableException extends AdhocException {
        DuplicateTableException(String name) {
            super("Table exists more than once: " + name)
        }
    }

    static class DuplicateLinkException extends AdhocException {
        DuplicateLinkException(String name) {
            super("Link with the same tables exists more than once: " + name)
        }
    }

}
