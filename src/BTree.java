public class BTree extends AbstractBTree {
    public BTree(int degree) {
        super(degree);
    }

    @Override
    public boolean hasKey(String key) {
        AbstractBTreeNode curNode = this.getRoot();
        if(curNode == null){
            return false;
        }
        return curNode.hasKey(key);
    }

    @Override
    public void insert(FileContainer key) {
        if(this.getRoot() == null){
            this.setRoot(new BTreeNode(this.getDegree(), this));
        }
        this.getRoot().insert(key);
        return;
    }

    @Override
    public String toJson() {
        AbstractBTreeNode curNode = this.getRoot();
        return curNode.toJson();
    }

    @Override
    public void remove(FileContainer key) {
        this.getRoot().remove(key);
    }

    public void toObjSer(){
        AbstractBTreeNode curNode = this.getRoot();
        curNode.toObjSer();
    }

    @Override
    public AbstractBTreeNode fcWithKey(String key) {
        return this.getRoot().fcWithKey(key);
    }


    public static AbstractBTree serObjToTree() {
        //AbstractBTreeNode curNode = this.getRoot();
        return BTreeNode.serObjToTree();
    }

    public static void main(String[] args) {
        int degree = 2;

        BTree bTree0 = new BTree(degree);



    }

}
