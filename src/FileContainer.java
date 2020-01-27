import java.io.File;
import java.util.Comparator;

public class FileContainer implements Comparator<FileContainer> {

    public String name;
    public String path;

    public FileContainer(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compare(FileContainer o1, FileContainer o2) {
        if(o1.name.compareTo(o2.name) < 0){
            return -1;
        }else if(o1.name.compareTo(o2.name) > 0){
            return 1;
        }else if(o1.name.compareTo(o2.name) == 0){
            if(o1.path.compareTo(o2.path) < 0){
                return -1;
            }else if(o1.name.compareTo(o2.name) < 0){
                return 1;
            }else {
                return 0;
            }
        }
        return 0;
    }
}
