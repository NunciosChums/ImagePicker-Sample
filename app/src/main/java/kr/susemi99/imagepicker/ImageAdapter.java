package kr.susemi99.imagepicker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
  private ArrayList<ImageItem> items = new ArrayList<>();

  public void add(ImageItem item) {
    items.add(item);
  }

  public void clear() {
    items = new ArrayList<>();
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public Object getItem(int i) {
    return items.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    int resId = R.layout.list_item_image;
    view = LayoutInflater.from(viewGroup.getContext()).inflate(resId, null);
    view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 0));

    ImageItem item = items.get(i);
    ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
//    Picasso.with(viewGroup.getContext()).load(new File(item.filePath)).into(imageView);
    imageView.setImageBitmap(getThumbnail(viewGroup.getContext(), item.filePath));

    final View finalView = view;
    view.postDelayed(() -> finalView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, imageView.getWidth())), 100);
    return view;
  }

  private Bitmap getThumbnail(Context context, String path) {
    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=?",
            new String[]{path}, null);

    if (cursor != null && cursor.moveToFirst()) {
      int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
      cursor.close();
      return MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
    }

    cursor.close();
    return null;
  }
}
