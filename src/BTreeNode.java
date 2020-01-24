import sun.jvm.hotspot.debugger.MachineDescriptionPPC;

import javax.swing.plaf.IconUIResource;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;

public class BTreeNode extends AbstractBTreeNode{
    public BTreeNode(int degree) {
        super(degree);
    }

    @Override
    public boolean hasKey(int key) {
        AbstractBTreeNode curNode = this;

        while(true) {
            if(curNode.getKeys().contains(key)) {
                return true;
            }else {
                int cmp;
                int i;
                for (i = 0; i < curNode.getKeys().size(); i++) {
                    cmp = key - curNode.getKeys().get(i);
                    if(cmp < 0) {
                        break;
                    }
                }
                if(i < curNode.getChildren().size()) {
                    curNode = curNode.getChildren().get(i);
                }else {
                    return false;
                }
            }
        }


        /**
        while(true){
            int keyListSize = curNode.getKeys().size();
            Boolean isBigger = false;
            int i;
            for(i = 0; i < keyListSize; i++){
                System.out.println("i: " + i);
                if(key < curNode.getKeys().get(i)){
                    //index = i;
                }else if(key > curNode.getKeys().get(i)){
                    isBigger = true;
                    break;
                }else if(key == curNode.getKeys().get(i)){
                    return true;
                }
            }

            int debug02 = 5;

            if(isBigger){
                if(curNode.getChildren().size() > i+1){
                    curNode = curNode.getChildren().get(i+1);
                }else{
                    break;
                }
            }else{
                if(curNode.getChildren().size() > i){
                    curNode = curNode.getChildren().get(i);
                }else{
                    break;
                }
            }
         */
            /*
            if(curNode.getChildren().size() > index){
                curNode = curNode.getChildren().get(index);
                continue;
            }else{
                return false;
            }
            */


        //return false;
/**
        while(true){
            int i;
            for(i = 0; i < curNode.getKeys().size(); i++) {
                if (curNode.getKeys().get(i) >= key) {
                    if (curNode.getKeys().get(i) == key) {
                        return true;
                    }else{
                        break;
                    }
                }else{
                    //break;
                }
            }
            try {
                curNode = curNode.getChildren().get(i);
            }catch (Throwable t){
                return false;
            }
        }

 */

        //return false;
    }

    public static ArrayList<AbstractBTreeNode> find_Leaf_and_Parents(AbstractBTreeNode cur, int key) {
        AbstractBTreeNode curNode = cur;
        ArrayList<AbstractBTreeNode> parents = new ArrayList<AbstractBTreeNode>();
        while(true){
            int i;
            for(i = 0; i < curNode.getKeys().size(); i++){
                if(curNode.getKeys().get(i) > key){
                    break;
                }
            }
            parents.add(curNode);
            if(i >= curNode.getChildren().size()){
                return parents;
            }else {
                curNode = curNode.getChildren().get(i);
                continue;
            }
            /*
            try {
                parents.add(curNode);
                curNode = curNode.getChildren().get(i);
            }catch (Throwable exceptionIOOB){
                return parents;
            }
            */
        }
    }

    public static ArrayList<AbstractBTreeNode> sortAL(ArrayList<AbstractBTreeNode> toSort){
        AbstractBTreeNode swapA;
        AbstractBTreeNode swapB;
        int arrSize = toSort.size();
        for(int i = 0; i < arrSize; i++){
            swapA = toSort.get(i);
            for(int j = i+1; j < arrSize; j++){
                swapB = toSort.get(j);

                int keyMinA = swapA.getKeys().get(0);
                int keyMaxA = swapA.getKeys().get(swapA.getKeys().size()-1);

                int keyMinB = swapB.getKeys().get(0);
                int keyMaxB = swapB.getKeys().get(swapB.getKeys().size()-1);

                if(keyMaxA > keyMinB){
                    //A > b
                    int indexA = toSort.indexOf(swapA);
                    int indexB = toSort.indexOf(swapB);

                    if(indexA < indexB){
                        toSort.remove(indexA);
                        toSort.add(indexA, swapB);

                        toSort.remove(indexB);
                        toSort.add(indexB, swapA);
                    }
                }
                if(keyMaxB > keyMinA) {
                    //B > A
                    int indexA = toSort.indexOf(swapA);
                    int indexB = toSort.indexOf(swapB);

                    if (indexB < indexA) {

                        toSort.remove(indexA);
                        toSort.remove(indexB);

                        toSort.add(indexB, swapA);
                        toSort.add(indexA, swapB);
                    }
                }
            }
        }
        return toSort;
    }

    @Override
    public OverflowNode insert(int key) {
        System.out.println("Trying to insert key: " + key + " ...");
        if(this.hasKey(key) == true){
            System.out.println("Tried to insert but key: " + key + " was allready added to this tree!");
            return null;
        }else{
            System.out.println("Key: " + key + " can be added!");
            Comparator<Integer> cmp = Comparator.naturalOrder();
            ArrayList<AbstractBTreeNode> parents = find_Leaf_and_Parents(this, key);
            AbstractBTreeNode curNode = parents.get(parents.size()-1);
            AbstractBTreeNode ovflRight = null;
            while(true) {
                if(curNode.hasKey(key)){
                    return null;
                }
                ArrayList<Integer> keys = curNode.getKeys();
                keys.add(key);
                keys.sort(cmp);
                if (keys.size() > 2*curNode.getDegree()) {
                    OverflowNode ovfl = curNode.split();
                    if (ovfl != null) {
                        return ovfl;
                    }else{
                        System.out.println("Illegal state exception!");
                    }
                }else{
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public OverflowNode split() {
        if(this.getKeys().size() > 2*this.getDegree()) {
            AbstractBTreeNode curNode = this;
            AbstractBTreeNode rch = new BTreeNode(curNode.getDegree());

            int j = curNode.getDegree()+1;
            for(int i = 0; i < curNode.getDegree(); i++){
                rch.addKey(curNode.getKeys().get(j));
                curNode.getKeys().remove(j);

                if(j >= curNode.getChildren().size()){
                    continue;
                }else {
                    rch.addChild(curNode.getChildren().get(j));
                    if(i == getDegree()-1){
                        rch.addChild(curNode.getChildren().get(j+1));
                        curNode.getChildren().remove(j+1);
                    }
                    curNode.getChildren().remove(j);
                    continue;
                }
                /*
                try{
                    rch.addChild(curNode.getChildren().get(j));
                    curNode.getChildren().remove(j);
                }catch (Throwable t){
                    System.out.println("split cought:");
                    t.printStackTrace();
                    continue;
                }
                */
            }

            OverflowNode ovfNode = new OverflowNode(curNode.getKeys().get(curNode.getDegree()), rch);
            curNode.getKeys().remove(j-1);
            return ovfNode;
        }else{
            return null;
        }
    }

    public boolean isLastParent(){
        if(this.getChildren().get(0).getChildren().size() == 0){
            return true;
        }else{
            return false;
        }
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

            if(!parents.contains(curNode)){ //curNode unchecked? => add key to json
                json.append("{[");
                parents.push(curNode);
                for(int i = 0; i < curNode.getKeys().size(); i++){
                    if(i < curNode.getKeys().size()-1){ //last key?  no => append  i-key + ","
                        json.append(curNode.getKeys().get(i) + ",");
                    }else { //last key? yes => append i-key + "]"
                        json.append(curNode.getKeys().get(i) + "]");
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
                            json.append(",[");
                            index = i;
                            break;
                        }

                    }
                }
                //index could be -1 => all children of curnode were visited => go to parent of curnode
                if (index == -1) {
                    //if()
                    //reached end of curNode => json.append("]")
                    json.append("]");
                    lastChild.push(curNode);
                    if (parents.size() != 0) {
                        parents.pop();
                        curNode = parents.peek();
                    }
                    continue;
                }
                // if index != -1 add curNode to parents and set curnode to i-child of curnode and start from beginning
                if (index > -1) {
                    //parents.push(curNode);
                    curNode = curNode.getChildren().get(index);
                }
            }

        }


        //return json.toString();
    }

    public static void main(String[] args) {
        AbstractBTreeNode test = new BTreeNode(3);
        test.addKey(10);
        test.addKey(15);

        AbstractBTreeNode child1 = new BTreeNode(3);
        child1.addKey(0);
        child1.addKey(5);
        AbstractBTreeNode child2 = new BTreeNode(3);
        child2.addKey(11);
        child2.addKey(14);
        AbstractBTreeNode child3 = new BTreeNode(3);
        child3.addKey(16);
        child3.addKey(20);

        test.getChildren();

        test.addChild(child3);
        test.addChild(child1);
        test.addChild(child2);

        sortAL(test.getChildren());

        int debug = -1;
    }
}
