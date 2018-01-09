package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.select.Select
import spock.lang.Specification

class SelectPersistSpec extends Specification {

    Select select1 = Adhoc
            .buildSelect().buildField().table("contact").column("number").aggregate("Count").expression("abc").end()
            .buildWhere().conjunction("and").table("contact").column("number").comparison(">").value("20150000000").end()
            .buildOrder().table("contact").column("number").direction("asc").end()
            .end()

    def "select can be saved and loaded"() {
        def baos = new ByteArrayOutputStream()
        def oos = new ObjectOutputStream(baos)

        when:
        oos.writeObject(select1)

        then:
        baos.size() > 0

        when:
        def bais = new ByteArrayInputStream(baos.toByteArray())
        def ois = new ObjectInputStream(bais)

        Select select2 = (Select) ois.readObject()

        then:
        select2.fields.size() == 1
        select2.fields[0].table == "contact"
        select2.fields[0].column == "number"
        select2.fields[0].expression == "abc"
        select2.fields[0].aggregate.name == "Count"

        select2.wheres.size() == 1
        select2.wheres[0].table == "contact"
        select2.wheres[0].column == "number"
        select2.wheres[0].comparison.name == ">"
        select2.wheres[0].values[0] == "20150000000"

        select2.orders.size() == 1
        select2.orders[0].table == "contact"
        select2.orders[0].column == "number"
        select2.orders[0].direction.name == "asc"
    }

}