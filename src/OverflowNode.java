public class OverflowNode {
	// the key in the node
	private final FileContainer key;
	// the right successor
	private final AbstractBTreeNode rightChild;

	public OverflowNode(FileContainer key, AbstractBTreeNode rightChild) {
		this.key = key;
		this.rightChild = rightChild;
	}

	public FileContainer getKey() {
		return key;
	}

	public AbstractBTreeNode getRightChild() {
		return rightChild;
	}
}