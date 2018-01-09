package org.mrpaulwoods.adhoc.core.select

class FieldBuilder {

    final SelectBuilder selectBuilder
    final Field field = new Field()

    FieldBuilder(SelectBuilder selectBuilder) {
        this.selectBuilder = selectBuilder
    }

    SelectBuilder end() {
        selectBuilder.select.fields << field
        selectBuilder
    }

    FieldBuilder table(String name) {
        field.table = name
        this
    }

    FieldBuilder column(String name) {
        field.column = name
        this
    }

    FieldBuilder expression(String name) {
        field.expression = name
        this
    }

    FieldBuilder aggregate(String name) {
        field.aggregate = Aggregate.findByName(name)
        this
    }

}
