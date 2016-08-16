package kr.susemi99.imagepicker;

import java.io.File;

public class FolderItem {
  public String fullPath;
  public String folderName;
  public String folderPath;
  public int count = 1;

  public FolderItem(String fullPath, String folderName) {
    this.fullPath = fullPath;
    this.folderName = folderName;
    if (fullPath.equals("/")) {
      this.folderPath = fullPath;
    }
    else {
      this.folderPath = new File(fullPath).getParentFile().toString();
    }
  }
}
