import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class FileContainer implements Comparator<FileContainer>, Serializable {

    public String name;
    public ArrayList<String> path;

    public FileContainer(String name, String path){
        this.name = name;
        this.path = new ArrayList<String>();
        this.path.add(path);
    }

    public String getFirstPath() {
        return path.get(0);
    }

    public void addPath(String path){
        this.path.add(path);
    }

    public String getName() {
        return this.name;
    }


    public int compare(FileContainer o1, FileContainer o2) {
        if(o1.name.compareTo(o2.name) < 0){
            return -1;
        }else if(o1.name.compareTo(o2.name) > 0){
            return 1;
        }else if(o1.name.compareTo(o2.name) == 0){

            return 0;
            /*
            if(o1.path.compareTo(o2.path) < 0){
                return -1;
            }else if(o1.name.compareTo(o2.name) < 0){
                return 1;
            }else {
                return 0;
            }
            */
        }
        return 0;
    }
}
