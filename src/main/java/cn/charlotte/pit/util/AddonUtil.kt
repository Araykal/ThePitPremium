package cn.charlotte.pit.util

import cn.charlotte.pit.ThePit
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import sinc.Native
import java.io.StringReader

/**
 * @author Araykal
 * @since 2025/2/7
 */
@Native
object AddonUtil {
    fun check(addon: String): Boolean {
        var version = false
        HttpClientBuilder.create().build().use { client ->
            val get = HttpGet("http://43.248.187.113:24608/text?addon=${addon}")
            val result = EntityUtils.toString(client.execute(get).entity)
            val jsonReader = JsonReader(StringReader(result))
            if (result.contains("Not")) {
                return false
            }
            jsonReader.isLenient = true
            val jsSet = JsonParser().parse(jsonReader).asJsonObject.get(addon)?.asJsonArray?.map { element ->
                element.asString
            }?.toSet() ?: emptySet()
            if (jsSet.contains(ThePit.getInstance().pitConfig.code)) {
                version = true
            }
        }
        return version
    }

}