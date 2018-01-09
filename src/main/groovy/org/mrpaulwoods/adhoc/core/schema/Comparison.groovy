package org.mrpaulwoods.adhoc.core.schema

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Comparison implements Serializable {

    static final long serialVersionUID = 0

    String name
    String code
    String prefix
    String suffix
    Scope scope

    private Comparison() {}

    static final comparisons = Arrays.asList(
            new Comparison(name: "null", code: "is null", scope: Scope.findByName("NONE")),
            new Comparison(name: "not null", code: "is not null", scope: Scope.findByName("NONE")),

            new Comparison(name: "=", code: "=", scope: Scope.findByName("ONE")),
            new Comparison(name: "<>", code: "<>", scope: Scope.findByName("ONE")),
            new Comparison(name: "<", code: "<", scope: Scope.findByName("ONE")),
            new Comparison(name: ">", code: ">", scope: Scope.findByName("ONE")),
            new Comparison(name: "<=", code: "<=", scope: Scope.findByName("ONE")),
            new Comparison(name: ">=", code: ">=", scope: Scope.findByName("ONE")),

            new Comparison(name: "begins with", code: "like", scope: Scope.findByName("ONE"), suffix: "%"),
            new Comparison(name: "contains", code: "like", scope: Scope.findByName("ONE"), prefix: "%", suffix: "%"),
            new Comparison(name: "ends with", code: "like", scope: Scope.findByName("ONE"), prefix: "%"),
            new Comparison(name: "not begins with", code: "not like", scope: Scope.findByName("ONE"), suffix: "%"),
            new Comparison(name: "not contains", code: "not like", scope: Scope.findByName("ONE"), prefix: "%", suffix: "%"),
            new Comparison(name: "not ends with", code: "not like", scope: Scope.findByName("ONE"), prefix: "%"),

            new Comparison(name: "between", code: "between", scope: Scope.findByName("TWO")),

            new Comparison(name: "in list", code: "in", scope: Scope.findByName("LIST")),
            new Comparison(name: "not in list", code: "not in", scope: Scope.findByName("LIST")),
    )

    static Comparison findByName(String name) {
        Comparison comparison = comparisons.find { it.name == name }
        if (comparison) {
            comparison
        } else {
            throw new NotFoundException(name)
        }
    }

    static class NotFoundException extends AdhocException {
        NotFoundException(String name) {
            super("Comparison not found: " + name)
        }
    }

}
