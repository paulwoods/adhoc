package org.mrpaulwoods.adhoc.core.execute

class UserValues implements Serializable {

    static final long serialVersionUID = 0

    // LinkedHashMap keeps keys in order of insertion
    final Map<String, List<Object>> values = new LinkedHashMap<>()

    UserValues add(String name, Object value) {
        values.put name, [value]
        this
    }

    UserValues add(String name, List<Object> value) {
        values.put name, value
        this
    }

    int size() {
        values.size()
    }

    boolean isEmpty() {
        values.isEmpty()
    }
}
