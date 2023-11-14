package com.example.ex11023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The main activity:
 * <p>
 * Reads and adds text to an external file, according to different buttons.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 14/11/2023
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 1;
    private final String FILENAME = "extFile.txt";
    private boolean storageExist, permExist;
    TextView tV;
    EditText eT;
    Intent si;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.tV);
        eT = (EditText) findViewById(R.id.eT);

        if(checkStorage()){
            // Displays the file content as the app starts
            String fileContent = readFile();
            tV.setText(fileContent);
        }
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
                Toast.makeText(this, "Permission to access external storage denied",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This function checks if the external storage is available and there is a permission to access
     * it. If not, asks permission and displays suitable Toasts.
     * @return Whether the external storage is available and there is permission to it, or not.
     */
    public boolean checkStorage(){
        storageExist = isExternalStorageAvailable();
        permExist = checkPermission();

        if (!storageExist) {
            Toast.makeText(this, "External memory isn't available",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!permExist) {
            requestPermission();
        }

        return storageExist && permExist;
    }

    /**
     * This function reads the text from the external file, and returns it as a string.
     * @return The text written in the file.
     */
    public String readFile() {
        String fileContent = "";

        try {
            // Inits the variables in order to read from the file
            File externalDir = Environment.getExternalStorageDirectory();
            File file = new File(externalDir, FILENAME);
            file.getParentFile().mkdirs();
            FileReader reader = new FileReader(file);
            BufferedReader bR = new BufferedReader(reader);
            StringBuilder sB = new StringBuilder();

            // Reads from file
            String line = bR.readLine();
            while (line != null) {
                sB.append(line+'\n');
                line = bR.readLine();
            }
            bR.close();
            reader.close();

            fileContent = sB.toString();
        }
        catch (IOException e){
            Toast.makeText(this, "Error reading file", Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "Error reading file");
        }

        return fileContent;
    }

    /**
     * This function writes a given text to the external file.
     * @param text the text to write to the file.
     */
    public void writeToFile(String text) {
        try {
            // Inits the variables in order to write to the file
            File externalDir = Environment.getExternalStorageDirectory();
            File file = new File(externalDir, FILENAME);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);

            writer.write(text);  // Writes to the external file
            writer.close();
        }
        catch (IOException e) {
            Toast.makeText(this, "Error writing to file", Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "Error writing to file");
        }
    }

    /**
     * This function adds the input of the edit text to the external file content,
     * if external storage is available.
     * @param view The button clicked to save the input to the file.
     */
    public void save(View view) {
        String prevContent = "";

        if(checkStorage())
        {
            prevContent = readFile();

            if(prevContent.length() > 0)
                // Removes the extra '\n'
                prevContent = prevContent.substring(0, prevContent.length() - 1);

            prevContent += eT.getText().toString();  // Adds the edit text input

            writeToFile(prevContent);
            tV.setText(prevContent);
        }
    }

    /**
     * This function resets the content in the external file, and the text view,
     * if external storage is available.
     * @param view The button clicked to reset the file.
     */
    public void reset(View view) {
        if(checkStorage())
        {
            writeToFile("");  // Resets the file content
            tV.setText("");
        }
    }

    /**
     * This function adds the input of the edit text to the file content, and exits the app,
     * if external storage is available.
     * @param view The button clicked to save and exit.
     */
    public void exit(View view) {
        if(checkStorage()) {
            save(view);  // Adds the input of the edit text to the file
            finish();
        }
    }

    /**
     * This function presents the options menu for moving between activities.
     * @param menu The options menu in which you place your items.
     * @return true in order to show the menu, otherwise false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function reacts to the user choice in the options menu - it moves to the chosen
     * activity from the menu.
     * @param item The menu item that was selected.
     * @return Must return true for the menu to react.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.menuCredits){
            si = new Intent(this, CreditsActivity.class);
            startActivity(si);
        }

        return super.onOptionsItemSelected(item);
    }
}