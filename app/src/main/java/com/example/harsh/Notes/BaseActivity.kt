package com.example.harsh.Notes

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.os.Process
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.harsh.Notes.NoteUtils.NotesConstants.REQUEST_CODE_APP_PERMISSION
import com.example.harsh.Notes.NoteUtils.NotesUtils

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onResume() {
        super.onResume()
        if (!NotesUtils.checkPermissions(this, NotesUtils.getNotesAllPermissions())) {
            requestPermissions(NotesUtils.getNotesAllPermissions(), REQUEST_CODE_APP_PERMISSION)
            Process.killProcess(Process.myPid())
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_APP_PERMISSION) {
            for (i in NotesUtils.getNotesAllPermissions().indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)))
                    finish()
                }
            }
        }
    }
}