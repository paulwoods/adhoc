package org.mrpaulwoods.adhoc.core.execute

class ConnectionBuilder {

    final Connection connection = new Connection()

    Connection end() {
        connection
    }

    ConnectionBuilder vendor(String vendor) {
        connection.vendor = vendor
        this
    }

    ConnectionBuilder host(String host) {
        connection.host = host
        this
    }

    ConnectionBuilder port(int port) {
        connection.port = port
        this
    }

    ConnectionBuilder username(String username) {
        connection.username = username
        this
    }

    ConnectionBuilder password(String password) {
        connection.password = password
        this
    }

    ConnectionBuilder database(String database) {
        connection.database = database
        this
    }

    ConnectionBuilder parameter(String key, String value) {
        connection.parameters[key] = value
        this
    }

}
