package kr.susemi99.imagepicker;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class ImagePickerActivity extends AppCompatActivity {

  private ImageAdapter adapter;
  private FolderAdapter folderAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_picker);
    findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());

    AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.spinner);
    folderAdapter = new FolderAdapter();
    spinner.setAdapter(folderAdapter);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        FolderItem item = (FolderItem) adapterView.getItemAtPosition(i);
        fetchAllImages(item.folderPath);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {}
    });

    GridView listView = (GridView) findViewById(R.id.list);
    listView.setOnItemClickListener((adapterView, view, i, l) -> {
      ImageItem item = (ImageItem) adapterView.getItemAtPosition(i);
      Intent intent = new Intent();
      intent.putExtra("path", item.filePath);
      setResult(RESULT_OK, intent);
      finish();
    });

    adapter = new ImageAdapter();
    listView.setAdapter(adapter);

    new TedPermission(this)
            .setPermissionListener(new PermissionListener() {
              @Override
              public void onPermissionGranted() {
                fetchAllImages("/");
              }

              @Override
              public void onPermissionDenied(ArrayList<String> arrayList) {

              }
            })
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check();
  }


  private void fetchAllImages(String folderPath) {
    adapter.clear();
    String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

    Cursor cursor = getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Images.Media.DATA + " like ? ",
            new String[]{"%" + folderPath + "%"},
            MediaStore.Images.Media.DATE_TAKEN + " desc");

    if (cursor == null || cursor.getCount() == 0) {
      return;
    }

    cursor.moveToFirst();

    int dataColumnIndex = cursor.getColumnIndex(projection[0]);
    int idColumnIndex = cursor.getColumnIndex(projection[1]);
    int folderNameColumnIndex = cursor.getColumnIndex(projection[2]);

    do {
      String filePath = cursor.getString(dataColumnIndex);
      String imageId = cursor.getString(idColumnIndex);

//      ImageItem item = new ImageItem(filePath, thumbnailPath(imageId));
      ImageItem item = new ImageItem(filePath, null);
      adapter.add(item);
      folderAdapter.add(new FolderItem(filePath, cursor.getString(folderNameColumnIndex)));
    } while (cursor.moveToNext());

    cursor.close();
    folderAdapter.notifyDataSetChanged();
    folderAdapter.stopAdd();
  }

  // get thumbnail. but not work on my emulator
  private String thumbnailPath(String imageId) {
    String[] projection = {MediaStore.Images.Thumbnails.DATA};
    ContentResolver contentResolver = getContentResolver();

    Cursor cursor = contentResolver.query(
            MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
            new String[]{imageId},
            null);

    if (cursor == null || cursor.getCount() == 0) {
      MediaStore.Images.Thumbnails.getThumbnail(contentResolver, Long.parseLong(imageId), MediaStore.Images.Thumbnails.MINI_KIND, null);
      return thumbnailPath(imageId);
    }
    else {
      cursor.moveToFirst();
      int thumbnailColumnIndex = cursor.getColumnIndex(projection[0]);
      String thumbnailPath = cursor.getString(thumbnailColumnIndex);
      cursor.close();
      return thumbnailPath;
    }
  }
}
