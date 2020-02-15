package com.worke.data

import java.util.Date

data class Workday(
    var time: Long?,
    var date: Date?,
    val displayDate: String?,
    val displayTime: String?
) {
    constructor(): this(null, null, "", "")
}

