package org.mrpaulwoods.adhoc.core.execute

class Connection implements Serializable {

    static final long serialVersionUID = 0

    String vendor
    String host
    int port
    String username
    String password
    String database
    final Map<String, String> parameters = new LinkedHashMap<>()
}
