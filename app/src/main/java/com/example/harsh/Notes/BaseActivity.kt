package com.example.harsh.Notes

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.Process
import android.provider.Settings
import android.speech.RecognizerIntent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.harsh.Notes.NoteUtils.*
import kotlinx.android.synthetic.main.create_notes_layout.*
import java.util.ArrayList

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermissions(this, getNotesAllPermissions())) {
            requestPermissions(getNotesAllPermissions(), REQUEST_CODE_APP_PERMISSION)
            Process.killProcess(Process.myPid())
        }
    }

    fun checkPermissionForAudio() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 99)
    }

    fun startRecognizeVoice() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
        try {
            startActivityForResult(intent, RESULT_SPEECH)
        } catch (a: ActivityNotFoundException) {
            showToast("Download Voice Search App")
        }
    }

    open fun onRecognizeVoiceText(texts: ArrayList<String?>?) {

    }

    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(applicationContext, msg, duration).show()
    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(Notes_data.windowToken, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_SPEECH) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                onRecognizeVoiceText(text)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_APP_PERMISSION) {
            for (i in getNotesAllPermissions().indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                    finish()
                }
            }
        }
    }
}