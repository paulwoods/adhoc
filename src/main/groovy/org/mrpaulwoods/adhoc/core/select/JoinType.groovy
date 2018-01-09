package org.mrpaulwoods.adhoc.core.select

import org.mrpaulwoods.adhoc.core.exception.AdhocException

class JoinType implements Serializable {

    static final long serialVersionUID = 0

    String name

    private JoinType() {}

    static final joinTypes = Arrays.asList(
            new JoinType(name: "inner"),
            new JoinType(name: "left"),
            new JoinType(name: "right")
    )

    static JoinType findByName(String name) {
        JoinType joinType = joinTypes.find { it.name == name }
        if (joinType) {
            joinType
        } else {
            throw new NotFoundException(name)
        }
    }

    static class NotFoundException extends AdhocException {
        NotFoundException(String name) {
            super("Join columnType not found: " + name)
        }
    }

}
