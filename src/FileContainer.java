import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class FileContainer implements Comparator<FileContainer>, Serializable {

    public String name;
    public ArrayList<String> filePath;
    public ArrayList<String> folderPath;
    public boolean isFile;

    public FileContainer(String name, String path, boolean isFile){
        this.name = name;
        this.filePath = new ArrayList<String>();
        this.folderPath = new ArrayList<String>();
        if(isFile == true){
            this.filePath.add(path);
        }else{
            this.folderPath.add(path);
        }
        this.isFile = isFile;
    }

    public ArrayList<String> getFilePaths(){
        return this.filePath;
    }
    
    public ArrayList<String> getFolderPaths(){
        return this.folderPath;
    }

    public String getFirstFilePath() {
        return this.filePath.get(0);
    }

    public String getFirstFolderPath() {
        return this.folderPath.get(0);
    }

    public void addFilePath(String path){
        this.filePath.add(path);
    }
    
    public void addFolderPath(String path){
        this.folderPath.add(path);
    }

    public String getName() {
        return this.name;
    }

    public boolean getFileStatus(){
        return this.isFile;
    }


    public int compare(FileContainer o1, FileContainer o2) {
        if(o1.name.compareTo(o2.name) < 0){
            return -1;
        }else if(o1.name.compareTo(o2.name) > 0){
            return 1;
        }else if(o1.name.compareTo(o2.name) == 0){
            return 0;
        }
        return 0;
    }
}
