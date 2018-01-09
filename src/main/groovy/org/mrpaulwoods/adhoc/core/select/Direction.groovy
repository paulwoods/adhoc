package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class Direction implements Serializable {

    static final long serialVersionUID = 0

    String name

    private Direction() {}

    static final directions = Arrays.asList(
            new Direction(name: "asc"),
            new Direction(name: "desc")
    )

    static Direction findByName(String name) {
        Direction direction = directions.find { it.name.equalsIgnoreCase(name) }
        if (direction) {
            direction
        } else {
            throw new NotFoundException(name)
        }
    }

    static class NotFoundException extends AdhocException {
        NotFoundException(String name) {
            super("Direction not found: " + name)
        }
    }

}

