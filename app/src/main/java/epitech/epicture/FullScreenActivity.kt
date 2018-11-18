package epitech.epicture

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso

class FullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)


        val intent : Intent = getIntent()

        val extras : Bundle = intent.getExtras()
        val lol : String = extras.getString("hey")
        Toast.makeText(this, lol, Toast.LENGTH_LONG).show()
        val img: ImageView = findViewById(R.id.imgFull)
        Picasso.with(this).load(lol).into(img)

    }
}
