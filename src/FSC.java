import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FSC{
    File[] root;
    HashSet<String> nameSet;
    static String[] allNames;
    static String[][] mtMatches;


    public FSC(File[] root){
        this.root = root;
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

            /**
             * 
             */
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
                    boolean skip = false;
                    if (folderName == proc){
                        skip = true;
                    }
                    if(skip == false){
                        if(!bTree.hasKey(folderName)){
                            
                            this.nameSet.add(folderName + ";");
                            System.out.println(curPos[i].getPath());
                            FileContainer add = new FileContainer(folderName, curPos[i].getPath(), false);
                            bTree.insert(add);

                        }
                    }

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
            }else{
            
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
                    boolean skip = false;
                    if (folderName == proc){
                        skip = true;
                    }
                    if(skip == false){
                        System.out.println(curPos[i].getPath());
                        if(!bTree.hasKey(folderName)){
                            
                            this.nameSet.add(folderName + ";");
        
                            FileContainer add = new FileContainer(folderName, curPos[i].getPath(), false);
                            bTree.insert(add);

                        }
                    }
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
                System.out.println("Enter search term: ");

                String inFromCLI = System.console().readLine();
                String regPat;

                if(inFromCLI.contains("direct:")){
                    inFromCLI = inFromCLI.replace("direct:", "");
                    inFromCLI = inFromCLI.replace(".", "\\.");
                    
                    regPat = "([^;]*^(" + inFromCLI + "))|([^;]*(" + inFromCLI + "))";
                    System.out.println("regpat: " + regPat);

                }else if(inFromCLI.contains("ext:")){
                    inFromCLI = inFromCLI.replace("ext:", "");

                    regPat = "([^;]*^(\\." + inFromCLI + "))|([^;]*(\\." + inFromCLI + "))";
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
                    e.printStackTrace();
                }

                int h = 0;
                if(mtMatches == null){
                    System.out.println("no matches!");
                }
                
                
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

                ArrayList<String> allPathsAL = new ArrayList<String>(totalLen);
                String toFind;
                for(int idx = 0; idx < sortedMatches.length; idx++){
                    
                    toFind = sortedMatches[idx];

                    if(testTree.hasKey(toFind)){
                        AbstractBTreeNode nodeWithKey = testTree.fcWithKey(toFind);
                        ArrayList<FileContainer> keys01 = nodeWithKey.getKeys();
                        for(int idz = 0; idz < keys01.size(); idz++){
                            if(keys01.get(idz).name.equals(toFind)){
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

        }
        else
        {
            // File[] entries = File.listRoots()[0].listFiles()[0].listFiles();
            // File[] entries = File.listRoots()[0].listFiles();
            File[] entries = File.listRoots();
            FSC fileCrawler = new FSC(entries);
            BTree bTree = new BTree(6);
            fileCrawler.crawl(bTree);

            ArrayList<String> toAdd = new ArrayList<String>();

            Iterator<String> hashIt = fileCrawler.nameSet.iterator();

            int idx =  0;
            while(hashIt.hasNext()){
                toAdd.add(hashIt.next());
            }

            String[] finalStringArr = toAdd.toArray(new String[0]);

            bTree.setFileNames(finalStringArr);
            bTree.toObjSer();

        }
    }
}
