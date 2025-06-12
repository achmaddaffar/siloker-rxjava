package com.oliver.siloker.data.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object DownloadUtil {

    fun saveFileToPublicDownloads(
        context: Context,
        fileName: String,
        mimeType: String,
        body: ResponseBody
    ): Boolean {
        return try {
            val outputStream: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore for API 29+
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, mimeType)
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                val itemUri = resolver.insert(collection, contentValues) ?: return false

                val stream = resolver.openOutputStream(itemUri)

                // Mark as not pending (download complete)
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(itemUri, contentValues, null, null)

                stream
            } else {
                // Legacy method for API 26–28
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                val file = File(downloadsDir, fileName)
                FileOutputStream(file)
            }

            outputStream?.use { out ->
                body.byteStream().copyTo(out)
                out.flush()
            }

            true
        } catch (e: Exception) {
            Log.e("Download", "Saving failed", e)
            false
        }
    }
}