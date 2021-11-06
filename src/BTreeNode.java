import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class BTreeNode extends AbstractBTreeNode{
    public BTreeNode(int degree, AbstractBTree bTree) {
        super(degree, bTree);
    }

    
    public AbstractBTreeNode fcWithKey(String key) {
        AbstractBTreeNode curNode = this;
        while(true) {
            ArrayList<FileContainer> keys = curNode.getKeys();
            int index = -1;
            for(int i = 0; i < keys.size(); i++){
                int cmp = key.compareTo(keys.get(i).getName());
                //int cmp = keys.get(i).name.compareTo(key);
                if(cmp == 0){
                    return curNode;
                }else if(cmp > 0){
                    continue;
                }else if(cmp < 0){
                    index = i;
                    break;
                }
            }
            if(index == -1){
                if(curNode.getChildren().size() != 0){
                    index = curNode.getKeys().size();
                    curNode = curNode.getChildren().get(index);
                    continue;
                }
                return null;
            }
            if(index < curNode.getChildren().size()) {
                curNode = curNode.getChildren().get(index);
            }else {
                return null;
            }
        }
    }

    public FileContainer fc01WithKey(String key) {
        AbstractBTreeNode curNode = this;
        boolean foundF = false;
        while(true) {
            ArrayList<FileContainer> keys = curNode.getKeys();
            int index = -1;
            for(int i = 0; i < keys.size(); i++){
                int cmp = key.compareTo(keys.get(i).getName());
                //int cmp = keys.get(i).name.compareTo(key);
                if(cmp == 0){
                    return curNode.getKeys().get(i);
                    // foundF = true;
                    // return curNode;
                }else if(cmp > 0){
                    continue;
                }else if(cmp < 0){
                    index = i;
                    break;
                }
            }
            
            if(index == -1){
                if(curNode.getChildren().size() != 0){
                    index = curNode.getKeys().size();
                    curNode = curNode.getChildren().get(index);
                    continue;
                }
                return null;
            }
            if(index < curNode.getChildren().size()) {
                curNode = curNode.getChildren().get(index);
            }else {
                return null;
            }
        }

    }

    @Override
    public boolean hasKey(String key) {
        AbstractBTreeNode curNode = this;
        while(true) {
            ArrayList<FileContainer> keys = curNode.getKeys();
            int index = -1;
            for(int i = 0; i < keys.size(); i++){
                int cmp = key.compareTo(keys.get(i).getName());
                //int cmp = keys.get(i).name.compareTo(key);
                if(cmp == 0){
                    return true;
                }else if(cmp > 0){
                    continue;
                }else if(cmp < 0){
                    index = i;
                    break;
                }
            }
            if(index == -1){
                if(curNode.getChildren().size() != 0){
                    index = curNode.getKeys().size();
                    curNode = curNode.getChildren().get(index);
                    continue;
                }
                return false;
            }
            if(index < curNode.getChildren().size()) {
                curNode = curNode.getChildren().get(index);
            }else {
                return false;
            }
        }
    }

    public int compareAToB(AbstractBTreeNode a, AbstractBTreeNode b){
        FileContainer aMax, aMin, bMax, bMin;
        FileContainer cmprtr = new FileContainer("Comparator", "", true);
        int cmp1, cmp2;

        aMax = a.getKeys().get(a.getKeys().size() - 1);
        aMin = a.getKeys().get(0);

        bMax = b.getKeys().get(b.getKeys().size() - 1);
        bMin = b.getKeys().get(0);

        cmp1 = cmprtr.compare(aMax, bMin);
        cmp2 = cmprtr.compare(bMax, aMin);

        if(cmp1 < 0){
            return -1;// A < B
        }else if(cmp1 > 0){ // B < A ?
            if(cmp2 < 0){
                return 1;// B < A
            }else if(cmp2 > 0){
                return -1;// A < B
            }
        }
        return -2;
    }

    public ArrayList<AbstractBTreeNode> quickSortKids(ArrayList<AbstractBTreeNode> aL){
        AbstractBTreeNode swapA, swapB;
        int cmp, indA, indB;

        while(true){
            boolean noChanges = true;

            for (int i = 0; i < aL.size(); i ++){
                swapA = aL.get(i);
                for(int j = i+1; j < aL.size(); j++){
                    swapB = aL.get(j);

                    cmp = this.compareAToB(swapA, swapB);

                    switch (cmp){
                        case(0):
                            break;
                        case(-1)://A < B
                            indA = aL.indexOf(swapA);
                            indB = aL.indexOf(swapB);

                            if(indA > indB){
                                aL.remove(indA);
                                aL.add(indA, swapB);

                                aL.remove(indB);
                                aL.add(indB, swapA);
                                noChanges = false;
                            }
                            break;
                        case(1)://B < A
                            indA = aL.indexOf(swapA);
                            indB = aL.indexOf(swapB);

                            if(indB > indA){
                                aL.remove(indA);
                                aL.add(indA, swapB);

                                aL.remove(indB);
                                aL.add(indB, swapA);
                                noChanges = false;
                            }
                            break;
                        case(-2):
                            System.out.println("critical error");
                    }

                }
            }
            if (noChanges == true){
                break;
            }

        }
        return aL;
    }

    @Override
    public ArrayList<FileContainer> quickSortAR(ArrayList<FileContainer> aL){
        boolean noChanges;
        FileContainer cmprtr = new FileContainer("Comparator", "", true);
        FileContainer swapA, swapB;
        int cmp, indA, indB;

        while(true){
            noChanges = true;
            for(int i = 0; i < aL.size(); i++){
                swapA = aL.get(i);
                for(int j = i+1; j < aL.size(); j++){
                    swapB = aL.get(j);

                    cmp = cmprtr.compare(swapA, swapB);

                    switch (cmp){
                        case(0):
                            break;
                        case(-1)://A < B
                            indA = aL.indexOf(swapA);
                            indB = aL.indexOf(swapB);

                            if(indA > indB){
                                aL.remove(indA);
                                aL.add(indA, swapB);

                                aL.remove(indB);
                                aL.add(indB, swapA);
                                noChanges = false;
                            }
                            break;
                        case(1)://B < A
                            indA = aL.indexOf(swapA);
                            indB = aL.indexOf(swapB);

                            if(indB > indA){
                                aL.remove(indA);
                                aL.add(indA, swapB);

                                aL.remove(indB);
                                aL.add(indB, swapA);
                                noChanges = false;
                            }
                            break;
                    }
                }
            }
            if(noChanges == true){
                break;
            }
        }
        return aL;
    }

    @Override
    public OverflowNode insert(FileContainer key) {
        /**
         * find leaf in wich the key should be added(remember last parent ALWAYS)
         * add key to keyList
         * check keylist size
         * bigger than 2*curnode.Degree?(check)
         *      yes => curnode needs to be split
         *          call .split() (ovfl = curnode.split)
         *          if ovfl != null => insert ovfl in parent and start check again
         *          if ovfl == null => return ovfl
         *
         *      no => return null (ovfl = null)
         */

        //AbstractBTreeNode curNode = new BTreeNode(this.getDegree());
        AbstractBTreeNode curNode = this;
        FileContainer cmprtr = new FileContainer("Comparator", "", true);
        Stack<AbstractBTreeNode> parents = new Stack<AbstractBTreeNode>();
        ArrayList<FileContainer> keys;
        ArrayList<AbstractBTreeNode> children;

        if(curNode.get_bTree().getRoot().hasKey(key.getName())){
            //return null;
        }

        while (true) { //find leaf on wich to insert and create stack containing parents
            int i;
            for (i = 0; i < curNode.getKeys().size(); i++) {

                int cmp = cmprtr.compare(curNode.getKeys().get(i), key);
                //int cmp = curNode.getKeys().get(i).name.compareTo(key.name);
                if(cmp == 0){
                    if(key.getFileStatus()){
                        curNode.getKeys().get(i).addFilePath(key.getFirstFilePath());
                    }else{
                        curNode.getKeys().get(i).addFolderPath(key.getFirstFolderPath());
                    }
                    return null;
                    //return true;
                }else if(cmp < 0){
                    continue;
                }else if(cmp > 0){
                    break;
                }

            }

            if(curNode.getChildren().size() == 0){
                break;
            }
            if (i >= curNode.getChildren().size()) {
                break;
                //return parents;
            } else {
                parents.push(curNode);
                curNode = curNode.getChildren().get(i);
                curNode.parent = parents.peek();
                continue;
            }
        }

        keys = curNode.getKeys();
        keys.add(key);
        keys = curNode.quickSortAR(keys);

        if(curNode.getKeys().size() <= curNode.getDegree() * 2 ){ //no need to split
            return null;
        }

        OverflowNode ovfl = curNode.split();
        OverflowNode ovflBAK = ovfl; //copy just to make sure

        while (true){
            if(ovfl == null){
                break;
            }
            if(parents.size() == 0){
                //TODO
                //root changes...
                AbstractBTreeNode newRoot = new BTreeNode(curNode.getDegree(), curNode.get_bTree());

                newRoot.addKey(ovfl.getKey());
                newRoot.addChild(curNode);
                curNode.parent = newRoot;
                ovfl.getRightChild().parent = newRoot;
                newRoot.addChild(ovfl.getRightChild());


                curNode.get_bTree().setRoot(newRoot);

                break;
            }
            curNode = parents.peek();
            parents.pop();

            key = ovfl.getKey();
            AbstractBTreeNode ovflRight = ovfl.getRightChild();
            ovflRight.parent = curNode;

            keys = curNode.getKeys();
            keys.add(key);
            keys = curNode.quickSortAR(keys);

            children = curNode.getChildren();
            children.add(ovflRight);
            children = curNode.quickSortKids(children);

            if(curNode.getKeys().size() > curNode.getDegree() * 2){
                ovfl = curNode.split();
                continue;
            }else{
                break;
            }
        }
        return ovflBAK;
    }



    @Override
    public OverflowNode split() {
        //TODO
        AbstractBTreeNode curNode = this;
        AbstractBTreeNode ovflRight = new BTreeNode(this.getDegree(), curNode.get_bTree());

        int j = curNode.getDegree()+1;
        for(int i = 0; i < curNode.getDegree(); i++){
            ovflRight.addKey(curNode.getKeys().get(j));
            curNode.getKeys().remove(j);

            if(j >= curNode.getChildren().size()){
                continue;
            }else {
                ovflRight.addChild(curNode.getChildren().get(j));
                if(i == getDegree()-1){
                    ovflRight.addChild(curNode.getChildren().get(j+1));
                    curNode.getChildren().remove(j+1);
                }
                curNode.getChildren().remove(j);
                continue;
            }
        }

        OverflowNode ovfl = new OverflowNode(curNode.getKeys().get(curNode.getDegree()), ovflRight);
        curNode.getKeys().remove(j-1);

        return ovfl;
    }

    // { [9,22],[{[2,8]},{[17,21]},{[23,24,25]}] }

    @Override
    public String toJson() {

        /**
         * start:
         *
         * if curnode is unechecked =>add curNode values;
         * else if curnode is checked => doNothing(dont add curnode values)
         *
         * if child exists => enter first unchecked child
         * goto start;
         *
         * else if cur is leaf(got leaf?)? || cur children all checked =>
         *      check leaf
         *
         *      go to parent of curNode
         * goto start
         */

        StringBuilder json = new StringBuilder();
        AbstractBTreeNode curNode = this;
        Stack<AbstractBTreeNode> parents = new Stack<>();
        Stack<AbstractBTreeNode> lastChild = new Stack<>();

        while(true){
            // curNode is root => if all children visited? => break;
            if(parents.size() == 0 && lastChild.containsAll(curNode.getChildren())){
                break;
            }
            if(!parents.contains(curNode)){ //curNode unchecked? => add key to json
                json.append("{keys:[");
                parents.push(curNode);
                for(int i = 0; i < curNode.getKeys().size(); i++){
                    if(i < curNode.getKeys().size()-1){ //last key?  no => append  i-key + ","
                        json.append("<" + curNode.getKeys().get(i).getName() + ";" + curNode.getKeys().get(i).filePath + ">" + ",");
                    }else { //last key? yes => append i-key + "]"
                        json.append("<" + curNode.getKeys().get(i).getName() + ";" + curNode.getKeys().get(i).filePath + ">" + "]");
                    }
                }
            }else{ //curnode is checked? => skip adding values
                //nop
            }
            //all keys of curNode were added => looking for unchecked child

            if(curNode.getChildren().size() == 0){
                lastChild.push(curNode);
                if(parents.size() != 0){
                    parents.pop();
                    curNode = parents.peek();
                    json.append("}");
                }else{
                    System.out.println("idk...");
                }
            }else {
                int index = -1;
                for (int i = 0; i < curNode.getChildren().size(); i++) {
                    if (lastChild.contains(curNode.getChildren().get(i))) { //i-child has already been visited => skip kid
                        continue;
                    } else { //i-child has not been visited yet => index = i and break;
                        if(i > 0 && i < curNode.getChildren().size()) {
                            json.append(",");
                            index = i;
                            break;
                        }else{
                            json.append(",children:[");
                            index = i;
                            break;
                        }

                    }
                }
                //index could be -1 => all children of curnode were visited => go to parent of curnode
                if (index == -1) {
                    //reached end of curNode => json.append("]}")
                    json.append("]}");
                    lastChild.push(curNode);
                    if (parents.size() != 0) {
                        parents.pop();
                        if(parents.size() == 0){
                            break;
                        }
                        curNode = parents.peek();
                    }
                    continue;
                }
                // if index != -1 add curNode to parents and set curnode to i-child of curnode and start from beginning
                if (index > -1) {
                    curNode = curNode.getChildren().get(index);
                }
            }

        }
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(new File("out.json")));
            fileWriter.write(json.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public void toObjSer(){
        AbstractBTree bTree = this.get_bTree();
        Deflater defl = new Deflater(Deflater.BEST_COMPRESSION);
        try {
            System.out.println("Saving tree...");
            long start = System.currentTimeMillis();

            FileOutputStream fiOut = new FileOutputStream("bTree.FSC");
            DeflaterOutputStream defOut = new DeflaterOutputStream(fiOut, defl);
            ObjectOutputStream objOut = new ObjectOutputStream(defOut);
            objOut.writeObject(bTree);
            objOut.close();
            defOut.close();
            fiOut.close();

            long time = (System.currentTimeMillis() - start)/1000;
            System.out.println("Saving took: " + time + "seconds.");
        } catch (Throwable e) {
            e.printStackTrace();
            e.printStackTrace();
        }

    }

    @Override
    public void remove(FileContainer key) {
        AbstractBTreeNode rootNode = this;
        AbstractBTreeNode nodeWithKey = rootNode.fcWithKey(key.getName());
        FileContainer fc2del = null;

        int i;
        for(i = 0; i < nodeWithKey.getKeys().size(); i++){
            if(nodeWithKey.getKeys().get(i).getName().compareTo(key.getName()) == 0){
                fc2del = nodeWithKey.getKeys().get(i);
                break;
            }
        }
        if(fc2del == null){
            return;
        }else{
            if(fc2del.filePath.size() <= 1){
                //Node/FC has to be deleted
            }else{
                //Only path has to be removed
                int indx2rm = fc2del.filePath.indexOf(key.getFirstFilePath());
                if(indx2rm != -1){
                    fc2del.filePath.remove(fc2del.filePath.indexOf(key.getFirstFilePath()));
                }
                return;
            }
        }
    }

    public static AbstractBTree serObjToTree(){
        System.out.println("Starting reconstruction...");
        long start = System.currentTimeMillis();
        AbstractBTree bTree;
        try{
            FileInputStream fIn = new FileInputStream("bTree.FSC");
            InflaterInputStream infIn = new InflaterInputStream(fIn);
            ObjectInputStream objIn = new ObjectInputStream(infIn);
            bTree = (AbstractBTree) objIn.readObject();
            objIn.close();
            infIn.close();
            fIn.close();
            long time = (System.currentTimeMillis() - start)/1000;
            System.out.println("Reconstruction took: " + time +" seconds." );

            return bTree;

        }catch (Throwable t){
            t.printStackTrace();
        }
        int a = 0;
        return null;
    }

    public static void main(String[] args) {

    }
}
