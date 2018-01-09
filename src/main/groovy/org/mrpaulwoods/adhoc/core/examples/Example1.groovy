package org.mrpaulwoods.adhoc.core.examples

import org.mrpaulwoods.adhoc.core.Adhoc
import org.mrpaulwoods.adhoc.core.execute.Connection
import org.mrpaulwoods.adhoc.core.report.Report
import org.mrpaulwoods.adhoc.core.report.RowItem
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.select.Select

class Example1 {

    static void main(String[] args) {

        Schema schema = Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .buildColumn().name("first_name").columnType("varchar").size(100).nullable(false).end()
                .buildColumn().name("last_name").columnType("varchar").size(100).nullable(false).end()
                .end()
                .end()

        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("first_name").end()
                .buildField().table("contact").column("last_name").end()
                .buildWhere().conjunction("and").table("contact").column("last_name").comparison("begins with").value("A").end()
                .buildOrder().table("contact").column("first_name").direction("asc").end()
                .buildOrder().table("contact").column("last_name").direction("asc").end()
                .end()

        Connection connection = Adhoc.buildConnection()
                .vendor("mysql")
                .host("localhost")
                .port(3306)
                .database("contact")
                .username("contact")
                .password("contact")
                .parameter("useSSL", "false")
                .end()

        Report report = Adhoc.buildExecute()
                .vendor("mysql")
                .schema(schema)
                .select(select)
                .connection(connection)
                .end()

        // print the report as a .csv
        println report.headers.title.join(", ")
        report.rows.each { RowItem rowItem ->
            println rowItem.fields.value.join(", ")
        }

    }

}
