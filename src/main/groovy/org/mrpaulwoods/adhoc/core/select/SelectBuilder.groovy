package org.mrpaulwoods.adhoc.core.select

class SelectBuilder {

    final Select select = new Select()

    Select end() {
        select.validate()
        select
    }

    FieldBuilder buildField() {
        new FieldBuilder(this)
    }

    OrderBuilder buildOrder() {
        new OrderBuilder(this)
    }

    WhereBuilder buildWhere() {
        new WhereBuilder(this)
    }

    SelectBuilder paginate(int row, int numRows) {
        select.row = row
        select.numRows = numRows
        this
    }

    SelectBuilder open(String name) {
        Where where = new Where()
        where.sequence = select.wheres.size()
        if (name) {
            where.conjunction = Conjunction.findByName(name)
        }
        where.isOpen = true
        select.wheres << where
        this
    }

    SelectBuilder close() {
        Where where = new Where()
        where.sequence = select.wheres.size()
        where.isClose = true
        select.wheres << where
        this
    }

    SelectBuilder distinct() {
        select.distinct = true
        this
    }

}
