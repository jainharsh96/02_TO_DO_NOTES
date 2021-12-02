package com.example.harsh.Notes

//import android.Manifest
//import android.app.Activity
//import androidx.appcompat.app.AppCompatActivity
//import androidx.biometric.BiometricPrompt.PromptInfo
//import android.os.Bundle
//import com.example.harsh.Notes.MainActivity
//import com.example.harsh.Notes.R
//import android.content.Intent
//import com.example.harsh.Notes.NotesActivity
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.provider.Settings
//import android.speech.RecognizerIntent
//import android.util.Log
//import androidx.core.content.ContextCompat
//import android.widget.Toast
//import androidx.biometric.BiometricPrompt
//import androidx.lifecycle.lifecycleScope
//import com.example.harsh.Notes.NoteDatabase.NotesDatabase
//import com.example.harsh.Notes.NoteUtils.*
//import kotlinx.coroutines.*
//import java.io.File
//import java.util.concurrent.Executor
//import kotlin.coroutines.CoroutineContext
//
//class MainActivity : AppCompatActivity() {
//    private var biometricPrompt: BiometricPrompt? = null
//    private var promptInfo: PromptInfo? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.e(TAG, "onCreate: ")
//        setContentView(R.layout.activity_main_layout)
//        if (checkPermissions(this, permissionList)) {
////            //todo remove this
////            openNotesActivity()
////            finish()
//            requestForEditFile()
//            //nextTask();
//        } else {
//            requestPermissions(permissionList, REQUEST_CODE_APP_PERMISSION)
//        }
//    }
//
//    private fun openNotesActivity() {
//        NotesActivity.startActivity(this)
//        finish()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//      //  activityScope.cancel()
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == REQUEST_CODE_APP_PERMISSION) {
//            for (i in permissionList.indices) {
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    startActivity(
//                        Intent(
//                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
//                        )
//                    )
//                    finish()
//                    return
//                }
//            }
//            nextTask()
//        }
//    }
//
//    private fun nextTask() {
//        biometricPrompt = BiometricPrompt(this,
//            ContextCompat.getMainExecutor(this), object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationError(
//                    errorCode: Int,
//                    errString: CharSequence
//                ) {
//                    Toast.makeText(
//                        applicationContext, errString,
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                    openNotesActivity()
//                    finish()
//                }
//
//                override fun onAuthenticationSucceeded(
//                    result: BiometricPrompt.AuthenticationResult
//                ) {
//                    super.onAuthenticationSucceeded(result)
//                    openNotesActivity()
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    Toast.makeText(
//                        applicationContext, "Authentication failed",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//            })
//        promptInfo = PromptInfo.Builder()
//            .setTitle("Biometric login")
//            .setSubtitle("Log in using your biometric credential")
//            .setNegativeButtonText(BIOMETRIC_CLICK_CANCEL)
//            .build()
//
//        // Prompt appears when user clicks "Log in".
//        // Consider integrating with the keystore to unlock cryptographic operations,
//        // if needed by your app.
////        Button biometricLoginButton = findViewById(R.id.biometric_login);
////        biometricLoginButton.setOnClickListener(view -> {
////        });
//        biometricPrompt!!.authenticate(promptInfo!!)
//    }
//
//    companion object {
//        val TAG = MainActivity::class.java.simpleName
//        private val permissionList = arrayOf(
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//    }
//
//    private fun requestForEditFile(){
//        val urisToModify = listOf(getDBMediaUri(NotesDatabase.DB_FILE_PATH + NotesDatabase.DATABASE_NAME, this))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val editPendingIntent = MediaStore.createWriteRequest(contentResolver, urisToModify);
//            startIntentSenderForResult(editPendingIntent.getIntentSender(),
//                REQUEST_CODE_WRITE_FILE_PERMISSION, null, 0, 0, 0);
//        } else {
//            openNotesActivity()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_WRITE_FILE_PERMISSION){
//            openNotesActivity()
//            finish()
//        }
//    }
//}