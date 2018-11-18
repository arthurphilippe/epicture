package epitech.epicture

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import android.widget.EditText
import okhttp3.RequestBody


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
            selectionMade = true
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
    }

    private fun openImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), requestPickImage)
    }

    private fun onUploadImage(){
        val url = "https://api.imgur.com/3/upload"

        if (selectionMade) {
            selectionMade = false
            buttonUpload.text = "Uploading..."

            Thread{
                val b64 = compressBitmap()
                val (title, description) = getDetails()

                val requestBody = getRequestBody(b64, title, description)
                var response = Imgur.post(url, requestBody)
                val result = Imgur.parseItemFromResponse(response)

                Toast.makeText(this, result.link, Toast.LENGTH_LONG).show()
                finish()
            }.start()
        }
    }

    private fun getRequestBody(
        b64: String?,
        title: String,
        description: String
    ): RequestBody {
        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("type", "file")
            .addFormDataPart("image", b64)
            .addFormDataPart("name", title)
            .addFormDataPart("title", title)
            .addFormDataPart("description", description)
            .build()
    }

    private fun getDetails(): Pair<String, String> {
        var text = findViewById<EditText>(R.id.imageTitle)
        val title = text.text.toString()
        text = findViewById(R.id.imageDescription)
        val description = text.text.toString()
        return Pair(title, description)
    }

    private fun compressBitmap(): String? {
        val stream = ByteArrayOutputStream()
        imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return Base64.encodeToString(File(path).readBytes(), 0)
    }
}
