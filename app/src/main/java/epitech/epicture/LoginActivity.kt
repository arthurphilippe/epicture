package epitech.epicture

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

class LoginActivity : AppCompatActivity() {

    private fun splitUrl(url: String, view: WebView) {
        val outerSplit =
            url.split("\\#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("\\&".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()
        //var username: String? = null
        //var accessToken: String? = null

        var index = 0

        for (s in outerSplit) {
            val innerSplit = s.split("\\=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            when (index) {
                // Access Token
                // 0 -> accessToken = innerSplit[1]
                0 -> Imgur.accessToken = innerSplit[1]

                // Refresh Token
                // 3 -> refreshToken = innerSplit[1]
                3 -> Imgur.refreshToken = innerSplit[1]

                // Username
                4 -> Imgur.username = innerSplit[1]
            }

            index++
        }
        if (Imgur.accessToken != null && Imgur.username != null)
        Imgur.loggedIn = true
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val imgurWebView = findViewById(R.id.loginview) as WebView
        //imgurWebView.setBackgroundColor(Color.TRANSPARENT)
        imgurWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=d4fc4058731bcf0&response_type=token&state=APPLICATION_STATE")
        imgurWebView.getSettings().setJavaScriptEnabled(true)


        imgurWebView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //val url = request.url.toString()
                print(url)

                if (url.contains("https://kappawakawai.fr")) {
                    splitUrl(url, view)
                    Log.d("TOTO", "kappa")
                } else {
                    view.loadUrl(url)
                    Log.d("TOTO", "pas kappa")
                }

                return true
            }
        })

    }
}
