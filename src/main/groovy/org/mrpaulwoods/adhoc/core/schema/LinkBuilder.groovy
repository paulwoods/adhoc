package org.mrpaulwoods.adhoc.core.schema

import org.mrpaulwoods.adhoc.core.select.JoinType
import org.mrpaulwoods.adhoc.core.select.OnBuilder

class LinkBuilder {

    final SchemaBuilder schemaBuilder
    final Link link = new Link()

    LinkBuilder(SchemaBuilder schemaBuilder) {
        this.schemaBuilder = schemaBuilder
    }

    SchemaBuilder end() {
        schemaBuilder.schema.links << link
        schemaBuilder
    }

    LinkBuilder table1(String name) {
        link.table1 = schemaBuilder.schema.findTable(name)
        this
    }

    LinkBuilder table2(String name) {
        link.table2 = schemaBuilder.schema.findTable(name)
        this
    }

    LinkBuilder joinType(String name) {
        link.joinType = JoinType.findByName(name)
        this
    }

    LinkBuilder cost(int cost) {
        link.cost = cost
        this
    }

    OnBuilder buildOns() {
        new OnBuilder(this)
    }

}
