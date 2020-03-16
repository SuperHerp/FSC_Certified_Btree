import junit.framework.JUnit4TestAdapter;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;

public class BTree extends AbstractBTree {
    public BTree(int degree) {
        super(degree);
    }

    @Override
    public boolean hasKey(String key) {
        AbstractBTreeNode curNode = this.getRoot();
        if(curNode == null){
            return false;
        }
        return curNode.hasKey(key);
    }

    @Override
    public void insert(FileContainer key) {
        //TODO
        if(this.getRoot() == null){
            this.setRoot(new BTreeNode(this.getDegree(), this));
        }

        /*
        if(this.getRoot().hasKey(key.name)){
            return;
        }else{
            this.getRoot().insert(key);
        }
        */
        this.getRoot().insert(key);

        return;

    }

    @Override
    public String toJson() {
        AbstractBTreeNode curNode = this.getRoot();
        return curNode.toJson();
    }

    public static void main(String[] args) {
        int degree = 2;

        BTree bTree0 = new BTree(degree);



    }
}
