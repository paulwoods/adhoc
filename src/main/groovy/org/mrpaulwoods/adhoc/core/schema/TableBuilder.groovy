package org.mrpaulwoods.adhoc.core.schema

class TableBuilder {

    final SchemaBuilder schemaBuilder
    final Table table = new Table()

    TableBuilder(SchemaBuilder schemaBuilder) {
        this.schemaBuilder = schemaBuilder
    }

    TableBuilder name(String name) {
        table.name = name
        this
    }

    ColumnBuilder buildColumn() {
        new ColumnBuilder(this)
    }

    SchemaBuilder end() {
        schemaBuilder << table
        schemaBuilder
    }


}
