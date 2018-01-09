package org.mrpaulwoods.adhoc.core.vendor

import groovy.sql.Sql
import org.mrpaulwoods.adhoc.core.Utils
import org.mrpaulwoods.adhoc.core.exception.NoSelectsException
import org.mrpaulwoods.adhoc.core.execute.Connection
import org.mrpaulwoods.adhoc.core.execute.UserValues
import org.mrpaulwoods.adhoc.core.schema.Column
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.schema.Table
import org.mrpaulwoods.adhoc.core.select.*

class MysqlVendor implements Vendor {

    String name = "mysql"

    /** build the query sql
     *
     * @param schema
     * @param select
     * @param userValues
     * @return
     */
    String createSql(Schema schema, Select select, UserValues userValues) {

        if (select.isEmpty()) {
            throw new NoSelectsException()
        }

        SqlData data = new SqlData()
        data.schema = schema
        data.select = select
        data.userValues = userValues

        // walk the schema's graph to get the referenced tables, and the path

        data.reportTables = Utils.getReportTables(select, schema)
        data.theLinks = Utils.getReportLinks(schema, data.reportTables)

        def sb = new StringBuilder()

        makeSelects data, sb
        makeFrom data, sb
        makeJoins data, sb
        makeWheres data, sb
        makeGroups data, sb
        makeOrders data, sb
        makePagination data, sb

        sb.toString()
    }

    /** create the select sql
     *
     * @param data
     * @param sb
     */
    void makeSelects(SqlData data, StringBuilder sb) {

        sb.append "select"

        if (data.select.distinct) {
            sb.append " distinct"
        }

        sb.append "\n"

        boolean first = true

        data.select.fields.each { field ->
            if (first) {
                first = false
                sb.append "\t"
            } else {
                sb.append ",\n\t"
            }

            if (field.expression) {

                // expression

                sb.append field.expression
                sb.append " as \""
                sb.append field.table
                sb.append "_"
                sb.append field.column
                sb.append "\""
            } else if (field.aggregate) {

                // aggregate

                sb.append field.aggregate.code.replace("data", field.table + "." + field.column)
                sb.append " as \""
                sb.append field.table
                sb.append "_"
                sb.append field.column
                sb.append "\""
            } else {

                // field name

                sb.append field.table
                sb.append "."
                sb.append field.column
                sb.append " as \""
                sb.append field.table
                sb.append "_"
                sb.append field.column
                sb.append "\""
            }

        }
        sb.append "\n\n"
    }

    /** create the from sql
     *
     * @param data
     * @param sb
     */
    void makeFrom(SqlData data, StringBuilder sb) {
        sb.append "from "
        sb.append data.reportTables[0].name
    }

    /** create the joins sql
     *
     * @param data
     * @param sb
     */
    void makeJoins(SqlData data, StringBuilder sb) {

        List<Table> includedTables = []
        includedTables << data.reportTables[0]

        data.theLinks.each { link ->

            // figure which table to create, based on if that table name has already been included
            // if link.table1 is not included, include it; else, include table2

            if (includedTables.contains(link.table1)) {
                includedTables << link.table2
            } else {
                includedTables << link.table1
            }

            sb.append "\n\n"
            sb.append link.joinType.name
            sb.append " join "
            sb.append includedTables[-1].name

            boolean first = true
            link.ons.each { On on ->
                if (first) {
                    sb.append "\non "
                    first = false
                } else {
                    sb.append "\nand "
                }

                sb.append on.table1
                sb.append "."
                sb.append on.column1
                sb.append " "
                sb.append on.comparison.code
                sb.append " "
                sb.append on.table2
                sb.append "."
                sb.append on.column2
            }
        }
    }

    /** create the where sql
     *
     * @param data
     * @param sb
     */
    void makeWheres(SqlData data, StringBuilder sb) {
        if (data.select.wheres.isEmpty()) {
            return
        }

        sb.append "\n\nwhere"

        boolean showConjunction = false

        boolean first = true
        data.select.wheres.each { Where where ->

            if (where.isOpen) {
                whereOpen where, sb
                showConjunction = false
                first = false
                return
            }

            if (where.isClose) {
                whereClose where, sb
                first = false
                return
            }

            if (showConjunction) {
                sb.append "\n"
                sb.append where.conjunction.name
                sb.append " "
            } else {
                showConjunction = true
            }

            if (first) {
                sb.append " "
            }

            switch (where.comparison.scope.name) {
                case "NONE":
                    whereNone where, sb
                    break
                case "ONE":
                    whereOne data, where, sb
                    break
                case "TWO":
                    whereTwo data, where, sb
                    break
                case "LIST":
                    whereList data, where, sb
                    break
            }

            first = false
        }
    }

    void whereOpen(Where where, StringBuilder sb) {
        sb.append "\n"
        if (where.conjunction) {
            sb.append where.conjunction.name
            sb.append " "
        }
        sb.append "(\n"
    }

    void whereClose(Where where, StringBuilder sb) {
        sb.append "\n)"
    }

    /** create where sql where there is no predicate
     *
     * @param where
     * @param sb
     */
    void whereNone(Where where, StringBuilder sb) {
        sb.append where.table
        sb.append "."
        sb.append where.column
        sb.append " "
        sb.append where.comparison.code
    }

    /** create where sql where there is 1 predicate
     *
     * @param data
     * @param where
     * @param sb
     */
    void whereOne(SqlData data, Where where, StringBuilder sb) {
        Column column = data.schema.findColumn(data.schema.findTable(where.table), where.column)

        sb.append where.table
        sb.append "."
        sb.append where.column
        sb.append " "
        sb.append where.comparison.code
        sb.append " "

        if (where.values.isEmpty()) {

            sb.append "?"

        } else {

            if (column.columnType.prefix) {
                sb.append column.columnType.prefix
            }

            if (where.comparison.prefix) {
                sb.append where.comparison.prefix
            }

            sb.append where.values[0]

            if (where.comparison.suffix) {
                sb.append where.comparison.suffix
            }

            if (column.columnType.suffix) {
                sb.append column.columnType.suffix
            }
        }
    }

    /** create where sql where there are 2 predicates
     *
     * @param data
     * @param where
     * @param sb
     */
    void whereTwo(SqlData data, Where where, StringBuilder sb) {
        Column column = data.schema.findColumn(data.schema.findTable(where.table), where.column)

        sb.append where.table
        sb.append "."
        sb.append where.column
        sb.append " "
        sb.append where.comparison.code
        sb.append " "

        if (where.values.isEmpty()) {
            sb.append "?"
        } else {

            if (column.columnType.prefix) {
                sb.append column.columnType.prefix
            }

            sb.append where.values[0]

            if (column.columnType.suffix) {
                sb.append column.columnType.suffix
            }

        }

        sb.append " and "

        if (where.values.size() < 2) {
            sb.append "?"
        } else {

            if (column.columnType.prefix) {
                sb.append column.columnType.prefix
            }

            sb.append where.values[1]

            if (column.columnType.suffix) {
                sb.append column.columnType.suffix
            }

        }
    }

    /** create where sql where the predicate is a list
     *
     * @param data
     * @param where
     * @param sb
     */
    void whereList(SqlData data, Where where, StringBuilder sb) {
        Column column = data.schema.findColumn(data.schema.findTable(where.table), where.column)

        sb.append where.table
        sb.append "."
        sb.append where.column
        sb.append " "
        sb.append where.comparison.code
        sb.append " ("

        if (where.values.isEmpty()) {

            if (data.userValues.isEmpty()) {
                sb.append "?"
            } else {
                String name = "${where.sequence}.${where.table}.${where.column}"
                int count = data.userValues.values[name].size()
                def text = (["?"] * count).join(", ")
                sb.append text
            }

        } else {

            boolean first = true
            for (Object value : where.values) {

                if (first) {
                    first = false
                } else {
                    sb.append ", "
                }

                if (column.columnType.prefix) {
                    sb.append column.columnType.prefix
                }

                sb.append value

                if (column.columnType.suffix) {
                    sb.append column.columnType.suffix
                }

            }

        }

        sb.append ")"
    }
    /** create group sql
     *
     * @param data
     * @param sb
     */
    void makeGroups(SqlData data, StringBuilder sb) {

        if (data.select.fields.any { null != it.aggregate }) {
            List<Field> groups = data.select.fields.findAll { !it.aggregate }
            if (!groups.isEmpty()) {

                boolean first = true
                groups.each { Field field ->

                    if (first) {
                        sb.append("\n\ngroup by\n")
                        first = false
                    } else {
                        sb.append(",\n")
                    }

                    sb.append "\t"
                    sb.append field.table
                    sb.append "."
                    sb.append field.column
                }

            }
        }
    }

    /** create order sql
     *
     * @param data
     * @param sb
     */
    void makeOrders(SqlData data, StringBuilder sb) {

        boolean first = true

        data.select.orders.each { Order order ->

            Field field = data.select.findField(order.table, order.column)

            if (first) {
                first = false
                sb.append "\n\norder by\n"
            } else {
                sb.append ",\n"
            }

            if (field.expression) {
                sb.append "\t"
                sb.append field.expression
                sb.append " "
                sb.append order.direction.name
            } else if (field.aggregate) {
                sb.append "\t"
                sb.append field.aggregate.code.replace("data", field.table + "." + field.column)
                sb.append " "
                sb.append order.direction.name
            } else {
                sb.append "\t"
                sb.append field.table
                sb.append "."
                sb.append field.column
                sb.append " "
                sb.append order.direction.name
            }
        }

    }

    /** create pagination sql
     *
     * @param data
     * @param sb
     */
    void makePagination(SqlData data, StringBuilder sb) {
        if (null != data.select.row) {
            sb.append "\n\nlimit "
            sb.append data.select.row
            sb.append ", "
            sb.append data.select.numRows
        }
    }

    /**
     * Create a connection to the database.
     *
     * @param connection
     * @return
     */
    Sql createDatabase(Connection connection) {

        // build the connection url

        def url = "jdbc:mysql://$connection.host:$connection.port/$connection.database"

        boolean first = true
        connection.parameters.each { k, v ->
            if (first) {
                url += "?"
                first = false
            } else {
                url += "&"
            }
            url += "$k=$v"
        }

        // create the groovy.sql.Sql connection

        Sql.newInstance(url, connection.username, connection.password, "com.mysql.jdbc.Driver")
    }

}

