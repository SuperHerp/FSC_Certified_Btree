import com.sun.org.apache.xml.internal.utils.StringComparable;

import java.io.File;
import java.util.Stack;

public class FSC{
    File[] root;

    public FSC(File[] root){
        this.root = root;
    }

    public void crawl(BTree bTree){
        Stack<File[]> parents = new Stack<>();
        String lastVisit = "";
        Stack<Integer> lastItter = new Stack<>();
        File[] curPos = this.root;
        int i = 0;
        long timeStart = System.currentTimeMillis();

        System.out.println("Start crawling fs...");
        while(true){
            //System.out.println("Current directory: " + curPos[i].getPath());
            if(curPos[i].toString() == "C:\\Users\\simon\\IdeaProjects\\FSC_Certified_Btree\\src\\FSC.java"){
                int debug= 0;
            }
            if(i == this.root.length-1 && parents.size() == 0 && lastItter.size() == 0){
                if(curPos[i].isFile()){
                    String fileName = curPos[i].getName();
                    FileContainer add = new FileContainer(fileName, curPos[i].getPath());
                    bTree.insert(add);
                    //System.out.println("adding: " + fileName);

                    //add FC to tree
                    //continue;
                    if(i == curPos.length - 1 ){
                        lastVisit = curPos[i].getParentFile().getPath();
                        if(parents.size() == 0){
                            break;
                        }
                        curPos = parents.peek();
                        parents.pop();
                        i = lastItter.peek();
                        lastItter.pop();
                        continue;
                    }
                    i++;
                    continue;
                }else{//is Folder
                    File[] debug = curPos[i].listFiles();
                    if(curPos[i].listFiles() == null){//folder empty
                        if(i == curPos.length-1){
                            lastVisit = curPos[i].getParentFile().getPath();
                            curPos = parents.peek();
                            parents.pop();
                            i = lastItter.peek();
                            lastItter.pop();
                            continue;
                        }
                        i++;
                        continue;
                    }//not empty folder
                    String curVisit = curPos[i].getPath();
                    int debug1 = curPos[i].listFiles().length;
                    if(curVisit.compareTo(lastVisit) != 0 && curPos[i].listFiles().length != 0) {
                        parents.push(curPos);
                        lastItter.push(i);
                        curPos = curPos[i].listFiles();
                        i = 0;
                        continue;
                    }else{
                        if(i == curPos.length-1){
                            if(parents.size() == 0 && i == this.root.length-1 && lastItter.size() == 0){
                                break;
                            }
                            lastVisit = curPos[i].getParentFile().getPath();
                            curPos = parents.peek();
                            parents.pop();
                            i = lastItter.peek();
                            lastItter.pop();
                            continue;
                        }else{
                            i++;
                            continue;
                        }
                    }
                }
                //break;
            }
        //for(int i = 0; i < curPos.length; i++){
            if(curPos[i].isFile()){
                FileContainer add = new FileContainer(curPos[i].getName(), curPos[i].getPath());
                bTree.insert(add);
                String fileName = curPos[i].getName();
                //System.out.println("adding: " + fileName);

                //add FC to tree
                //continue;
                if(i == curPos.length - 1 ){
                    lastVisit = curPos[i].getParentFile().getPath();
                    curPos = parents.peek();
                    parents.pop();
                    i = lastItter.peek();
                    lastItter.pop();
                    continue;
                }
                i++;
                continue;
            }else{//is Folder
                File[] debug = curPos[i].listFiles();
                if(curPos[i].listFiles() == null){//folder empty
                    if(i == curPos.length-1){
                        lastVisit = curPos[i].getParentFile().getPath();
                        curPos = parents.peek();
                        parents.pop();
                        i = lastItter.peek();
                        lastItter.pop();
                        continue;
                    }
                    i++;
                    continue;
                }//not empty folder
                String curVisit = curPos[i].getPath();
                int debug1 = curPos[i].listFiles().length;
                if(curVisit.compareTo(lastVisit) != 0 && curPos[i].listFiles().length != 0) {
                    parents.push(curPos);
                    lastItter.push(i);
                    curPos = curPos[i].listFiles();
                    i = 0;
                    continue;
                }else{
                    if(i == curPos.length-1){
                        if(parents.size() == 0 && i == this.root.length-1 && lastItter.size() == 0){
                            break;
                        }
                        lastVisit = curPos[i].getParentFile().getPath();
                        curPos = parents.peek();
                        parents.pop();
                        i = lastItter.peek();
                        lastItter.pop();
                        continue;
                    }else{
                        i++;
                        continue;
                    }
                }
            }
        }
        long timeEnd = System.currentTimeMillis();
        long timeTaken = (timeEnd-timeStart)/1000;
        System.out.println("Crawled all the way through in: " + timeTaken + " seconds!");

    }

    public static void main(String[] args) {


        File[] entries = File.listRoots();

        FSC test0 = new FSC(entries);
        BTree bTree = new BTree(9);
        test0.crawl(bTree);

        bTree.toObjSer();
        AbstractBTree btree1 = new BTree(2);
        AbstractBTree testTree = BTree.serObjToTree();

        //System.out.println(bTree.toJson());


        boolean test00 = testTree.hasKey("A.txt");
        boolean test1 = testTree.hasKey("B.txt");
        boolean test2 = testTree.hasKey("C.txt");
        boolean test3 = testTree.hasKey("D.txt");
        boolean test4 = testTree.hasKey("E.txt");
        boolean test5 = testTree.hasKey("F.txt");
        boolean test6 = testTree.hasKey("G.txt");
        boolean test7 = testTree.hasKey("H.txt");
        boolean test8 = testTree.hasKey("I.txt");
        boolean test9 = testTree.hasKey("J.txt");

        System.out.println(test00);
        System.out.println(test1);
        System.out.println(test2);
        System.out.println(test3);
        System.out.println(test4);
        System.out.println(test5);
        System.out.println(test6);
        System.out.println(test7);
        System.out.println(test8);
        System.out.println(test9);


        //System.out.println("\n" + test);
        int debug = -1;


    }
}