package co.sundroid.imagecompression;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public Button btn_capture;
    public ImageView image;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_capture = findViewById(R.id.btn_capture);
        editText = findViewById(R.id.et_ratio);
        image = findViewById(R.id.image);

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(editText.getText().toString()) > 0 && Integer.parseInt(editText.getText().toString()) < 101) {
                    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    Permissions.check(MainActivity.this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            // do your task.
                            createFolders();
                            openCamera();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter correct range", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createFolders() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "imgcomp");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void openCamera() {
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths.size() > 0) {
                try {
                    String file_path = Environment.getExternalStorageDirectory() + File.separator + "imgcomp" + File.separator + System.currentTimeMillis() + ".png";
                    FileOutputStream out = new FileOutputStream(file_path);
                    Bitmap myBitmap = BitmapFactory.decodeFile(mPaths.get(0));

                    Log.d("MainActivity", "cond:-" + myBitmap.compress(Bitmap.CompressFormat.JPEG, Integer.parseInt(editText.getText().toString()), out));


                    //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    image.setImageBitmap(myBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
