package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.schema.Comparison

class WhereBuilder {

    final SelectBuilder selectBuilder
    final Where where = new Where()

    WhereBuilder(SelectBuilder selectBuilder) {
        this.selectBuilder = selectBuilder
    }

    WhereBuilder table(String table) {
        where.table = table
        this
    }

    WhereBuilder column(String column) {
        where.column = column
        this
    }

    WhereBuilder comparison(String comparison) {
        where.comparison = Comparison.findByName(comparison)
        this
    }

    WhereBuilder value(Object value) {
        where.values.clear()
        where.values.add value
        this
    }

    WhereBuilder values(List<Object> values) {
        where.values.clear()
        where.values.addAll values
        this
    }

    WhereBuilder values(Object... values) {
        where.values.clear()
        where.values.addAll Arrays.asList(values)
        this
    }

    WhereBuilder conjunction(String name) {
        where.conjunction = Conjunction.findByName(name)
        this
    }

    SelectBuilder end() {
        where.sequence = selectBuilder.select.wheres.size()
        selectBuilder.select.wheres << where
        selectBuilder
    }

}

