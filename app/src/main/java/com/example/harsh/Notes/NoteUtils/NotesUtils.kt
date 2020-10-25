package com.example.harsh.Notes.NoteUtils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class NotesUtils {
    companion object {
        fun checkPermissions(activity: Activity, permissionList: Array<String>): Boolean {
            for (permission in permissionList) {
                if (ContextCompat.checkSelfPermission(activity, permission)
                        == PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
            return true
        }

        fun getNotesAllPermissions() = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest
                .permission.READ_EXTERNAL_STORAGE)
    }
}