package edu.washington.recyclednasaimages

import android.webkit.URLUtil
import org.json.JSONObject

data class AstronomyData(
    val title: String,
    val urlString: String?,
    val explanation: String)

fun parseAstronomyData(astronomyJson: JSONObject): AstronomyData {
    var urlString: String? = astronomyJson.getString("url")
    if (urlString == "null" || !URLUtil.isValidUrl(urlString)) {
        urlString = null
    }

    var title = astronomyJson.getString("title")
    var explanation = astronomyJson.getString("explanation")

    return AstronomyData(title, urlString, explanation)
}