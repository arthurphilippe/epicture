package epitech.epicture

import android.os.AsyncTask.execute
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class Imgur {
    companion object {
        var loggedIn: Boolean = false
        var username: String? = null
        var accessToken: String? = null
        var refreshToken: String? = null

        fun getSelfImages(): String? {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.imgur.com/3/account/me/images")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("Authorization", "Bearer ${Imgur.accessToken}")
                .build()

            val response = client.newCall(request).execute()
            val json = response.body()
            return json?.string()
        }
    }
}