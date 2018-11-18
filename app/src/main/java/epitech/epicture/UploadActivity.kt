package epitech.epicture

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.IOException

class UploadActivity : AppCompatActivity() {
    private val requestPickImage = 1
    private var image : Intent? = null
    private var selectionMade : Boolean = false
    private var path : String? = null
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        buttonUpload.visibility = View.INVISIBLE
        buttonPick.setOnClickListener{onPickImage()}
        buttonUpload.setOnClickListener{onUploadImage()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestPickImage
            && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            image = data
            selectionMade = true
            val uri = data.data

            setBitmap(uri)
            setPath(uri)
        }
    }

    private fun setPath(uri: Uri) {
        try {
            var id = uri.getLastPathSegment().split(":")[1]
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var imageOrderBy = null

            var uriNew = getUri()

            var imageCursor = deprecated@ managedQuery(
                uriNew,
                projection,
                MediaStore.Images.Media._ID + "=" + id,
                null,
                imageOrderBy
            )

            if (imageCursor.moveToFirst()) {
                path = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }

            buttonUpload.visibility = View.VISIBLE
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setBitmap(uri: Uri?) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imageBitmap = bitmap
            val imageView = findViewById<ImageView>(R.id.image)
            imageView.setImageBitmap(bitmap)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getUri(): Uri {
        val state = Environment.getExternalStorageState()
        return if (!state.equals(Environment.MEDIA_MOUNTED, ignoreCase = true))
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    private fun onPickImage(){
        if (!permissionsCheckReadExternalStorage(this)) {
            permissionsRequestReadExternalStorage(this)
            return
        }
        openImagePicker()
//        buttonUpload.visibility = View.VISIBLE
    }

    private fun openImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), requestPickImage)
    }

    private fun onUploadImage(){

    }
}
