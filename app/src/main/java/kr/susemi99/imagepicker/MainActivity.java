package kr.susemi99.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {

  private ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.btn_select).setOnClickListener(v -> selectImage());
    imageView = (ImageView) findViewById(R.id.image_view);
  }

  private void selectImage() {
    startActivityForResult(new Intent(getApplicationContext(), ImagePickerActivity.class), 1);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != RESULT_OK) {
      return;
    }

    String path = data.getStringExtra("path");
    Picasso.with(getApplicationContext()).load(new File(path)).into(imageView);

  }
}
