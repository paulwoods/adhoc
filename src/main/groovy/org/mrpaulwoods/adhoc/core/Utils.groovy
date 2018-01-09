package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.query.Graph
import org.mrpaulwoods.adhoc.core.schema.Link
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.schema.Table
import org.mrpaulwoods.adhoc.core.select.Select

class Utils {

    /** get all of the selected & where tables
     *
     * @param select
     * @param schema
     * @return
     */

    static List<Table> getReportTables(Select select, Schema schema) {

        // get the table names

        List<String> names = []
        names += select.fields.table

        select.wheres.each {
            if (!it.isOpen && !it.isClose) {
                names += it.table
            }
        }

        names = names.unique()

        // return the tables

        names.collect { schema.findTable(it) }
    }

    /** build a list of links that join all of the tables in the query
     *
     * @param schema
     * @param reportTables
     * @return
     */
    static ArrayList<Link> getReportLinks(Schema schema, List<Table> reportTables) {
        def graph = new Graph()
        graph.dijkstra schema.tables, schema.links, reportTables.get(0)
        graph.walk reportTables, schema.links
    }


}
