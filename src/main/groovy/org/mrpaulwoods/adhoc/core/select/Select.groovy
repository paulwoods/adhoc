package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Select implements Serializable {

    static final long serialVersionUID = 0

    final List<Field> fields = []
    final List<Where> wheres = []
    final List<Order> orders = []
    Integer row
    Integer numRows
    boolean distinct = false

    boolean isEmpty() {
        fields.isEmpty()
    }

    Field findField(String table, String column) {
        Field field = fields.find { it.table == table && it.column == column }
        if (field) {
            field
        } else {
            throw new FieldNotFoundException(table + "." + column)
        }
    }

    /** return an UserValues object, with a key for each table.column that needs a value.
     *
     * @return
     */
    List determineUserValues() {

        def userValues = []

        wheres.each { Where where ->
            if (where.values.isEmpty()) {

                String name = "${where.sequence}.${where.table}.${where.column}"

                switch (where.comparison.scope.name) {
                    case "ONE":
                        userValues << name
                        break

                    case "TWO":
                        userValues << name + ".lower"
                        userValues << name + ".upper"
                        break

                    case "LIST":
                        userValues << name
                        break
                }
            }
        }

        userValues
    }

    void validate() {
        if (fields.isEmpty()) {
            throw new NoFieldsException()
        }

        fields.each { it.validate() }
        wheres.each { it.validate() }
        orders.each { it.validate() }
    }

    static class FieldNotFoundException extends AdhocException {
        FieldNotFoundException(String name) {
            super("Field not found: " + name)
        }
    }

    static class NoFieldsException extends AdhocException {
        NoFieldsException() {
            super("The select has no fields")
        }
    }

}
