package org.mrpaulwoods.adhoc.core.report

class Report implements Serializable {

    static final long serialVersionUID = 0

    final List<HeaderItem> headers = []
    final List<RowItem> rows = []
}
