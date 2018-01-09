package org.mrpaulwoods.adhoc.core.exception

class NoSelectsException extends AdhocException {
    NoSelectsException() {
        super("The query has no selected fields.")
    }
}
