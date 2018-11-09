package epitech.epicture

import android.os.AsyncTask.execute
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.Exception


class Imgur {
    data class Item(val id: String, val title: String, val favorite : Boolean, val link: String)

    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()

        var loggedIn: Boolean = false
        var username: String? = null
        var accessToken: String? = null
        var refreshToken: String? = null

        private fun parseItemsFromResponse(response: Response) : List<Item>
        {
            val json = response.body()?.string()
            val jsonObj = JSONObject(json?.substring(json.indexOf("{"), json.lastIndexOf("}") + 1))
            val data = jsonObj.getJSONArray("data")
            return gson.fromJson(data.toString(), object : TypeToken<List<Item>>() {}.type)
        }

        private fun requestSelfImages() : Response
        {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.imgur.com/3/account/me/images")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("Authorization", "Bearer ${Imgur.accessToken}")
                .build()

            return client.newCall(request).execute()
        }

        private fun requestFavoriteImages() : Response
        {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.imgur.com/3/account/me/favorites")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("Authorization", "Bearer ${Imgur.accessToken}")
                .build()

            return client.newCall(request).execute()
        }

        fun getSelfImages(): List<Item> {
            var items: List<Item> = try {
                val response = requestSelfImages()
                parseItemsFromResponse(response)
            } catch (e: Exception) {
                listOf()
            }
            return items
        }

        fun getFavoriteImages(): List<Item> {
            var items: List<Item> = try {
                val response = requestFavoriteImages()
                parseItemsFromResponse(response)
            } catch (e: Exception) {
                listOf()
            }
            return items
        }

    }
}