package epitech.epicture

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
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