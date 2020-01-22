import javax.swing.plaf.IconUIResource;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Comparator;

public class BTreeNode extends AbstractBTreeNode{
    public BTreeNode(int degree) {
        super(degree);
    }

    @Override
    public boolean hasKey(int key) {
        AbstractBTreeNode curNode = this;

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


    @Override
    public OverflowNode insert(int key) {
        if(this.hasKey(key) == true){
            return null;
        }else{
            Comparator<Integer> cmp = Comparator.naturalOrder();
            ArrayList<AbstractBTreeNode> parents = find_Leaf_and_Parents(this, key);
            AbstractBTreeNode curNode = parents.get(parents.size()-1);
            AbstractBTreeNode ovflRight = null;
            while(true) {
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

    @Override
    public String toJson() {
        return null;
    }

    public static void main(String[] args) {
        ArrayList<Integer> test = new ArrayList<>();
        test.add(0);
        test.add(2);
        test.add(3);
        test.add(4);

        test.add(1, 1);


        int degree = 2;
        /*
        AbstractBTreeNode test01 = new BTreeNode(degree);
        ArrayList test02 = test01.getChildren();
        System.out.println("main: " + test02.hashCode());
        */

        AbstractBTreeNode test03 = new BTreeNode(degree);
        test03.addChild(new BTreeNode(degree));
        ArrayList test04 = test03.getChildren();
        //System.out.println("main: " + test04.hashCode());
        test04.remove(0);
    }
}
