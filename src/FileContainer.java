import java.io.File;

public class FileContainer implements Comparable{

    public String name;
    public String path;

    public FileContainer(String name, String path){
        this.name = name;
        this.path = path;
    }

    @Override
    public int compareTo(Object o) {
        //TODO
        return 0;
    }
}
