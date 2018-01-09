package org.mrpaulwoods.adhoc.core.execute

import groovy.sql.Sql
import org.mrpaulwoods.adhoc.core.Adhoc
import org.mrpaulwoods.adhoc.core.report.FieldItem
import org.mrpaulwoods.adhoc.core.report.HeaderItem
import org.mrpaulwoods.adhoc.core.report.Report
import org.mrpaulwoods.adhoc.core.report.RowItem
import org.mrpaulwoods.adhoc.core.schema.Column
import org.mrpaulwoods.adhoc.core.select.Field

class QueryExecutor {

    Report execute(Execute execute) {

        // connect to the database

        Sql database = execute.createDatabase()

        // create the sql

        def sql = Adhoc.buildSql()
                .vendor("mysql")
                .schema(execute.schema)
                .select(execute.select)
                .userValues(execute.userValues)
                .end()

        // execute the sql, with or without user values

        def values = []
        if (execute.userValues) {
            // execute with a list of user values
            execute.select.determineUserValues().each { k ->
                execute.userValues.values[k].each { value ->
                    values << value
                }
            }
        }

        def rows = database.rows(sql, values)

        Report report = new Report()

        if (rows) {

            // create the header

            execute.select.fields.each { Field field ->
                Column column = execute.schema.findColumn(field)
                HeaderItem item = new HeaderItem()
                item.title = field.title ?: column.title ?: (field.table + "_" + field.column)
                item.name = field.table + "_" + field.column
                report.headers << item
            }

            // create the rows

            rows.each { row ->
                RowItem rowItem = new RowItem()

                execute.select.fields.each { Field field ->
                    FieldItem fieldItem = new FieldItem()
                    fieldItem.value = row[field.table + "_" + field.column]
                    rowItem.fields << fieldItem
                }

                report.rows << rowItem
            }

        }

        database.close()

        report
    }

}
