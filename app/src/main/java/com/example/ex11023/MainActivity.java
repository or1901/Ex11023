package com.example.ex11023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This function checks if the external storage is available.
     * @return Whether the external storage is available or not.
     */
    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * This function checks if there is a permission to write to the external storage.
     * @return Whether there is a permission to write to the external storage, or not.
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This function requests permission from the user to write to the external storage.
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION);
    }

    /**
     * This function reacts to the user's answer to a permission request.
     * @param requestCode The request code passed in.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission to access external storage granted",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Permission to access external storage NOT granted",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}