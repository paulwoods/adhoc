package org.mrpaulwoods.adhoc.core.execute

import groovy.sql.Sql
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.select.Select
import org.mrpaulwoods.adhoc.core.vendor.Vendor

class Execute implements Serializable {

    static final long serialVersionUID = 0

    Vendor vendor
    Connection connection
    Schema schema
    Select select
    UserValues userValues = new UserValues()

    Sql createDatabase() {
        vendor.createDatabase(connection)
    }
}
