package org.mrpaulwoods.adhoc.core.select

class OrderBuilder {

    final SelectBuilder selectBuilder
    final Order order = new Order()

    OrderBuilder(SelectBuilder selectBuilder) {
        this.selectBuilder = selectBuilder
    }

    SelectBuilder end() {
        selectBuilder.select.orders << order
        selectBuilder
    }

    OrderBuilder table(String table) {
        order.table = table
        this
    }

    OrderBuilder column(String column) {
        order.column = column
        this
    }

    OrderBuilder direction(String direction) {
        order.direction = Direction.findByName(direction)
        this
    }

}

