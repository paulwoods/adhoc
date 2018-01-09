package org.mrpaulwoods.adhoc.core.vendor

import org.mrpaulwoods.adhoc.core.execute.UserValues
import org.mrpaulwoods.adhoc.core.schema.Link
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.schema.Table
import org.mrpaulwoods.adhoc.core.select.Select

class SqlData {
    Schema schema
    Select select
    UserValues userValues
    List<Table> reportTables
    ArrayList<Link> theLinks
}
