/**
 * This class represents a B-tree of given {@link #degree} according to the following rules:
 * <ul>
 *     <li>Each node (except for the root) contains at least {@link #degree} and at most 2x {@link #degree} keys.</li>
 *     <li>The root node contains at least 1 and at most 2x {@link #degree} keys.</li>
 *     <li>The keys stored in each node are kept sorted in ascending order.</li>
 *     <li>Each inner node (i.e. not a leaf) containing k keys also has k+1 children.</li>
 *     <li>All leaves of the tree have the same depth within this tree.</li>
 *     <li>The keys satisfy the following search tree property for B-trees:
 *     <p>If any node x contains the keys s<sub>1</sub>, s<sub>2</sub>, ..., s<sub>k</sub> then
 *     <ul>
 *         <li>all keys of the first child of x are less than s<sub>1</sub></li>
 *         <li>all keys of the i-th child of x (for 1 < i <= k) are greater than s<sub>i-1</sub> and less than s<sub>i</sub></li>
 *         <li>all keys of the (k+1)-th child of x are greater than s<sub>k</sub></li>
 *     </ul>
 *     </p>
 *     </li>
 * </ul>
 *
 * @see <a href="https://de.wikipedia.org/wiki/B-Baum">B-Tree (Wikipedia)</a>
 */
public abstract class AbstractBTree {
	/**
	 * The degree of this BTree. All nodes of this tree can store at most twice as many keys.
	 * The {@link #root} must store at least one key, all other nodes must contain at least {@literal degree} keys.
	 */
	private final int degree;
	/**
	 * The root node of this BTree.
	 */
	private AbstractBTreeNode root;

	/**
	 * Creates a new BTree with given {@link #degree}.
	 *
	 * @param degree the degree of the new BTree
	 */
	public AbstractBTree(int degree) {
		this.degree = degree;
	}

	/**
	 * Returns the {@link #degree} of this BTree
	 *
	 * @return the {@link #degree} of this BTree
	 */
	public final int getDegree() {
		return degree;
	}

	/**
	 * Returns the {@link #root} node of this BTree.
	 *
	 * @return the {@link #root} node of this BTree
	 */
	public final AbstractBTreeNode getRoot() {
		return root;
	}

	/**
	 * Replaces the current {@link #root} node of this BTree with the given argument.
	 *
	 * @param root the new {@link #root} node replacing the old {@link #root} of this BTree
	 */
	public final void setRoot(AbstractBTreeNode root) {
		this.root = root;
	}

	/**
	 * Returns {@literal true} if this BTree contains the specified element.
	 * This method just delegates the search to the {@link #root} node of this BTree.
	 *
	 * @param key element whose presence in this BTree is to be tested
	 * @return {@literal true} if this BTree contains the specified element
	 */
	public abstract boolean hasKey(int key);

	/**
	 * Inserts the given key into this BTree if not already present.
	 * <p>
	 * If this BTree is empty (i.e. {@link #root} is {@literal null}) then a new {@link #root} node with the same {@link #degree} as this BTree is created first.
	 * </p>
	 * <p>
	 * The insertion of the given key is always delegated to the {@link AbstractBTreeNode#insert(int)} method of the {@link #root} node of this BTree.
	 * </p>
	 * If {@link AbstractBTreeNode#insert(int)} returns an {@link OverflowNode} then the {@link #root} of this BTree is replaced by a new root node.
	 * The old {@link #root} node of this BTree becomes the left child of the new root node. The single key and the right child of the new root node are copied over from the {@link OverflowNode}.
	 *
	 * @param key the value to be inserted into this BTree
	 * @see AbstractBTreeNode#insert(int)
	 */
	public abstract void insert(int key);

	/**
	 * Returns a string representation of the {@link #root} node of this BTree in the JSON format or <samp>"{}"</samp> if this BTree is empty (i.e. {@link #root} is {@literal null}).
	 * For a detailed specification of the format to be used here, please refer to the description of the {@link AbstractBTreeNode#toJson()} method.
	 *
	 * @return a string representation of the {@link #root} node of this BTree in the JSON format or <samp>"{}"</samp> if this BTree is empty (i.e. {@link #root} is {@literal null})
	 * @see <a href="https://de.wikipedia.org/wiki/JavaScript_Object_Notation">JavaScript Object Notation (JSON)</a>
	 */
	public abstract String toJson();
}