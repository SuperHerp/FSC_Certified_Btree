import org.junit.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

public class BTreeNode extends AbstractBTreeNode{
    public BTreeNode(int degree, AbstractBTree bTree) {
        super(degree, bTree);
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
    }

    //should not use these .... but maybe i will

    /*
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
        }
    }

    public static ArrayList<AbstractBTreeNode> sortAL(ArrayList<AbstractBTreeNode> toSort){
        AbstractBTreeNode swapA;
        AbstractBTreeNode swapB;
        int arrSize = toSort.size();
        if(arrSize <= 1){
            return toSort;
        }
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
    */

    @Override
    public OverflowNode insert(int key) {
        //TODO
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
        Stack<AbstractBTreeNode> parents = new Stack<AbstractBTreeNode>();
        ArrayList<Integer> keys;
        ArrayList<AbstractBTreeNode> children;

        while (true) { //find leaf on wich to insert and create stack containing parents
            int i;
            for (i = 0; i < curNode.getKeys().size(); i++) {
                if (curNode.getKeys().get(i) > key) {
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
                continue;
            }
        }

        curNode.getKeys().add(key);
        curNode.getKeys().sort(Comparator.naturalOrder());

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
                newRoot.addChild(ovfl.getRightChild());

                curNode.get_bTree().setRoot(newRoot);

                break;
            }
            curNode = parents.peek();
            parents.pop();

            key = ovfl.getKey();
            AbstractBTreeNode ovflRight = ovfl.getRightChild();

            curNode.getKeys().add(key);
            curNode.getKeys().sort(Comparator.naturalOrder());
            int index = curNode.getKeys().indexOf(key);
            curNode.getChildren().add(index+1, ovflRight);

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
        return json.toString();
    }

    public static void main(String[] args) {

        int degree = 2;
/*
        AbstractBTreeNode bTree0 = new BTreeNode(degree);

        bTree0.insert(2);
        bTree0.insert(8);
        bTree0.insert(9);
        bTree0.insert(17);

        bTree0.insert(23);
        bTree0.insert(21);
        bTree0.insert(22);
        bTree0.insert(24);

        bTree0.insert(25);
        bTree0.insert(30);
        bTree0.insert(27);
        bTree0.insert(10);

        bTree0.insert(11);
        bTree0.insert(12);
        bTree0.insert(13);
        bTree0.insert(14);

        bTree0.insert(15);

        String expected = "{keys:[15],children:[{keys:[9, 12],children:[{keys:[2,8]},{keys:[10,11]},{keys:[13,14]},]},{keys:[22,25],children:[{keys:[17,21]},{keys:[23,24]},{keys:[27,30]},]}]}";

        Assert.assertEquals("unequal", expected, bTree0.toJson());
*/
        int debug = -1;
    }
}
