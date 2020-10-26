package com.bulletmys.bitcoin.models

import com.squareup.moshi.Json

data class Response(
    val Response: String,
    val HasWarning: Boolean,
    @Json(name = "Data") val data: Data
)

data class Data(
    @Json(name = "Data") val data: List<BTCInfo>
)

data class BTCInfo(
    var time: Long,
    var high: String? = null,
    var low: String? = null,
    var open: String? = null,
    var close: String? = null
)