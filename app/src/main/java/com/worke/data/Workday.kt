package com.worke.data

import java.util.Date

data class Workday constructor(
    var id: Int,
    var startDateTime: Date,
    var endDateTime: Date,
    var title: String = "",
    var description: String = ""
)
