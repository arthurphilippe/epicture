package epitech.epicture

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.os.StrictMode
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.nav_header_main.*
import android.widget.Toast



class  MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    enum class Mode {
        GALLERY, FAVORITES
    }

    private var mode: Mode = Mode.GALLERY

    //! Recycler view variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var dataSet: List<Imgur.Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //! Permit http requests
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //! Init FAB
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //! Init drawer
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        //! Login if needed
        if (!Imgur.loggedIn) {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        } else {
            drawerUsername.text = Imgur.username
            initRv()
        }
    }

    private fun initRv() {
        viewManager = LinearLayoutManager(this)
        dataSet = Imgur.getSelfImages()
        viewAdapter = ImagesRvAdapter(this, dataSet)
        recyclerView = findViewById<RecyclerView>(R.id.rv).apply {
            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

    }

    override fun onResume() {
        if (Imgur.loggedIn) {
            drawerUsername.text = Imgur.username
            initRv()
        }
        super.onResume()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_import -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {
                dataSet = Imgur.getSelfImages()
                viewAdapter = ImagesRvAdapter(this, dataSet)
                rv.adapter = viewAdapter
            }
            R.id.nav_favorites -> {
                dataSet = Imgur.getFavoriteImages()
                viewAdapter = ImagesRvAdapter(this, dataSet)
                rv.adapter = viewAdapter
            }
            R.id.nav_search-> {
                val i = Intent(this, SearchActivity::class.java)
                startActivity(i)
            }
            R.id.nav_search -> {

            }
            R.id.nav_share -> {
                val url = "http://www.google.com/"
                val obj = URL(url)

                with(obj.openConnection() as HttpURLConnection) {
                    // optional default is GET
                    requestMethod = "GET"


                    println("\nSending 'GET' request to URL : $url")
                    println("Response Code : $responseCode")

                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        println(response.toString())
                    }
                }
            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
