package epitech.epicture

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

fun permissionsCheckReadExternalStorage(context: Context): Boolean {
    return Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M
            || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

fun permissionsRequestReadExternalStorage(context: Context) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            context as Activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )) {
        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 123
        )
    } else {
        ActivityCompat
            .requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 123)
    }
}
