package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.select.Select
import spock.lang.Specification

class SelectSpec extends Specification {

    Schema schema1 = Adhoc.buildSchema()
            .buildTable()
            .name("contact")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
            .end()
            .end()

    def "create a select query"() {
        when:
        Select select = Adhoc
                .buildSelect().buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison(">").value("20150000000").end()
                .buildOrder().table("contact").column("number").direction("asc").end()
                .end()

        then:
        select.fields[0].table == "contact"
        select.fields[0].column == "number"
        select.wheres[0].sequence == 0
        select.wheres[0].table == "contact"
        select.wheres[0].column == "number"
        select.wheres[0].comparison.name == ">"
        select.wheres[0].values == ["20150000000"]
        select.orders[0].table == "contact"
        select.orders[0].column == "number"
        select.orders[0].direction.name == "asc"
    }

    def "create sql"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact'''.stripMargin()
    }

    def "create sql with expression"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").expression("abc").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tabc as "contact_number"
                   |
                   |from contact'''.stripMargin()
    }

    def "create sql with aggregate"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").aggregate("count").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcount(contact.number) as "contact_number"
                   |
                   |from contact'''.stripMargin()
    }

    def "create sql with order asc"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildOrder().table("contact").column("number").direction("asc").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |order by
                   |\tcontact.number asc'''.stripMargin()
    }

    def "create sql with order desc"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildOrder().table("contact").column("number").direction("desc").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |order by
                   |\tcontact.number desc'''.stripMargin()
    }

    def "create sql with order - 2 fields"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildField().table("contact").column("number").end()
                .buildOrder().table("contact").column("id").direction("asc").end()
                .buildOrder().table("contact").column("number").direction("desc").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id",
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |order by
                   |\tcontact.id asc,
                   |\tcontact.number desc'''.stripMargin()
    }

    def "create sql with 2 fields and aggregate"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildField().table("contact").column("number").aggregate("count").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id",
                   |\tcount(contact.number) as "contact_number"
                   |
                   |from contact
                   |
                   |group by
                   |\tcontact.id'''.stripMargin()
    }

    def "create sql with where = (varchar)"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("=").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number = '20150101000\''''.stripMargin()
    }

    def "create sql with where = user defined"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("=").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number = ?'''.stripMargin()
    }

    def "create sql with where = (int)"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison("=").value(123).end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id"
                   |
                   |from contact
                   |
                   |where contact.id = 123'''.stripMargin()
    }

    def "create sql with where <>"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("<>").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number <> '20150101000\''''.stripMargin()
    }

    def "create sql with where <"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("<").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number < '20150101000\''''.stripMargin()
    }

    def "create sql with where >"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison(">").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number > '20150101000\''''.stripMargin()
    }

    def "create sql with where <="() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("<=").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number <= '20150101000\''''.stripMargin()
    }

    def "create sql with where >="() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison(">=").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number >= '20150101000\''''.stripMargin()
    }

    def "create sql with where null"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("null").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number is null'''.stripMargin()
    }

    def "create sql with where not null"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("not null").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number is not null'''.stripMargin()
    }

    def "create sql with where begins with"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("begins with").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number like '20150101000%\''''.stripMargin()
    }

    def "create sql with where begins with user defined"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("begins with").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number like ?'''.stripMargin()
    }

    def "create sql with where contains"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("contains").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number like '%20150101000%\''''.stripMargin()
    }

    def "create sql with where ends with"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("ends with").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number like '%20150101000\''''.stripMargin()
    }

    def "create sql with where not begins with"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("not begins with").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number not like '20150101000%\''''.stripMargin()
    }

    def "create sql with where not contains"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("not contains").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number not like '%20150101000%\''''.stripMargin()
    }

    def "create sql with where not ends with"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("not ends with").value("20150101000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number not like '%20150101000\''''.stripMargin()
    }

    def "create sql with where in list (varchar, varargs)"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("in list").values("a", "b", "c").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number in ('a', 'b', 'c')'''.stripMargin()
    }

    def "create sql with where in list (varchar, list)"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("in list").values(Arrays.asList("a", "b", "c")).end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number in ('a', 'b', 'c')'''.stripMargin()
    }

    def "create sql with where in list (int, vararg)"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison("in list").values(1, 2, 3).end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id"
                   |
                   |from contact
                   |
                   |where contact.id in (1, 2, 3)'''.stripMargin()
    }

    def "create sql with where in list (int, list)"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison("in list").values(Arrays.asList(1, 2, 3)).end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id"
                   |
                   |from contact
                   |
                   |where contact.id in (1, 2, 3)'''.stripMargin()
    }

    def "create sql with where between (varchar, varchar)"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("between").values(Arrays.asList("1000", "2000")).end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number between '1000' and '2000\''''.stripMargin()
    }

    def "create sql with where between (user defined, user defined)"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("between").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.number between ? and ?'''.stripMargin()
    }

    Schema schema2 = Adhoc.buildSchema()
            .buildTable()
            .name("contact")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildTable()
            .name("address")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("filename").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildLink()
            .table1("contact").joinType("left").table2("address").cost(500)
            .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("contact_id").end()
            .end()

            .end()

    def "create sql with left joined tables"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildField().table("address").column("id").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema2).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id",
                   |\taddress.id as "address_id"
                   |
                   |from contact
                   |
                   |left join address
                   |on contact.id = address.contact_id'''.stripMargin()
    }

    Schema schema3 = Adhoc.buildSchema()
            .buildTable()
            .name("contact")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildTable()
            .name("address")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("filename").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildLink()
            .table1("contact").joinType("right").table2("address").cost(500)
            .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("contact_id").end()
            .end()

            .end()

    def "create sql with right joined tables"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildField().table("address").column("id").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema3).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id",
                   |\taddress.id as "address_id"
                   |
                   |from contact
                   |
                   |right join address
                   |on contact.id = address.contact_id'''.stripMargin()
    }

    Schema schema4 = Adhoc.buildSchema()
            .buildTable()
            .name("contact")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildTable()
            .name("address")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("filename").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildLink()
            .table1("contact").joinType("inner").table2("address").cost(500)
            .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("contact_id").end()
            .end()

            .end()

    def "create sql with equal joined tables"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildField().table("address").column("id").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema4).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id",
                   |\taddress.id as "address_id"
                   |
                   |from contact
                   |
                   |inner join address
                   |on contact.id = address.contact_id'''.stripMargin()
    }

    Schema schema5 = Adhoc.buildSchema()
            .buildTable()
            .name("contact")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildTable()
            .name("address")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("filename").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildTable()
            .name("customer")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("customer_name").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildLink()
            .table1("contact").joinType("left").table2("address").cost(500)
            .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("contact_id").end()
            .end()

            .buildLink()
            .table1("address").joinType("left").table2("customer").cost(500)
            .buildOns().table1("address").column1("id").comparison("=").table2("customer").column2("address_id").end()
            .end()

            .end()


    def "create sql with 3 joined tables"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildField().table("address").column("filename").end()
                .buildField().table("customer").column("customer_name").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema5).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number",
                   |\taddress.filename as "address_filename",
                   |\tcustomer.customer_name as "customer_customer_name"
                   |
                   |from contact
                   |
                   |left join address
                   |on contact.id = address.contact_id
                   |
                   |left join customer
                   |on address.id = customer.address_id'''.stripMargin()
    }

    def "create sql with 2 joined tables with a middle table skipped"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildField().table("customer").column("id").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema5).select(select).end()

        then:
        sql == '''select
                   |\tcontact.id as "contact_id",
                   |\tcustomer.id as "customer_id"
                   |
                   |from contact
                   |
                   |left join address
                   |on contact.id = address.contact_id
                   |
                   |left join customer
                   |on address.id = customer.address_id'''.stripMargin()
    }

    def "pagination"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildOrder().table("contact").column("number").direction("asc").end()
                .paginate(5, 10)
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |order by
                   |\tcontact.number asc
                   |
                   |limit 5, 10'''.stripMargin()
    }

    def "where sequence is set"() {

        when:
        Select select = Adhoc
                .buildSelect().buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison(">").value("1").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison(">").value("2").end()
                .buildWhere().conjunction("and").table("contact").column("name").comparison(">").value("3").end()
                .end()

        then:
        select.wheres[0].sequence == 0
        select.wheres[1].sequence == 1
        select.wheres[2].sequence == 2
    }

    def "order on an expression"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").expression("abc").end()
                .buildOrder().table("contact").column("number").direction("asc").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tabc as "contact_number"
                   |
                   |from contact
                   |
                   |order by
                   |\tabc asc'''.stripMargin()
    }

    def "order on an aggregate"() {
        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").aggregate("count").end()
                .buildOrder().table("contact").column("number").direction("asc").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcount(contact.number) as "contact_number"
                   |
                   |from contact
                   |
                   |order by
                   |\tcount(contact.number) asc'''.stripMargin()
    }

    def "multiple wheres"() {

        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison(">").value(100).end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("begins with").value("2015").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.id > 100
                   |and contact.number like '2015%\''''.stripMargin()

    }

    def "multiple wheres with an or"() {

        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison(">").value(100).end()
                .buildWhere().conjunction("or").table("contact").column("number").comparison("begins with").value("2015").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.id > 100
                   |or contact.number like '2015%\''''.stripMargin()

    }

    def "multiple wheres with multiple conjunctions"() {

        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison(">").value(100).end()
                .buildWhere().conjunction("or").table("contact").column("number").comparison("begins with").value("2015").end()
                .buildWhere().conjunction("AND").table("contact").column("id").comparison("<").value(500).end()
                .buildWhere().conjunction("OR").table("contact").column("number").comparison("ends with").value("000").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.id > 100
                   |or contact.number like '2015%'
                   |and contact.id < 500
                   |or contact.number like '%000\''''.stripMargin()

    }

    def "multiple wheres with grouping"() {

        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison(">").value(100).end()
                .open("or")
                .buildWhere().conjunction("or").table("contact").column("number").comparison("ends with").value("000").end()
                .close()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where contact.id > 100
                   |or (
                   |contact.number like '%000'
                   |)'''.stripMargin()
    }

    def "multiple wheres with grouping 2"() {

        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .open()
                .buildWhere().conjunction("and").table("contact").column("id").comparison("=").value(1).end()
                .buildWhere().conjunction("or").table("contact").column("id").comparison("=").value(2).end()
                .close()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where
                   |(
                   |contact.id = 1
                   |or contact.id = 2
                   |)'''.stripMargin()
    }

    def "multiple wheres with grouping 3"() {

        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("number").end()
                .open()
                .buildWhere().conjunction("and").table("contact").column("id").comparison("=").value(1).end()
                .open("or")
                .buildWhere().conjunction("or").table("contact").column("id").comparison("=").value(2).end()
                .open("or")
                .buildWhere().conjunction("or").table("contact").column("id").comparison("=").value(3).end()
                .close()
                .close()
                .close()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select
                   |\tcontact.number as "contact_number"
                   |
                   |from contact
                   |
                   |where
                   |(
                   |contact.id = 1
                   |or (
                   |contact.id = 2
                   |or (
                   |contact.id = 3
                   |)
                   |)
                   |)'''.stripMargin()
    }

    def "select distinct"() {

        Select select = Adhoc.buildSelect()
                .distinct()
                .buildField().table("contact").column("number").end()
                .end()

        when:
        def sql = Adhoc.buildSql().vendor("mysql").schema(schema1).select(select).end()

        then:
        sql == '''select distinct
                   |\tcontact.number as "contact_number"
                   |
                   |from contact'''.stripMargin()
    }

}

