package org.mrpaulwoods.adhoc.core.query

import org.mrpaulwoods.adhoc.core.execute.UserValues
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.select.Select
import org.mrpaulwoods.adhoc.core.vendor.MysqlVendor
import org.mrpaulwoods.adhoc.core.vendor.Vendor

class SqlBuilder {
    Vendor vendor
    Schema schema
    Select select
    UserValues userValues

    SqlBuilder vendor(String name) {
        switch (name.toLowerCase()) {
            case "mysql":
                vendor = new MysqlVendor()
                break
        }
        this
    }

    SqlBuilder schema(Schema schema) {
        this.schema = schema
        this
    }

    SqlBuilder select(Select select) {
        this.select = select
        this
    }

    SqlBuilder userValues(UserValues userValues) {
        this.userValues = userValues
        this
    }

    String end() {
        vendor.createSql schema, select, userValues
    }

}
