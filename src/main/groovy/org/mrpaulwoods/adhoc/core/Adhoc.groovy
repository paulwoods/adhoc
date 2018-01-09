package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.execute.ConnectionBuilder
import org.mrpaulwoods.adhoc.core.execute.ExecuteBuilder
import org.mrpaulwoods.adhoc.core.query.SqlBuilder
import org.mrpaulwoods.adhoc.core.schema.SchemaBuilder
import org.mrpaulwoods.adhoc.core.select.SelectBuilder

class Adhoc {

    static ConnectionBuilder buildConnection() {
        new ConnectionBuilder()
    }

    static SchemaBuilder buildSchema() {
        new SchemaBuilder()
    }

    static SelectBuilder buildSelect() {
        new SelectBuilder()
    }

    static ExecuteBuilder buildExecute() {
        new ExecuteBuilder()
    }

    static SqlBuilder buildSql() {
        new SqlBuilder()
    }

}
