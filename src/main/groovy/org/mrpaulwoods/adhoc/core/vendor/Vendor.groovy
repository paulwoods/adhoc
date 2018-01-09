package org.mrpaulwoods.adhoc.core.vendor

import groovy.sql.Sql
import org.mrpaulwoods.adhoc.core.execute.Connection
import org.mrpaulwoods.adhoc.core.execute.UserValues
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.select.Select

interface Vendor {

    String getName()

    String createSql(Schema schema, Select select, UserValues userValues)

    Sql createDatabase(Connection connection)
}
