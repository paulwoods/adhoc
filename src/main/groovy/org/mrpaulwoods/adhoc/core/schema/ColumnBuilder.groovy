package org.mrpaulwoods.adhoc.core.schema

class ColumnBuilder {

    final TableBuilder tableBuilder
    final Column column = new Column()

    ColumnBuilder(TableBuilder tableBuilder) {
        this.tableBuilder = tableBuilder
        column.table = tableBuilder.table
    }

    ColumnBuilder name(String name) {
        column.name = name
        this
    }

    ColumnBuilder columnType(String columnType) {
        column.columnType = ColumnType.findByName(columnType)
        this
    }

    ColumnBuilder size(Integer size) {
        column.size = size
        this
    }

    ColumnBuilder nullable(boolean nullable) {
        column.nullable = nullable
        this
    }

    ColumnBuilder title(String title) {
        column.title = title
        this
    }

    TableBuilder end() {
        tableBuilder.table.columns << column
        tableBuilder
    }

}
