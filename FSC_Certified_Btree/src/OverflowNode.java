public class OverflowNode {
	// the key in the node
	private final Integer key;
	// the right successor
	private final AbstractBTreeNode rightChild;

	public OverflowNode(Integer key, AbstractBTreeNode rightChild) {
		this.key = key;
		this.rightChild = rightChild;
	}

	public Integer getKey() {
		return key;
	}

	public AbstractBTreeNode getRightChild() {
		return rightChild;
	}
}