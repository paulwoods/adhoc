package org.mrpaulwoods.adhoc.core.schema

class SchemaBuilder {

    final Schema schema = new Schema()

    Schema end() {
        schema.validate()
        schema
    }

    TableBuilder buildTable() {
        new TableBuilder(this)
    }

    LinkBuilder buildLink() {
        new LinkBuilder(this)
    }

    SchemaBuilder leftShift(Table table) {
        schema.tables << table
        this
    }

}
