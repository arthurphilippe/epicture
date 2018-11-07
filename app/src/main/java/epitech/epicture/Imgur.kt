package epitech.epicture

import android.os.AsyncTask.execute
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class Imgur {
    data class Item(val id: String, val title: String, val favorite : Boolean, val link: String)

    companion object {
        val gson = GsonBuilder().setPrettyPrinting().create()

        var loggedIn: Boolean = false
        var username: String? = null
        var accessToken: String? = null
        var refreshToken: String? = null

        fun getSelfImages(): List<Item> {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.imgur.com/3/account/me/images")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("Authorization", "Bearer ${Imgur.accessToken}")
                .build()

            val response = client.newCall(request).execute()
            val json = response.body()?.string()
            val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
            val data = jsonObj.getJSONArray("data")
            var items : List<Item> = gson.fromJson(data.toString(), object : TypeToken<List<Item>>() {}.type)
            return items
        }
    }
}