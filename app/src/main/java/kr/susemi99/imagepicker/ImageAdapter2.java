package kr.susemi99.imagepicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter2 extends RecyclerView.Adapter {
  private ArrayList<ImageItem> items = new ArrayList<>();
  private Context context;

  public void add(ImageItem item) {
    items.add(item);
  }

  public void clear() {
    items = new ArrayList<>();
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    int resId = R.layout.list_item_image;
    context = parent.getContext();
    View v = LayoutInflater.from(parent.getContext()).inflate(resId, null, false);
    return new ImageViewHolder(v);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ImageItem item = items.get(position);
    Picasso.with(context).load(new File(item.filePath)).resize(100, 100).centerInside().into(((ImageViewHolder) holder).imageView);
//    ((ImageViewHolder) holder).imageView.setImageURI(Uri.parse(item.thumbnailPath));
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  private class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public ImageViewHolder(View view) {
      super(view);
      imageView = (ImageView) view.findViewById(R.id.image_view);
    }
  }
}
