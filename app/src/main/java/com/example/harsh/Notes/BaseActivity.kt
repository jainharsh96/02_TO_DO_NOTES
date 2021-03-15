package com.example.harsh.Notes

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.Process
import android.provider.Settings
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

    fun checkPermissionForAudion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 99)
        }
    }

    fun startRecognizeVoice() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
        try {
            startActivityForResult(intent, NotesActivity.RESULT_SPEECH)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                    applicationContext,
                    "Download Voice Search App",
                    Toast.LENGTH_SHORT).show()
        }
    }

    open fun onRecognizeVoiceText(texts: ArrayList<String>) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NotesActivity.RESULT_SPEECH) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                onRecognizeVoiceText(text)
            }
        }
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