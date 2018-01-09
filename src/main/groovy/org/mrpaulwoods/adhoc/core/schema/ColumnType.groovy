package org.mrpaulwoods.adhoc.core.schema

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class ColumnType implements Serializable {

    static final long serialVersionUID = 0

    String name
    String prefix
    String suffix

    private ColumnType() {}

    static final columnTypes = Arrays.asList(
            new ColumnType(name: "int", prefix: null, suffix: null),
            new ColumnType(name: "varchar", prefix: "'", suffix: "'"),
            new ColumnType(name: "datetime", prefix: null, suffix: null),
            new ColumnType(name: "bit", prefix: null, suffix: null),
            new ColumnType(name: "bigint", prefix: null, suffix: null),
            new ColumnType(name: "timestamp", prefix: null, suffix: null),
            new ColumnType(name: "longblob", prefix: null, suffix: null),
            new ColumnType(name: "tinyint", prefix: null, suffix: null),
            new ColumnType(name: "longtext", prefix: null, suffix: null),
            new ColumnType(name: "tinyblob", prefix: null, suffix: null)
    )

    static ColumnType findByName(String name) {
        ColumnType columnType = columnTypes.find { it.name.equalsIgnoreCase(name) }
        if (columnType) {
            columnType
        } else {
            throw new NotFoundException(name)
        }
    }

    static class NotFoundException extends AdhocException {
        NotFoundException(String name) {
            super("Column columnType not found: " + name)
        }
    }

}

