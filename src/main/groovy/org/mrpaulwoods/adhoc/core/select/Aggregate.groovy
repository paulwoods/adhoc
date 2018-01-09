package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Aggregate implements Serializable {

    static final long serialVersionUID = 0

    String name
    String code

    private Aggregate() {}

    static final aggregates = Arrays.asList(
            new Aggregate(name: "Average", code: "avg(data)"),
            new Aggregate(name: "Count", code: "count(data)"),
            new Aggregate(name: "Count Distinct", code: "count(distinct data)"),
            new Aggregate(name: "Minimum", code: "min(data)"),
            new Aggregate(name: "Maximum", code: "max(data)"),
            new Aggregate(name: "Standard Deviation", code: "std(data)"),
            new Aggregate(name: "Sum", code: "sum(data)"),
            new Aggregate(name: "Variance", code: "variance(data)")
    )

    static Aggregate findByName(String name) {
        Aggregate aggregate = aggregates.find { it.name.equalsIgnoreCase(name) }
        if (aggregate) {
            aggregate
        } else {
            throw new NotFoundException(name)
        }
    }

    static class NotFoundException extends AdhocException {
        NotFoundException(String name) {
            super("Aggregate not found: " + name)
        }
    }

}
