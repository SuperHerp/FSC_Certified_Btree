import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FSC{
    File[] root;
    StringBuilder fileNames;


    public FSC(File[] root){
        this.root = root;
        this.fileNames = new StringBuilder();
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
            // System.out.println("Current directory: " + curPos[i].getPath());
            if(i == this.root.length-1 && parents.size() == 0 && lastItter.size() == 0){
                if(curPos[i].isFile()){
                    String fileName = curPos[i].getName();
                    if(this.fileNames.indexOf(fileName) == -1){
                        this.fileNames.append(fileName + ";");
                        // System.out.println("filename added: " + this.fileNames);
                    }
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
                String fileName = curPos[i].getName();
                FileContainer add = new FileContainer(fileName, curPos[i].getPath());
                if(this.fileNames.indexOf(fileName) == -1){
                    this.fileNames.append(fileName + ";");
                    // System.out.println("filename added: " + this.fileNames);
                }
                bTree.insert(add);
                // String fileName = curPos[i].getName();
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

    // public String[] sortMatches(String[] matches){
    //     // String[] sortedMat = new String[matches.length];
    //     String carry;
    //     int j;
    //     boolean changedQ = false;
    //     while(!changedQ){
    //         changedQ = false;
    //         for(int i = 0; i < matches.length; i++){
    //             j = i+1;
    //             int cmp2 = matches[i].compareTo(matches[j]);
    //             //matches[i] > matches[j] --> cmp > 0
    //             if(cmp2 > 0){
    //                 carry = matches[j];
    //                 matches[j] = matches[i];
    //                 matches[i] = carry;
    //                 changedQ = true;
    //             }else{
    //                 if(cmp2 < 0){
    //                     //do nothing
    //                 }else{
    //                     //cmp2 == 0 -> ?
    //                     System.out.println("error -> encountered same filename 2 times");
    //                 }
    //             }
    //         }

    //     }
    //     return matches;
    // }

    public static void main(String[] args) {
        //hihi

        AbstractBTree testTree = BTree.serObjToTree();
        Pattern pat;
        Matcher mat;

        while(true){
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println("Search-atrributes:");
            System.out.println("    0.: ''          -> search for strings containing input (e.g: input 'hello' => every file containing 'hello' in its name (daksjhhelloaskjdla.ext gets matched!))");
            System.out.println("    1.: 'ext:'      -> search for extension (e.g: ext:exe => lists all files with '.exe' as extension)");
            System.out.println("    2.: 'direct:'   -> search for this literaly (whole word match -> e.g: direct:ascii.inc => looks for ascii.inc)");

            // Scanner in = new Scanner(System.in);
            System.out.println("Enter search term: ");
            // String toFind = in.toString();

            String inFromCLI = System.console().readLine();
            String regPat;

            if(inFromCLI.contains("direct:")){
                inFromCLI = inFromCLI.replace("direct:", "");

                regPat = inFromCLI;
                System.out.println("regpat: " + regPat);

            }else if(inFromCLI.contains("ext:")){
                inFromCLI = inFromCLI.replace("ext:", "");

                regPat = "([^;]*\\w+." + inFromCLI + ")";
                System.out.println("regpat: " + regPat);
            }else{
                regPat = "[^;]*(" + inFromCLI + ")[^;]+";
                System.out.println("regpat: " + regPat);
            }


            pat = Pattern.compile(regPat, Pattern.CASE_INSENSITIVE);
            mat = pat.matcher(testTree.getFileNames());
            
            String[] matches = mat.results().map(MatchResult::group).toArray(String[]::new);
            Arrays.sort(matches);
            // String toFind = System.console().readLine();
            // in.close();

            String toFind;
            int h = 0;
            for(int j = 0; j < matches.length; j++){

                // toFind = matches[j].substring(0, matches[j].length() - 1);
                toFind = matches[j];
                // System.out.println(toFind);
                boolean foundQ = testTree.hasKey(toFind);
                // System.out.println("Search for '" + toFind + "' returned: " + foundQ);
    
                // testTree.remove(new FileContainer("A.txt", "E:\\Desktop\\FSC TestDir\\A\\A.txt"));
    
                if(foundQ){
                    ArrayList<FileContainer> keys01 = testTree.fcWithKey(toFind).getKeys();
                    for(int i = 0; i < keys01.size(); i++){
                        if(keys01.get(i).name.equals(toFind)){
                            ArrayList<String> paths01 = testTree.fcWithKey(toFind).getKeys().get(i).getPaths();
                            for(int k = 0; k < paths01.size(); k++){
                                System.out.println(h + ". Path: " + paths01.get(k));
                                h++;
                            }
                            break;
                        }
                    }
                }
            }
        }

        //  //File[] entries = File.listRoots()[0].listFiles()[13].listFiles();
        // File[] entries = File.listRoots()[0].listFiles();
        // // File[] entries = File.listRoots();
        // //  File[] entries = File.listRoots()[2].listFiles()[8].listFiles()[60].listFiles();
        // FSC test0 = new FSC(entries);
        // BTree bTree = new BTree(3);
        // test0.crawl(bTree);
        // // System.out.println("filenames:" + test0.fileNames);
        // bTree.setFileNames(test0.fileNames.toString());
        // bTree.toObjSer();
        // AbstractBTree testTree = BTree.serObjToTree();
        // //System.out.println(bTree.toJson());
        // boolean test00 = testTree.hasKey("A.txt");
        // boolean test1 = testTree.hasKey("B.txt");
        // boolean test2 = testTree.hasKey("C.txt");
        // boolean test3 = testTree.hasKey("D.txt");
        // boolean test4 = testTree.hasKey("E.txt");
        // boolean test5 = testTree.hasKey("Arch.exe");
        // boolean test6 = testTree.hasKey("World of Warcraft Launcher.exe");
        // boolean test7 = testTree.hasKey("Am Arsch!.mp3");
        // boolean test8 = testTree.hasKey("cemu_1.15.1.rar");
        // boolean test9 = testTree.hasKey("GOPR0603.JPG");
        // boolean test10 = testTree.hasKey("Loki_S01E01_Glorious Purpose.mp4");
        // //FileContainer test10 = testTree.FCwithKey("A.txt");
        // System.out.println("test00:" + test00);
        // System.out.println("test01:" + test1);
        // System.out.println("test02:" +test2);
        // System.out.println("test03:" +test3);
        // System.out.println("test04:" +test4);
        // System.out.println("test05:" +test5);
        // System.out.println("test06:" +test6);
        // System.out.println("test07:" +test7);
        // System.out.println("test08:" +test8);
        // System.out.println("test09:" +test9);
        // System.out.println("test10:" +test10);
        


        //  testTree.remove(new FileContainer("A.txt", "C:\\Users\\simon\\Desktop\\FSC TestDir\\A.txt"));
 //         //System.out.println("\n" + test);
 //         int debug = -1;


    }
}