package kr.susemi99.imagepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class FolderAdapter extends BaseAdapter {
  private ArrayList<FolderItem> items = new ArrayList<>();
  private boolean addStopped = false;

  public FolderAdapter() {
    items.add(new FolderItem("/", "전체"));
  }

  public void add(FolderItem item) {
    if (addStopped) {
      return;
    }

    File itemFile = new File(item.fullPath);

    boolean isContain = false;
    for (FolderItem f : items) {
      if (f.fullPath.equals("/")) {
        continue;
      }

      File folder = new File(f.fullPath);
      isContain = folder.getParentFile().equals(itemFile.getParentFile());
      if (isContain) {
        f.count++;
        break;
      }
    }

    if (!isContain) {
      items.add(item);
    }

    FolderItem all = items.get(0);
    all.count++;
    if (items.size() > 1) {
      all.fullPath = items.get(1).fullPath;
    }
  }

  public void stopAdd() {
    addStopped = true;
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
    FolderItem item = items.get(i);

    TextView textView = (TextView) View.inflate(viewGroup.getContext(), android.R.layout.simple_spinner_dropdown_item, null);
    textView.setText(item.folderName);
    return textView;
  }

  @Override
  public View getDropDownView(int i, View view, ViewGroup viewGroup) {
    int resId = R.layout.list_item_folder;
    view = LayoutInflater.from(viewGroup.getContext()).inflate(resId, null);

    FolderItem item = items.get(i);

    ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
    TextView textTitle = (TextView) view.findViewById(R.id.text_title);
    TextView textCount = (TextView) view.findViewById(R.id.text_count);

    textTitle.setText(item.folderName);
    textCount.setText(String.valueOf(item.count));
    Picasso.with(viewGroup.getContext()).load(new File(item.fullPath)).into(imageView);

    return view;
  }
}
