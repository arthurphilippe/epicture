package epitech.epicture

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        holder.itemView.image.setOnClickListener {
            //Toast.makeText(context, lists[position].link, Toast.LENGTH_LONG).show()
            val intent = Intent( context, FullScreenActivity::class.java )
            intent.putExtra("hey", lists[position].link)
            context.startActivity(intent)
        }
    }

    class Item(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(entry: Imgur.Item) {
            if (entry.title != null && !entry.title.isEmpty())
                itemView.title.text = entry.title
            if (entry.description != null && !entry.description.isEmpty())
                itemView.description.text = entry?.description
            Picasso
                .with(context) // give it the context
                .load(entry.link) // load the image
                .into(itemView.image) // select the ImageView to load it into
            Thread(Runnable{
                if (entry.favorite) {
                    itemView.buttonFav.setText("Remove from Favorites")
//                    itemView.buttonFav.text = "Remove from Favorites"
                } else {
                    itemView.buttonFav.setText("Add to Favorites")
                    //itemView.buttonFav.text =
                }
            }).start()
            itemView.buttonFav.setOnClickListener{
                    if (itemView.buttonFav.text == "Add to Favorites") {
                        itemView.buttonFav.setText("Remove from Favorites")
                    } else {
                        itemView.buttonFav.setText("Add to Favorites")
                    }
                Imgur.toggleFavorite(entry.id)
            }
        }
    }
}