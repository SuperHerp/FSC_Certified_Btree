import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FSC{
    File[] root;
    StringBuilder fileNames;
    HashSet<String> allNames;

    static String[][] mtMatches;


    public FSC(File[] root){
        this.root = root;
        this.fileNames = new StringBuilder();
        this.allNames = new HashSet<String>(500000);
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
            // System.out.println("elapsed time since start: " + ((System.currentTimeMillis()-timeStart)/1000) + " sec");
            // System.out.println("Current directory: " + curPos[i].getPath());
            if(i == this.root.length-1 && parents.size() == 0 && lastItter.size() == 0){
                if(curPos[i].isFile()){
                    String fileName = curPos[i].getName();
                    this.allNames.add(fileName + ';');
                    // if(this.fileNames.indexOf(fileName) == -1){
                    //     this.fileNames.append(fileName + ";");
                    //     // System.out.println("filename added: " + this.fileNames);
                    // }
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
                this.allNames.add(fileName + ';');
                // if(this.fileNames.indexOf(fileName) == -1){
                //     this.fileNames.append(fileName + ";");
                //     // System.out.println("filename added: " + this.fileNames);
                // }
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



    static class mtRegWork implements Runnable{
        int id;
        String rexpression;
        String toMatchOn;
        public mtRegWork(int id, String rexpression, String toMatchOn){
            this.id = id;
            this.rexpression = rexpression;
            this.toMatchOn = toMatchOn;
        }
        @Override
        public void run() {
            Pattern pat;
            Matcher mat;
            pat = Pattern.compile(this.rexpression, Pattern.CASE_INSENSITIVE);
            mat = pat.matcher(toMatchOn);
            mtMatches[this.id] = mat.results().map(MatchResult::group).toArray(String[]::new);
        }

    } 
    public static void main(String[] args) {
        //hihi

        AbstractBTree testTree = BTree.serObjToTree();
        Pattern pat;
        Matcher mat;
        
        mtMatches = new String[4][];

        
        
        
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
            if(inFromCLI.equals("")){
                System.out.println("cant search for nothing!");
                continue;
            }
            
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
            
            ExecutorService bees = Executors.newFixedThreadPool(4);
            for(int i  = 0; i < 4; i++){
                bees.execute(new mtRegWork(i, regPat, testTree.getFileNames()[i]));
            }

            bees.shutdown();

            try {
                bees.awaitTermination(10, TimeUnit.MINUTES);
            } catch (Exception e) {
                System.out.println("smth went wrong");
            }



            // pat = Pattern.compile(regPat, Pattern.CASE_INSENSITIVE);
            // mat = pat.matcher(testTree.getFileNames());
            
            // String[] matches = mat.results().map(MatchResult::group).toArray(String[]::new);
            // Arrays.sort(matches);
            // String toFind = System.console().readLine();
            // in.close();

            String toFind;
            int h = 0;
            for(int idx = 0; idx < 4; idx++){
                
                if(mtMatches[idx] == null){
                    continue;
                }
                Arrays.sort(mtMatches[idx]);

                for(int j = 0; j < mtMatches[idx].length; j++){

                    // toFind = matches[j].substring(0, matches[j].length() - 1);
                    toFind = mtMatches[idx][j];
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
        }

        // //File[] entries = File.listRoots()[0].listFiles()[13].listFiles();
        // File[] entries = File.listRoots()[0].listFiles();
        // // File[] entries = File.listRoots();
        // //  File[] entries = File.listRoots()[2].listFiles()[8].listFiles()[60].listFiles();
        // FSC test0 = new FSC(entries);
        // BTree bTree = new BTree(3);
        // test0.crawl(bTree);

        // String fileNameSuperString = test0.fileNames.toString();
        // StringBuilder[] toAdd = new StringBuilder[4];
        // String tmp;

        // for(int i = 0; i < 4; i++){
        //     toAdd[i] = new StringBuilder();
        // }

        // // int idx = 0;
        // // int idy = 0;
        // // int idz = 0;
        // // while(idx < fileNameSuperString.length()){
        // //     idx++;
        // //     while(fileNameSuperString.charAt(idx) != ';'){
                
        // //         idx++;
        // //     }
        // //     fileNameSuperString.substring(idy, idx);
        // //     toAdd[idz].append(fileNameSuperString.substring(idy, idx));
        // //     idz = (idz+1 == 4)?(0):(idz+1);
        // //     idy = idx+1;

        // // }

        // // String[] fuckMe = new String[4];
        // // fuckMe[0] = toAdd[0].toString();
        // // fuckMe[1] = toAdd[1].toString();
        // // fuckMe[2] = toAdd[2].toString();
        // // fuckMe[3] = toAdd[3].toString();

        // int idx = 0;
        // Iterator<String> hashIter = test0.allNames.iterator();
        // while(hashIter.hasNext()){
        //     toAdd[idx].append(hashIter.next());
        //     idx = (idx+1 == 4)?(0):(idx+1);
        // }
        // String[] fuckMe = new String[4];
        // fuckMe[0] = toAdd[0].toString();
        // fuckMe[1] = toAdd[1].toString();
        // fuckMe[2] = toAdd[2].toString();
        // fuckMe[3] = toAdd[3].toString();

        // // System.out.println("filenames:" + test0.fileNames);
        // bTree.setFileNames(fuckMe);
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