package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Conjunction implements Serializable {

    static final long serialVersionUID = 0

    String name

    private Conjunction() {}

    static final conjunctions = Arrays.asList(
            new Conjunction(name: "and"),
            new Conjunction(name: "or"))

    static Conjunction findByName(String name) {
        Conjunction conjunction = conjunctions.find { it.name.equalsIgnoreCase(name) }
        if (conjunction) {
            conjunction
        } else {
            throw new NotFoundException(name)
        }
    }

    static class NotFoundException extends AdhocException {
        NotFoundException(String name) {
            super("Conjunction not found: " + name)
        }
    }

}
