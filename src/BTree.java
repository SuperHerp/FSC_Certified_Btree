import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;

public class BTree extends AbstractBTree {
    public BTree(int degree) {
        super(degree);
        //this.setRoot(new BTreeNode(degree));
    }

    @Override
    public boolean hasKey(int key) {
        AbstractBTreeNode curNode = this.getRoot();
        if(curNode == null){
            return false;
        }
        return curNode.hasKey(key);
    }

    @Override
    public void insert(int key) {
        if(this.hasKey(key)){
            return;
        }
        System.out.println("BTree-----------------------------------------------------------------------");
        if(this.getRoot() == null){
            this.setRoot(new BTreeNode(this.getDegree()));
        }
        //System.out.println("entered BTree insert");
        AbstractBTreeNode curNode = this.getRoot();
        //ArrayList<AbstractBTreeNode> parents = BTreeNode.find_Leaf_and_Parents(curNode, key);
        Comparator<Integer> cmp = Comparator.naturalOrder();
        Stack<AbstractBTreeNode> parentStack = new Stack<>();

        AbstractBTreeNode curNodeCP = curNode;
        while(true){
            int i;
            for(i = 0; i < curNodeCP.getKeys().size(); i++){
                if(curNodeCP.getKeys().get(i) > key){
                    break;
                }
            }
            parentStack.push(curNodeCP);
            if (i >= curNodeCP.getChildren().size()){
                break;
            }else{
                curNodeCP = curNodeCP.getChildren().get(i);
            }
        }
        curNode = curNodeCP;
        OverflowNode ovfl = curNode.insert(key);
        while(true) {
            if(ovfl == null){
                return;
            }else{
                parentStack.pop();
                if(parentStack.size() < 1){
                    if(parentStack.size() == 0) {
                        //System.out.println("enterd rerooting");
                        AbstractBTreeNode newRoot = new BTreeNode(curNode.getDegree());
                        newRoot.addKey(ovfl.getKey());
                        newRoot.addChild(curNode);
                        newRoot.addChild(ovfl.getRightChild());
                        this.setRoot(newRoot);
                        return;
                    }
                }else {
                    curNode = parentStack.peek();
                    ArrayList<Integer> keys = curNode.getKeys();
                    ArrayList<AbstractBTreeNode> children = curNode.getChildren();

                    key = ovfl.getKey();

                    keys.add(key);
                    keys.sort(cmp);

                    children.add(ovfl.getRightChild());
                    BTreeNode.sortAL(children);
                    /*
                    int keyDex = keys.indexOf(key)+1;

                    if(keyDex >= keys.size()){
                        children.add(ovfl.getRightChild());
                    }else {
                        children.add(keyDex, ovfl.getRightChild());
                    }
                    */

                    if (keys.size() > 2 * curNode.getDegree()) {

                        ovfl = curNode.split();
                        continue;
                        //System.out.println("enterd rerooting2");
                        /*
                        AbstractBTreeNode newRoot = new BTreeNode(curNode.getDegree());
                        newRoot.addKey(ovfl.getKey());
                        newRoot.addChild(curNode);
                        newRoot.addChild(ovfl.getRightChild());
                        this.setRoot(newRoot);
                        return;
                        */
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String toJson() {
        AbstractBTreeNode curNode = this.getRoot();
        return curNode.toJson();
    }

    public static void main(String[] args) {
        Random rand = new Random();
        int rnd;

        BTree bTree0 = new BTree(2);

        ArrayList<Integer> randomInts = new ArrayList<>();

        //bTree0.insert(-100);

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

        bTree0.hasKey(23);

        bTree0.toJson();

        int wcount = 0;
        while(bTree0.hasKey(15) && wcount < 1001){
            rnd = rand.nextInt(500);
            randomInts.add(rnd);
            bTree0.insert(rnd);
            System.out.println("last insertion: " + rnd);
            wcount++;
        }

        System.out.println("wcount: " + wcount);
        int debug = -1;
    }
}
