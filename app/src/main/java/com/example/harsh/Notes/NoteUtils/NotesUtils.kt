package com.example.harsh.Notes.NoteUtils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.provider.MediaStore

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import java.io.File
import android.content.ContentUris
import java.text.SimpleDateFormat
import java.util.*


fun checkPermissions(activity: Activity, permissionList: Array<String>): Boolean {
    for (permission in permissionList) {
        if (ContextCompat.checkSelfPermission(activity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {
            return false
        }
    }
    return true
}

fun getNotesAllPermissions() = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest
        .permission.READ_EXTERNAL_STORAGE
)

fun dateInFormate(date: Date, formate: String): String {
    return SimpleDateFormat(formate, Locale.getDefault()).format(date)
}

fun noteDateFormated(date: Date) = dateInFormate(date, NOTE_DATE_FORMAT)

fun getFilePathToMediaID(songPath: String, context: Context): Long {
    var id: Long = 0
    val cr: ContentResolver = context.getContentResolver()
    val uri: Uri = MediaStore.Files.getContentUri("external")
    val selection = MediaStore.Audio.Media.DATA
    val selectionArgs = arrayOf(songPath)
    val projection = arrayOf(MediaStore.Audio.Media._ID)
    val cursor: Cursor? = cr.query(uri, projection, "$selection=?", selectionArgs, null)
    if (cursor != null) {
        while (cursor.moveToNext()) {
            val idIndex: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            id = cursor.getString(idIndex).toLong()
        }
        cursor.close()
    }
    return id
}

fun getDBMediaUri(
    filePath: String,
    context: Context
): Uri {   // "/storage/emulated/0/tempPic/export_image.jpg"
    val tempFile = File(filePath)
    val mediaID = getFilePathToMediaID(tempFile.getAbsolutePath(), context)
    return ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), mediaID)
}