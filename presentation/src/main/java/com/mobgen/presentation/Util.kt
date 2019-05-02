package com.mobgen.presentation

import android.content.ContentResolver
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.mobgen.domain.check
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Util {

    companion object {

        private const val FILE_DATE_FORMAT = "yyyyMMdd_HHmmss"

        fun getPathFromURI(uri: Uri, contentResolver: ContentResolver): String {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var path = ""
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor.check(
                ifNotNull = {
                    val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    it.moveToFirst()
                    path = it.getString(columnIndex).toString()
                },
                ifNull = {
                    throw Throwable("Uri can not be obtained")
                }
            )

            cursor?.close()
            return path
        }

        fun getOutputMediaFile(nameDirectory: String): File {
            val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ), nameDirectory
            )

            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                throw Throwable("File can not be created")
            }

            val timeStamp = SimpleDateFormat(FILE_DATE_FORMAT, Locale.ENGLISH).format(Date())
            return File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
        }
    }
}