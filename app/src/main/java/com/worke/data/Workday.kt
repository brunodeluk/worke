package com.worke.data

import java.util.Date

data class Workday(
    var start: Date,
    var end: Date,
    var title: String,
    var description: String
) {
    constructor() : this(Date(), Date(), "", "")
}
