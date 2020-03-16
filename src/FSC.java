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
            System.out.println("Current directory: " + curPos[i].getPath());
            if(curPos[i].toString() == "C:\\Users\\simon\\IdeaProjects\\FSC_Certified_Btree\\src\\FSC.java"){
                int debug= 0;
            }
            if(i == this.root.length-1 && parents.size() == 0 && lastItter.size() == 0){
                if(curPos[i].isFile()){
                    String fileName = curPos[i].getName();
                    FileContainer add = new FileContainer(fileName, curPos[i].getPath());
                    bTree.insert(add);
                    System.out.println("adding: " + fileName);

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
                System.out.println("adding: " + fileName);

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
        System.out.println("Crawled all the way through in: " + timeTaken + "seconds !");

    }

    public static void main(String[] args) {
        File[] rootEntrys = File.listRoots();
        /*
        File[] entries = rootEntrys[0].listFiles()[13].listFiles()[5].listFiles()[35].listFiles()[11].listFiles();

        File[] entries2 = entries[1].listFiles();
         */
        File[] entries = rootEntrys[0].listFiles()[14].listFiles()[5].listFiles()[26].listFiles()[23].listFiles();
        //File[] entries = rootEntrys[0].listFiles();

        FSC test0 = new FSC(entries);
        BTree bTree = new BTree(2);
        test0.crawl(bTree);

        System.out.println(bTree.toJson());


        boolean test = bTree.hasKey("A.txt");
        boolean test1 = bTree.hasKey("B.txt");
        boolean test2 = bTree.hasKey("C.txt");
        boolean test3 = bTree.hasKey("D.txt");
        boolean test4 = bTree.hasKey("E.txt");
        boolean test5 = bTree.hasKey("F.txt");
        boolean test6 = bTree.hasKey("G.txt");
        boolean test7 = bTree.hasKey("H.txt");
        boolean test8 = bTree.hasKey("I.txt");
        boolean test9 = bTree.hasKey("J.txt");

        /*
        boolean test = bTree.hasKey("primes.sln");
        */
        System.out.println("\n" + test);
        int debug = -1;
    }
}


//2-4