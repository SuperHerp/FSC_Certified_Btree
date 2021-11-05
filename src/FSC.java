import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

public class FSC{
    File[] root;
    HashSet<String> nameSet;
    // StringBuilder fileNames;
    static String[] allNames;
    static String[][] mtMatches;


    public FSC(File[] root){
        this.root = root;
        // this.fileNames = new StringBuilder();
        this.nameSet = new HashSet<String>(2<<20);
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

                    this.nameSet.add(fileName + ";");

                    FileContainer add = new FileContainer(fileName, curPos[i].getPath(), true);
                    bTree.insert(add);

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

                    String folderName = curPos[i].getName();

                    this.nameSet.add(folderName + ";");

                    FileContainer add = new FileContainer(folderName, curPos[i].getPath(), false);
                    bTree.insert(add);

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
            if(curPos[i].isFile()){
                String fileName = curPos[i].getName();
                FileContainer add = new FileContainer(fileName, curPos[i].getPath(), true);
                
                this.nameSet.add(fileName + ";");
                
                bTree.insert(add);

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

                
                String folderName = curPos[i].getName();

                this.nameSet.add(folderName + ";");

                FileContainer add = new FileContainer(folderName, curPos[i].getPath(), false);
                bTree.insert(add);

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

    

    static class mtRegex implements Runnable{
        int id;
        String rexpression;
        String toMatchOn;
        cSet workLoad;

        public mtRegex(int id, String rexpression, cSet workLoad){
            this.id = id;
            this.rexpression = rexpression;
            this.workLoad = workLoad;
            // this.toMatchOn = toMatchOn;
        }

        @Override
        public void run() {
            int[] work;
            StringBuilder toMatchOnBuild = new StringBuilder();
            int countWork = 0;
            while(true){
                work = workLoad.getWork();
                if(work[4] == -1){
                    break;
                }
                countWork++;
                for(int i = work[0]; i < work[2]; i++){
                    toMatchOnBuild.append(allNames[i]);
                }
            }
            
            toMatchOn = toMatchOnBuild.toString();
            
            Pattern pat;
            Matcher mat;
            pat = Pattern.compile(this.rexpression, Pattern.CASE_INSENSITIVE);
            mat = pat.matcher(this.toMatchOn);
            
            mtMatches[this.id] = mat.results().map(MatchResult::group).toArray(String[]::new);
            Arrays.parallelSort(mtMatches[this.id], getStringCMP());
            System.out.println("thread '" + this.id + "' finished after working out '" + countWork + "' packets");
        }
    }

    public static Comparator<String> getStringCMP(){
        Comparator<String> myCMP = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                // TODO Auto-generated method stub
                if(o1.length() < o2.length()){
                    return -1;
                }else if(o1.length() > o2.length()){
                    return 1;
                }else{
                    //strings are equal in length -> lexicalic compare
                    return o1.compareTo(o2);
                }
            }
            
        };
        
        return myCMP;
        
    }


    public static void main(String[] args) {
        //hihi

        int threads = 8;

        // String[] testSort = {"iplink", "iplinkC", "aiplinkCA", "iplinkB"};
        // Arrays.parallelSort(testSort, getStringCMP());

        // for(int i = 0; i < testSort.length; i++){
        //     System.out.println(testSort[i]);
        // }

        System.out.println("Create new tree (0) or search existing tree (1) ?");

        if(System.console().readLine().equals("1")){

            mtMatches = new String[threads][];

            AbstractBTree testTree = BTree.serObjToTree();

            allNames = testTree.getFileNames();

            boolean folderSearch;
            while(true){
                System.out.println("---------------------------------------------------------------------------------------------");
                System.out.println("---------------------------------------------------------------------------------------------");
                System.out.println("Search-atrributes:");
                System.out.println("    0.: ''          -> search for strings containing input (e.g: input 'hello' => every file containing 'hello' in its name (daksjhhelloaskjdla.ext gets matched!))");
                System.out.println("    1.: 'ext:'      -> search for extension (e.g: ext:exe => lists all files with '.exe' as extension)");
                System.out.println("    2.: 'folder:'   -> search for folders only");
                System.out.println("    3.: 'direct:'   -> search for this literaly (whole word match -> e.g: direct:ascii.inc => looks for ascii.inc)");

                folderSearch = false;
                // Scanner in = new Scanner(System.in);
                System.out.println("Enter search term: ");
                // String toFind = in.toString();

                String inFromCLI = System.console().readLine();
                String regPat;

                if(inFromCLI.contains("direct:")){
                    inFromCLI = inFromCLI.replace("direct:", "");
                    inFromCLI = inFromCLI.replace(".", "\\.");
                    
                    regPat = "([^;]*^(" + inFromCLI + "))|([^;]*(" + inFromCLI + "))";
                    // regPat = inFromCLI;
                    System.out.println("regpat: " + regPat);

                }else if(inFromCLI.contains("ext:")){
                    inFromCLI = inFromCLI.replace("ext:", "");

                    regPat = "([^;]*^(\\." + inFromCLI + "))|([^;]*(\\." + inFromCLI + "))";
                    // regPat = "([^;]*\\w+." + inFromCLI + ")";
                    System.out.println("regpat: " + regPat);

                }else if(inFromCLI.contains("folder:")){
                    inFromCLI = inFromCLI.replace("folder:", "");
                    inFromCLI = inFromCLI.replace(".", "\\.");

                    folderSearch = true;
                    regPat = "([^;]*(" + inFromCLI + ")[^;]*)";
                    System.out.println("regpat: " + regPat);
                }else{
                    //no options for search

                    inFromCLI = inFromCLI.replace(".", "\\.");

                    regPat = "([^;]*^(" + inFromCLI + ")[^;]*)|([^;]*(" + inFromCLI + ")[^;]*)";

                    // regPat = "[^;]*(" + inFromCLI + ")[^;]+";
                    System.out.println("regpat: " + regPat);
                }

                cSet regCSet = new cSet(allNames.length, threads);                
                ExecutorService bees = Executors.newFixedThreadPool(threads);
                for(int i = 0; i < threads; i++){
                    bees.execute(new mtRegex(i, regPat, regCSet));
                }
                bees.shutdown();
                try {
                    bees.awaitTermination(10, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                int h = 0;
                if(mtMatches == null){
                    System.out.println("no matches!");
                }
                
                //-----------------------------------------------------------------------------------------
                
                int totalLen = 0;
                for(int idx = 0; idx < mtMatches.length; idx++){
                    if(mtMatches[idx] == null){
                        continue;
                    }
                    totalLen = totalLen + mtMatches[idx].length;
                }
                
                String[] sortedMatches = new String[totalLen];
                
                int arrIdx = 0;
                for(int idx = 0; idx < mtMatches.length; idx++){
                    if(mtMatches[idx] == null){
                        continue;
                    }
                    for(int idy = 0; idy < mtMatches[idx].length; idy++){
                        sortedMatches[arrIdx] = mtMatches[idx][idy];
                        arrIdx++;
                    }
                }
                Arrays.parallelSort(sortedMatches, getStringCMP());
                
                // for(int i = 0; i < totalLen; i++){
                //     System.out.println(i + ". match is: '"+ sortedMatches[i] + "'");
                // }
                
                ArrayList<String> allPathsAL = new ArrayList<String>(totalLen);
                String toFind;
                for(int idx = 0; idx < sortedMatches.length; idx++){
                    
                    toFind = sortedMatches[idx];
                    // boolean foundQ = testTree.hasKey(toFind);
                    
                    // if(foundQ){
                    if(testTree.hasKey(toFind)){
                        AbstractBTreeNode nodeWithKey = testTree.fcWithKey(toFind);
                        // ArrayList<FileContainer> keys01 = testTree.fcWithKey(toFind).getKeys();
                        ArrayList<FileContainer> keys01 = nodeWithKey.getKeys();
                        for(int idz = 0; idz < keys01.size(); idz++){
                            if(keys01.get(idz).name.equals(toFind)){
                                // ArrayList<String> paths01 = testTree.fcWithKey(toFind).getKeys().get(i).getPaths();
                                ArrayList<String> paths01;
                                if(folderSearch == false){
                                    paths01 = keys01.get(idz).getFilePaths();
                                }else{
                                    paths01 = keys01.get(idz).getFolderPaths();
                                }
                                for(int k = 0; k < paths01.size(); k++){
                                    allPathsAL.add(paths01.get(k));
                                    // System.out.println(h + ". Path: " + paths01.get(k));
                                    h++;
                                }
                                break;
                            }
                        }
                    }
                }
                String[] pathArr = allPathsAL.toArray(new String[0]);
                // Arrays.parallelSort(pathArr);
                for(int k = 0; k < pathArr.length; k++){
                    System.out.println(k + ". Path: " + pathArr[k]);
                }
            }

        }else{

            //File[] entries = File.listRoots()[0].listFiles()[13].listFiles();
            // File[] entries = File.listRoots()[0].listFiles();
            File[] entries = File.listRoots();
            //  File[] entries = File.listRoots()[2].listFiles()[8].listFiles()[60].listFiles();
            FSC fileCrawler = new FSC(entries);
            BTree bTree = new BTree(3);
            fileCrawler.crawl(bTree);

            // String fileNameSuperString = test0.fileNames.toString();


            ArrayList<String> toAdd = new ArrayList<String>();

            // StringBuilder[] toAdd = new StringBuilder[threads];
            // String tmp;

            // for(int i = 0; i < threads; i++){
            //     toAdd[i] = new StringBuilder();
            // }

            Iterator<String> hashIt = fileCrawler.nameSet.iterator();

            int idx =  0;
            while(hashIt.hasNext()){
                toAdd.add(hashIt.next());
                // toAdd[idx].append(hashIt.next());
                // idx = (idx+1 >= threads)?(0):(idx+1);
            }

            String[] finalStringArr = toAdd.toArray(new String[0]);
            // String[] finalStringArr = new String[threads];

            // for(int i = 0; i < finalStringArr.length; i++){
            //     finalStringArr[i] = toAdd[i].toString();
            // }

            bTree.setFileNames(finalStringArr);
            bTree.toObjSer();

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
         
            // testTree.remove(new FileContainer("A.txt", "C:\\Users\\simon\\Desktop\\FSC TestDir\\A.txt"));
            // System.out.println("\n" + test);
            // int debug = -1;
        }
    }
}