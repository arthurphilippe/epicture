package epitech.epicture

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_my_images.view.*


class ImagesRvAdapter(val context: Context, var lists: List<Imgur.Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.content_my_images, parent, false)
        return Item(context, v)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Item).bindData(lists[position])
    }

    class Item(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(entry: Imgur.Item) {
            itemView.title.text = entry.title
            Picasso
                .with(context) // give it the context
                .load(entry.link) // load the image
                .into(itemView.image) // select the ImageView to load it into

        }
    }
}
class MyImagesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var dataSet: List<Imgur.Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_images)

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
}
