package org.mrpaulwoods.adhoc.core.execute

import org.mrpaulwoods.adhoc.core.report.Report
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.select.Select
import org.mrpaulwoods.adhoc.core.vendor.MysqlVendor

class ExecuteBuilder {

    final Execute execute = new Execute()

    QueryExecutor queryExecutor = new QueryExecutor()

    ExecuteBuilder connection(Connection connection) {
        execute.connection = connection
        this
    }

    ExecuteBuilder schema(Schema schema) {
        execute.schema = schema
        this
    }

    ExecuteBuilder select(Select select) {
        execute.select = select
        this
    }

    ExecuteBuilder userValues(UserValues userValues) {
        execute.userValues = userValues
        this
    }

    ExecuteBuilder vendor(String name) {
        switch (name.toLowerCase()) {
            case "mysql":
                execute.vendor = new MysqlVendor()
                break
        }
        this
    }

    Report end() {
        queryExecutor.execute(execute)
    }

}
