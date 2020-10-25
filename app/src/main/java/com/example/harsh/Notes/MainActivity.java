package com.example.harsh.Notes;

import com.example.harsh.Notes.NoteUtils.NotesUtils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.harsh.Notes.NoteUtils.NotesConstants.REQUEST_CODE_APP_PERMISSION;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] permissionList =
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        if (NotesUtils.Companion.checkPermissions(this, permissionList)) {
            openNotesActivity();
        } else {
            requestPermissions(permissionList, REQUEST_CODE_APP_PERMISSION);
        }
    }

    private void openNotesActivity() {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_APP_PERMISSION) {
            for (int i = 0; i < permissionList.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                    finish();
                    return;
                }
            }
            openNotesActivity();
        }
    }
}
