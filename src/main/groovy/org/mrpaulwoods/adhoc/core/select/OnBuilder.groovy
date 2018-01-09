package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.schema.Comparison
import org.mrpaulwoods.adhoc.core.schema.LinkBuilder

class OnBuilder {

    final LinkBuilder linkBuilder
    final On on = new On()

    OnBuilder(LinkBuilder linkBuilder) {
        this.linkBuilder = linkBuilder
    }

    LinkBuilder end() {
        linkBuilder.link.ons << on
        linkBuilder
    }

    OnBuilder table1(String table1) {
        on.table1 = table1
        this
    }

    OnBuilder column1(String column1) {
        on.column1 = column1
        this
    }

    OnBuilder comparison(String name) {
        on.comparison = Comparison.findByName(name)
        this
    }

    OnBuilder table2(String table2) {
        on.table2 = table2
        this
    }

    OnBuilder column2(String column2) {
        on.column2 = column2
        this
    }

}

