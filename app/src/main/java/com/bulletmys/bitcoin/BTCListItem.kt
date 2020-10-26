package com.bulletmys.bitcoin

import java.util.*

data class BTCListItem(
    var date: Date,
    var cost: String?,
    var currency: String,
    val minPrice: String?,
    val maxPrice: String?
)