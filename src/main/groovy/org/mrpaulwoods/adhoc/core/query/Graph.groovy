package org.mrpaulwoods.adhoc.core.query

import org.mrpaulwoods.adhoc.core.schema.Link
import org.mrpaulwoods.adhoc.core.schema.Table

class Graph {

    /** build the shortest path route from the table on this method, to all other tables.
     *
     * @param tables
     * @param links
     * @param firstTable
     */
    void dijkstra(List<Table> tables, List<Link> links, Table firstTable) {

        // clear previous data

        tables.each {
            it.distance = Integer.MAX_VALUE
            it.known = false
            it.path = null
        }

        // first table's distance is always zero

        firstTable.distance = 0

        while (true) {

            // find the candidate tables (known == false). exit if no more candidates

            List<Table> candidates = getCandidates(tables)
            if (candidates.isEmpty()) {
                break
            }

            // mark the 1st candidate as known

            Table v = candidates[0]
            v.known = true

            // loop through the tables adjacent to the 1st candidate

            List<Table> neighbors = findAdjacent(v, tables, links)

            for (Table neighbor : neighbors) {
                if (!neighbor.known) {
                    Link edge = findLink(v, neighbor, links)
                    if (v.distance + edge.cost < neighbor.distance) {
                        neighbor.distance = v.distance + edge.cost
                        neighbor.path = v
                    }
                }
            }
        }

    }

    /**
     * Return the tables that are table.known == false. order by distance desc
     *
     * @param tables
     * @return
     */
    List<Table> getCandidates(List<Table> tables) {
        tables.findAll { !it.known }.sort { -it.distance }
    }

    /**
     * Return the list of tables that are linked to this table
     * @param links
     * @param v
     */
    List<Table> findAdjacent(Table table, List<Table> tables, List<Link> links) {

        List<Table> adjacent = []

        for (Link link : links) {
            if (link.table1 == table) {
                adjacent << tables.find { it == link.table2 }
            } else if (link.table2 == table) {
                adjacent << tables.find { it == link.table1 }
            }
        }

        adjacent
    }

    /** return the list of links that cover all of the tables
     *
     * @param reportTables
     * @param allLinks
     * @return
     */
    List<Link> walk(List<Table> reportTables, List<Link> allLinks) {

        List<Link> theLinks = []

        for (int n = 0; n < reportTables.size(); ++n) {
            Table table = reportTables.get(n)
            List<Link> links = buildWalk(table, allLinks)

            // create the links that do not already exist

            for (Link link : links) {
                if (!theLinks.contains(link)) {
                    theLinks << link
                }
            }
        }
        theLinks
    }

    /** Get the list of links that bridge the gap between the table used on the dijkstra method and the table passed to this method.
     *
     * @param table
     * @param allTables
     * @param allLinks
     * @return
     */
    List<Link> buildWalk(Table table, List<Link> allLinks) {
        List<Link> links = []
        while (null != table.getPath()) {
            links << findLink(table, table.path, allLinks)
            table = table.getPath()
        }
        links.reverse()
    }

    /**
     * Given two tables, return the link between them
     */
    Link findLink(Table table1, Table table2, List<Link> links) {
        links.find {
            ((table1 == it.table1 && table2 == it.table2) || (table1 == it.table2 && table2 == it.table1))
        }
    }


}

