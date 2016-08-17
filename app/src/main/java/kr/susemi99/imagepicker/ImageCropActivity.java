package kr.susemi99.imagepicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCropActivity extends AppCompatActivity {
//  private CropView cropView;
  private CropImageView cropView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_crop);

    String path = getIntent().getStringExtra("path");

//    cropView = (CropView) findViewById(R.id.crop_view);
//    cropView.setImageURI(Uri.parse("file://" + path));
    cropView = (CropImageView) findViewById(R.id.crop_view);
    cropView.setImageUriAsync(Uri.parse("file://" + path));

    findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());
    findViewById(R.id.btn_ok).setOnClickListener(v -> saveToFile());
  }

  private void saveToFile() {
    File file = null;
    try {
      file = File.createTempFile("prefix", "suffix");
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(file);
//        Bitmap bitmap = cropView.crop();
        Bitmap bitmap = cropView.getCroppedImage();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
      } finally {
        if (fos != null) fos.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    Intent intent = new Intent();
    intent.putExtra("path", file.getPath());
    setResult(RESULT_OK, intent);
    finish();
  }
}
