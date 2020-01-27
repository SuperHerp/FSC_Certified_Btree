import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;
import org.junit.*;

public class BTreePublicTest {
	// ========== SYSTEM ==========
	protected static final String EX_NAME_hasKey = "hasKey";
	protected static final String EX_NAME_insert = "insert";
	protected static final String EX_NAME_toJson = "toJson";
	//public static StringBuilder stealer = new StringBuilder("{");
	// --------------------

	// ========== TEST DATA ==========
	private static final java.util.Random RND = new java.util.Random(4711_0815_666L);

	// ========== PUBLIC TEST ==========
	// -------------------- Intestines --------------------
	@Test(timeout = 666)
	public void pubTest__BTree_insertion__Intestines__THIS_TEST_IS_VERY_IMPORTANT__IF_IT_FAILS_THEN_YOU_WILL_GET_NO_POINTS_AT_ALL() {
		check__Intestines();
	}

	@Test(timeout = 666)
	public void pubTest__BTree_search__Intestines__THIS_TEST_IS_VERY_IMPORTANT__IF_IT_FAILS_THEN_YOU_WILL_GET_NO_POINTS_AT_ALL() {
		check__Intestines();
	}

	@Test(timeout = 666)
	public void pubTest__BTreeNode_toJson__Intestines__THIS_TEST_IS_VERY_IMPORTANT__IF_IT_FAILS_THEN_YOU_WILL_GET_NO_POINTS_AT_ALL() {
		check__Intestines();
	}

	// -------------------- hasKey --------------------
	@Test(timeout = 16666)
	public void pubTest__hasKey__random() {
		for (int pass = 0; pass < 5; pass++) {
			ArrayList<Integer> keysExp = new ArrayList<>();
			AbstractBTree bt = generateRandomBTree(3 + RND.nextInt(2), 3 + RND.nextInt(2), -RND.nextInt(4711), keysExp, new StringBuilder());
			for (Integer k : keysExp) {
				assertEquals("hasKey has failed.", keysExp.contains(k - 1), bt.hasKey(k - 1));
				assertEquals("hasKey has failed.", keysExp.contains(k), bt.hasKey(k));
				assertEquals("hasKey has failed.", keysExp.contains(k + 1), bt.hasKey(k + 1));
			}
		}
	}

	// -------------------- insert --------------------
	@Test(timeout = 666)
	public void pubTest__insert__random() {
		for (int pass = 0; pass <= 10; pass++) {
			int degreeExp = 3 + RND.nextInt(3);
			ArrayList<Integer> keysExp = new ArrayList<>();
			AbstractBTree bt = new BTree(degreeExp);
			check(bt, degreeExp, keysExp);
			for (int n = 100 + RND.nextInt(42); n >= 0; n--) {
				Integer key = RND.nextInt(4711) - 666;
				if (!keysExp.contains(key)) {
					keysExp.add(key);
				}
				bt.insert(key);
				check(bt, degreeExp, keysExp);
			}
		}
	}

	@Test(timeout = 666)
	public void pubTest__insertWithSplit__random() {
		for (int pass = 0; pass <= 10; pass++) {
			{ // leaf
				int degreeExp = 3 + RND.nextInt(3), key = -666;
				ArrayList<Integer> keysExp = new ArrayList<>();
				AbstractBTreeNode btn = new BTreeNode(degreeExp);
				for (int kIdx = 0; kIdx < 2 * degreeExp; kIdx++) {
					key += 2 + RND.nextInt(42);
					keysExp.add(key);
					OverflowNode o = btn.insert(key);
					assertNull("No splitting yet.", o);
				}
				key = keysExp.get(RND.nextInt(keysExp.size())) + (RND.nextBoolean() ? -1 : +1);
				keysExp.add(key);
				OverflowNode o = btn.insert(key);
				Collections.sort(keysExp);
				assertNotNull("Now we expect splitting!", o);
				assertEquals("Old node has wrong keys.", keysExp.subList(0, degreeExp), btn.getKeys());
				assertEquals("OverflowNode has wrong middle key.", keysExp.get(degreeExp), o.getKey());
				assertEquals("OverflowNode has wrong upper keys.", keysExp.subList(degreeExp + 1, keysExp.size()), o.getRightChild().getKeys());
			}
			{ // non-leaf
				int degreeExp = 3 + RND.nextInt(3), key = -666;
				ArrayList<Integer> keysExpRoot = new ArrayList<>();
				ArrayList<ArrayList<Integer>> keysExps = new ArrayList<>();
				AbstractBTreeNode btnRoot = new BTreeNode(degreeExp);
				AbstractBTreeNode[] btn = new AbstractBTreeNode[2 * degreeExp + 1];
				keysExps.add(new ArrayList<Integer>());
				for (int nIdx = 0; nIdx < btn.length; nIdx++) {
					keysExps.add(new ArrayList<Integer>());
					btn[nIdx] = new BTreeNode(degreeExp);
					btnRoot.addChild(btn[nIdx]);
					for (int kIdx = 0; kIdx < 2 * degreeExp; kIdx++) {
						key += 2 + RND.nextInt(42);
						keysExps.get(nIdx).add(key);
						btn[nIdx].addKey(key);
					}
					if (nIdx < btn.length - 1) {
						key += 2 + RND.nextInt(42);
						keysExpRoot.add(key);
						btnRoot.addKey(key);
					}
				}
				int subTreeIdx = RND.nextInt(btn.length);
				key = keysExps.get(subTreeIdx).get(RND.nextInt(2 * degreeExp)) + (RND.nextBoolean() ? -1 : +1);
				keysExps.get(subTreeIdx).add(key);
				Collections.sort(keysExps.get(subTreeIdx));
				OverflowNode o = btnRoot.insert(key);
				key = keysExps.get(subTreeIdx).get(degreeExp);
				keysExpRoot.add(key);
				Collections.sort(keysExpRoot);
				assertNotNull("Now we expect splitting!", o);
				for (int nIdx = 0; nIdx < btn.length; nIdx++) {
					if (nIdx != subTreeIdx) {
						assertEquals("Old untouched sub-node has wrong keys.", keysExps.get(nIdx), btn[nIdx].getKeys());
					} else {
						assertEquals("Old sub-node (left split) has wrong keys.", keysExps.get(subTreeIdx).subList(0, degreeExp), btn[nIdx].getKeys());
						if (subTreeIdx < degreeExp) { // splitting the last node is a bit critical...
							assertEquals("New sub-node (right split) has wrong keys.", keysExps.get(subTreeIdx).subList(degreeExp + 1, keysExps.get(subTreeIdx).size()), btnRoot.getChildren().get(subTreeIdx + 1).getKeys());
						}
					}
					if (nIdx <= subTreeIdx && nIdx <= degreeExp) {
						assertSame("Old root node has wrong children.", btn[nIdx], btnRoot.getChildren().get(nIdx));
					} else if (nIdx > subTreeIdx && nIdx > degreeExp) {
						assertSame("OverflowNode from root has wrong children.", btn[nIdx], o.getRightChild().getChildren().get(nIdx - degreeExp));
					}
				}
				assertEquals("Old root node has wrong keys.", keysExpRoot.subList(0, degreeExp), btnRoot.getKeys());
				assertEquals("OverflowNode from root has wrong middle key.", keysExpRoot.get(degreeExp), o.getKey());
				assertEquals("OverflowNode from root has wrong upper keys.", keysExpRoot.subList(degreeExp + 1, keysExpRoot.size()), o.getRightChild().getKeys());
			}
		}
	}

	// -------------------- toJson --------------------
	@Test(timeout = 999999)
	public void pubTest__toJson__random() {
		for (int pass = 0; pass < 5; pass++) {
			StringBuilder sbExp = new StringBuilder();
			AbstractBTree bt = generateRandomBTree(3 + RND.nextInt(2), 3 + RND.nextInt(2), -RND.nextInt(4711), new ArrayList<Integer>(), sbExp);
			assertEquals(sbExp.toString(), removeWhiteSpace(bt.toJson()));
		}
	}

	// ========== HELPER ==========
	protected static void check(AbstractBTree bt, int degreeExp, ArrayList<Integer> keysExp) {
		ArrayList<Integer> keysAct = new ArrayList<>();
		assertEquals("BTree has wrong degree.", degreeExp, bt.getDegree());
		if (keysExp == null || keysExp.size() == 0) {
			assertNull("Empty BTree should have root null.", bt.getRoot());
		} else {
			check(bt.getRoot(), true, degreeExp, keysExp, keysAct);
			ArrayList<Integer> keysExpSorted = new ArrayList<>(keysExp);
			Collections.sort(keysExpSorted);
			assertEquals("Non-empty BTree contains wrong values.", keysExpSorted, keysAct);
		}
	}

	private static void check(AbstractBTreeNode btn, boolean isRoot, int degreeExp, ArrayList<Integer> keysExp, ArrayList<Integer> keysAct) {
		assertNotNull("A BTreeNode of a non-empty BTree should never be null or have null as a child.", btn);
		int numOfKey = btn.getKeys().size(), numOfChildren = btn.getChildren().size();
		assertEquals("BTreeNode has wrong degree.", degreeExp, btn.getDegree());
		if (!isRoot) {
			assertTrue("Each BTreeNode (except for the root) of a BTree of degree " + degreeExp + " must have at least " + degreeExp + " and at most " + (2 * degreeExp) + " keys (but this node had " + numOfKey + " keys).", degreeExp <= numOfKey && numOfKey <= 2 * degreeExp);
		} else {
			assertTrue("The root of a BTree of degree " + degreeExp + " must have at least 1 and at most " + (2 * degreeExp) + " keys (but this node had " + numOfKey + " keys).", 1 <= numOfKey && numOfKey <= 2 * degreeExp);
		}
		if (numOfChildren > 0) {
			assertEquals("An inner node with " + numOfKey + " keys must have " + (numOfKey + 1) + " children!", numOfKey + 1, numOfChildren);
		}
		for (int i = 0; i < btn.getKeys().size(); i++) {
			if (numOfChildren > 0) {
				AbstractBTreeNode child = btn.getChildren().get(i);
				check(child, false, degreeExp, keysExp, keysAct);
			}
			Integer key = btn.getKeys().get(i);
			assertTrue("BTreeNode contains unexpected value.", keysExp.contains(key));
			assertFalse("BTreeNode contains duplicate value.", keysAct.contains(key));
			if (keysAct.size() > 0) {
				assertTrue("BTree must keep its keys sorted in ascending order if read by in-order traversal.", keysAct.get(keysAct.size() - 1) < key);
			}
			keysAct.add(key);
		}
		if (numOfChildren > 0) {
			check(btn.getChildren().get(numOfChildren - 1), false, degreeExp, keysExp, keysAct);
		}
	}

	protected static AbstractBTree generateRandomBTree(int degree, int depth, int firstKey, ArrayList<Integer> ks, StringBuilder sb) {
		AbstractBTree bt = new BTree(degree);
		StringBuilder stealy = new StringBuilder("{");
		bt.setRoot(generateRandomBTreeNode(degree, true, depth, firstKey, ks, sb, stealy));
		return bt;
	}

	private static AbstractBTreeNode generateRandomBTreeNode(int degree, boolean isRoot, int depth, int firstKey, ArrayList<Integer> ks, StringBuilder sb, StringBuilder stealer) {
		StringBuilder stealyMcStealson = new StringBuilder();

		AbstractBTreeNode n = new BTreeNode(degree);
		int numOfKeys = (isRoot ? 1 + RND.nextInt(degree) : degree) + RND.nextInt(degree + 1);
		StringBuilder sbKeys = new StringBuilder(), sbChildren = new StringBuilder();
		if (depth > 0) {
			n.addChild(generateRandomBTreeNode(degree, false, depth - 1, firstKey, ks, sbChildren, stealer));
		}
		System.out.println("----stealyMcStealson--------------------------------------------------------------------------------");
		stealyMcStealson.append("{");
		for (int kIdx = 0; kIdx < numOfKeys; kIdx++) {
			int key = (ks.isEmpty() ? firstKey : ks.get(ks.size() - 1)) + 1 + RND.nextInt(5);
			ks.add(key);
			if (kIdx > 0) {
				sbKeys.append(",");
			}
			sbKeys.append(key);
			stealer.append(key + ",");
			n.addKey(key);
			if (depth > 0) {
				sbChildren.append(",");
				n.addChild(generateRandomBTreeNode(degree, false, depth - 1, firstKey, ks, sbChildren, stealer));
			}
		}
		sb.append("{").append("keys:[").append(sbKeys.toString()).append("]");
		if (sbChildren.length() > 0) {
			sb.append(",children:[").append(sbChildren).append("]");
		}
		sb.append("}");

		//stealer.append("}");
		System.out.println(stealyMcStealson);
		System.out.println("----stealyMcStealson--------------------------------------------------------------------------------");
		System.out.println("----StealerDealer--------------------------------------------------------------------------------");
		System.out.println("random Tree with degree: "+n.getDegree() +"\n" + n.toJson() + "\nwas built with these keys:\n " + stealer);
		System.out.println("----StealerDealer--------------------------------------------------------------------------------");

		return n;
	}

	protected static String removeWhiteSpace(String _str) {
		return _str.replaceAll("\\s+", "");
	}

	// ========== HELPER: Intestines ==========
	// @AuD-STUDENT: DO NOT USE REFLECTION IN YOUR OWN SUBMISSION! BITTE KEINE REFLECTION IN DER EIGENEN ABGABE VERWENDEN! => "0 Punkte"!
	private static Class<?>[] getDeclaredClasses(Class<?> clazz) {
		java.util.List<Class<?>> declaredClasses = new java.util.ArrayList<>();
		for (Class<?> c : clazz.getDeclaredClasses()) {
			if (!c.isSynthetic()) {
				declaredClasses.add(c);
			}
		}
		return declaredClasses.toArray(new Class[0]);
	}

	private static Field[] getDeclaredFields(Class<?> clazz) {
		java.util.List<Field> declaredFields = new java.util.ArrayList<>();
		for (Field f : clazz.getDeclaredFields()) {
			if (!f.isSynthetic()) {
				declaredFields.add(f);
			}
		}
		return declaredFields.toArray(new Field[0]);
	}

	private static Constructor<?>[] getDeclaredConstructors(Class<?> clazz) {
		java.util.List<Constructor<?>> declaredConstructors = new java.util.ArrayList<>();
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			if (!c.isSynthetic()) {
				declaredConstructors.add(c);
			}
		}
		return declaredConstructors.toArray(new Constructor[0]);
	}

	private static Method[] getDeclaredMethods(Class<?> clazz) {
		java.util.List<Method> declaredMethods = new java.util.ArrayList<>();
		for (Method m : clazz.getDeclaredMethods()) {
			if (!m.isBridge() && !m.isSynthetic()) {
				declaredMethods.add(m);
			}
		}
		return declaredMethods.toArray(new Method[0]);
	}

	private void check__Intestines() {
		check__Intestines(BTree.class, AbstractBTree.class);
		check__Intestines(BTreeNode.class, AbstractBTreeNode.class);
	}

	private void check__Intestines(Class<?> clazz, Class<?> superClazz) {
		assertFalse(clazz.getName() + " ist faelschlicherweise eine Annotation.", clazz.isAnnotation());
		assertFalse(clazz.getName() + " ist faelschlicherweise ein Enum.", clazz.isEnum());
		assertFalse(clazz.getName() + " ist faelschlicherweise ein Interface.", clazz.isInterface());
		assertFalse(clazz.getName() + " ist faelschlicherweise >abstract<", Modifier.isAbstract(clazz.getModifiers()));
		assertTrue(clazz.getName() + " ist faelschlicherweise nicht >public<", Modifier.isPublic(clazz.getModifiers()));
		assertEquals(clazz.getName() + " hat faelschlicherweise innere Annotationen.", 0, clazz.getDeclaredAnnotations().length);
		assertEquals(clazz.getName() + " hat faelschlicherweise innere Klassen.", 0, getDeclaredClasses(clazz).length);
		assertSame(clazz.getName() + " ist nicht von der richtigen Superklasse abgeleitet.", superClazz, clazz.getSuperclass());
		assertEquals(clazz.getName() + " implementiert falsche Anzahl an Schnittstellen.", 0, clazz.getInterfaces().length);
		Field[] fields = getDeclaredFields(clazz);
		assertEquals(clazz.getName() + " hat falsche Anzahl an Attributen.", 0, fields.length);
		Constructor<?>[] constructors = getDeclaredConstructors(clazz);
		assertEquals(clazz.getName() + " hat falsche Gesamtanzahl an Konstruktoren (inkl. default-Konstruktor).", 1, constructors.length);
		for (Constructor<?> cons : constructors) {
			assertTrue(cons.getName() + " ist faelschlicherweise nicht >public<.", Modifier.isPublic(cons.getModifiers()));
		}
		Method[] methods = getDeclaredMethods(clazz);
		Method[] superMethods = getDeclaredMethods(superClazz);
		HashSet<String> abstractSuperMethods = new HashSet<>();
		for (Method method : superMethods) {
			if (Modifier.isAbstract(method.getModifiers())) {
				abstractSuperMethods.add(method.getName());
			}
		}
		assertEquals(clazz.getName() + " hat falsche Gesamtanzahl an Methoden.", abstractSuperMethods.size(), methods.length);
		for (Method method : methods) {
			if (!abstractSuperMethods.contains(method.getName())) {
				fail(method.getName() + " ist keine Methode, die die Schnittstelle vorschreibt!");
			}
			assertTrue(method.getName() + " ist faelschlicherweise nicht >public<.", Modifier.isPublic(method.getModifiers()));
		}
	}
}