import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * @author Jack Smalligan
 *
 * All of these tests will say something to the effect of:
 *      "Will pass if these methods are functional:"
 * followed by a list of methods. If those methods work, you should expect
 * the test to pass. I included these because nearly all of the tests depend on
 * other methods working that aren't strictly what's being tested,
 * particularly the add method. (You can't really test removes, traversals, etc if you can't properly add (actually
 * you theoretically could, but you'd have to implement the add method correctly in the test itself,
 * which wouldn't be cool with the honor code for this class, so unfortunately you just can't
 * test the others until you have a working add() method...))
 *
 * Note, the converse is not necessarily true; if the test passes, that doesn't
 * guarantee that your method is functional. (I will try to be as comprehensive as possible,
 * but I obviously can't guarantee that every edge case is covered.)
 */
public class SmalliganBSTTest {

    private BST<Integer> tree;

    // guarantee no autoboxing so that we can
    // check reference equality, not value equality in certain tests.
    // autoboxing of small ints can cause unpredictable results
    private Integer ten = new Integer(10);
    private Integer five = new Integer(5);
    private Integer fifteen = new Integer(15);
    private Integer three = new Integer(3);
    private Integer six = new Integer(6);
    private Integer twelve = new Integer(12);
    private Integer eighteen = new Integer(18);

    @Rule
    public Timeout globalTimeout = Timeout.millis(200);

    @Before
    public void setup() {
        tree = new BST<>();
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      default constructor (which should be unchanged anyways)
     */
    public void initTest() {
        assertNull(tree.getRoot());
        assertEquals(0, tree.size());
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      constructor taking a Collection arg, clear
     */
    public void initFromCollectionTest() {
        List<Integer> coll = Arrays.asList(new Integer[] {0, 5, 7, -5, 3, -1, 4});

        // create an indexed collection
        ArrayList<Integer> arrList = new ArrayList<>(coll);

        // create a non-indexed collection
        Queue<Integer> queue = new LinkedBlockingQueue<>(coll);

        tree = new BST<>(arrList);
        testParticularTree();

        tree.clear();

        tree = new BST<>(queue);
        testParticularTree();
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * Will pass if these methods are functional:
     *      constructor taking a Collection arg
     */
    public void initFromCollectionTestNullCollection() {
        ArrayList<Integer> arrList = null;
        tree = new BST<>(arrList);
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * Will pass if these methods are functional:
     *      constructor taking a Collection arg
     */
    public void initFromCollectionTestNullElements() {
        HashSet<Integer> set = new HashSet<>();
        set.add(0);
        set.add(1);
        set.add(null); // null adds to sets are legal
        tree = new BST<>(set);
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add
     */
    public void addTest() {
        tree.add(0);
        assertEquals(1, tree.size());
        assertEquals((Integer) 0, tree.getRoot().getData());
        //   0
        // n   n
        assertLeaf(tree.getRoot());

        tree.add(5);
        assertEquals(2, tree.size());
        //    0
        // n     5
        assertEquals((Integer) 5, tree.getRoot().getRight().getData());
        assertLeaf(tree.getRoot().getRight());
        assertNull(tree.getRoot().getLeft());

        tree.add(3);
        assertEquals(3, tree.size());
        //    0
        // n     5
        //     3   n
        //    n n
        assertEquals((Integer) 5, tree.getRoot().getRight().getData());
        assertEquals((Integer) 3, tree.getRoot().getRight().getLeft().getData());
        assertLeaf(tree.getRoot().getRight().getLeft());
        assertNull(tree.getRoot().getLeft());
        assertNull(tree.getRoot().getRight().getRight());

        tree.add(-5);
        assertEquals(4, tree.size());
        //      0
        // -5       5
        // n n    3   n
        //       n n
        assertEquals((Integer) (-5), tree.getRoot().getLeft().getData());
        assertLeaf(tree.getRoot().getLeft());

        tree.add(-3);
        assertEquals(5, tree.size());
        //      0
        // -5       5
        // n -3   3   n
        //       n n
        assertEquals((Integer) (-3), tree.getRoot().getLeft().getRight().getData());
        assertLeaf(tree.getRoot().getLeft().getRight());
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * Will pass if these methods are functional:
     *      add
     */
    public void addTestIllegalArgumentEmpty() {
        tree.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * Will pass if these methods are functional:
     *      add
     */
    public void addTestIllegalArgumentNotEmpty() {
        tree.add(0);
        tree.add(1);
        tree.add(-1);

        tree.add(null);
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, clear
     */
    public void clearTest() {
        tree.add(0);
        assertEquals((Integer) 0, tree.getRoot().getData());
        tree.add(1);
        assertEquals((Integer) 1, tree.getRoot().getRight().getData());
        tree.add(-1);
        assertEquals((Integer) (-1), tree.getRoot().getLeft().getData());
        assertEquals(3, tree.size());

        tree.clear();
        assertNull(tree.getRoot());
        assertEquals(0, tree.size());
    }


    @Test
    /**
     * Will pass if these methods are functional:
     *      add, clear, height
     */
    public void heightTest() {
        assertEquals(-1, tree.height());

        tree.add(0);
        assertEquals(0, tree.height());

        tree.add(5);
        assertEquals(1, tree.height());

        tree.add(-5);
        assertEquals(1, tree.height());

        tree.add(-3);
        assertEquals(2, tree.height());

        tree.add(3);
        assertEquals(2, tree.height());

        tree.add(7);
        assertEquals(2, tree.height());

        tree.add(10);
        assertEquals(3, tree.height());

        tree.add(-7);
        assertEquals(3, tree.height());

        tree.add(-10);
        assertEquals(3, tree.height());

        tree.add(-20);
        assertEquals(4, tree.height());

        tree.clear();
        assertEquals(-1, tree.height());
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * Will pass if these methods are functional:
     *      add, remove
     */
    public void removeTestIllegalArg() {
        tree.add(0);
        tree.add(1);
        tree.add(-1);

        tree.remove(null);
    }

    @Test(expected = NoSuchElementException.class)
    /**
     * Will pass if these methods are functional:
     *      add, remove
     */
    public void removeTestNoSuchElement() {
        tree.add(0);
        tree.add(1);
        tree.add(-1);

        tree.remove(2);
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, remove
     */
    public void removeTestNoChildren() {

        tree.add(ten);
        tree.add(five);
        tree.add(fifteen);
        tree.add(three);
        tree.add(six);
        tree.add(twelve);
        tree.add(eighteen);
        // level 1:        10
        // level 2:   5        15
        // level 3: 3   6    12  18

        assertEquals(7, tree.size());

        assertEquals((Integer) 6, tree.getRoot().getLeft().getRight().getData());
        Integer toBeFound = new Integer(6);
        Integer found = tree.remove(toBeFound);
        assertEquals(6, tree.size());
        assertEquals(toBeFound, found);
        assertNotSame(toBeFound, found);
        assertSame(six, found);
        assertNull(tree.getRoot().getLeft().getRight());

        assertEquals((Integer) 3, tree.getRoot().getLeft().getLeft().getData());
        toBeFound = new Integer(3);
        found = tree.remove(toBeFound);
        assertEquals(5, tree.size());
        assertEquals(toBeFound, found);
        assertNotSame(toBeFound, found);
        assertSame(three, found);
        assertNull(tree.getRoot().getLeft().getLeft());

        assertEquals((Integer) 5, tree.getRoot().getLeft().getData());
        toBeFound = new Integer(5);
        found = tree.remove(toBeFound);
        assertEquals(4, tree.size());
        assertEquals(toBeFound, found);
        assertNotSame(toBeFound, found);
        assertSame(five, found);
        assertNull(tree.getRoot().getLeft());

        assertEquals((Integer) 10, tree.getRoot().getData());
        assertEquals((Integer) 15, tree.getRoot().getRight().getData());
        assertEquals((Integer) 12, tree.getRoot().getRight().getLeft().getData());
        assertEquals((Integer) 18, tree.getRoot().getRight().getRight().getData());
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, remove
     */
    public void removeTestOneChild() {
        tree.add(ten);
        tree.add(five);
        tree.add(fifteen);
        tree.add(three);
        tree.add(eighteen);
        // level 1:         10
        // level 2:     5        15
        // level 3:   3           18

        assertEquals(5, tree.size());

        assertEquals((Integer) 5, tree.getRoot().getLeft().getData());
        Integer toBeFound = new Integer(5);
        Integer found = tree.remove(toBeFound);
        assertEquals(4, tree.size());
        assertEquals(toBeFound, found);
        assertNotSame(toBeFound, found);
        assertSame(five, found);
        assertEquals((Integer) 3, tree.getRoot().getLeft().getData());
        assertLeaf(tree.getRoot().getLeft());

        assertEquals((Integer) 15, tree.getRoot().getRight().getData());
        toBeFound = new Integer(15);
        found = tree.remove(toBeFound);
        assertEquals(3, tree.size());
        assertEquals(toBeFound, found);
        assertNotSame(toBeFound, found);
        assertSame(fifteen, found);
        assertLeaf(tree.getRoot().getRight());
        assertEquals((Integer) 18, tree.getRoot().getRight().getData());

        assertEquals((Integer) 10, tree.getRoot().getData());
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, remove
     */
    public void removeTestTwoChildren() {
        tree.add(ten);
        tree.add(five);
        tree.add(fifteen);
        tree.add(three);
        tree.add(six);
        tree.add(twelve);
        tree.add(eighteen);
        // level 1:        10
        // level 2:   5        15
        // level 3: 3   6    12  18
        assertEquals(7, tree.size());

        assertEquals((Integer) 5, tree.getRoot().getLeft().getData());
        Integer toBeFound = new Integer(5);
        Integer found = tree.remove(toBeFound);
        assertEquals(toBeFound, found);
        assertNotSame(toBeFound, found);
        assertSame(five, found);
        assertEquals(6, tree.size());

        // tree should now look like this (successor method):
        // level 1:       10
        // level 2:   6        15
        // level 3: 3       12  18
        assertEquals((Integer) 10, tree.getRoot().getData());
        assertEquals((Integer) 6, tree.getRoot().getLeft().getData());
        assertNull(tree.getRoot().getLeft().getRight());
        assertEquals((Integer) 3, tree.getRoot().getLeft().getLeft().getData());
        assertEquals((Integer) 15, tree.getRoot().getRight().getData());
        assertEquals((Integer) 12, tree.getRoot().getRight().getLeft().getData());
        assertEquals((Integer) 18, tree.getRoot().getRight().getRight().getData());

        toBeFound = new Integer(10);
        found = tree.remove(toBeFound);
        assertEquals(toBeFound, found);
        assertNotSame(toBeFound, found);
        assertSame(ten, found);
        assertEquals(5, tree.size());

        // tree should now look like this (successor method):
        // level 1:        12
        // level 2:   6        15
        // level 3: 3            18

        assertEquals(twelve, tree.getRoot().getData());
        assertEquals(six, tree.getRoot().getLeft().getData());
        assertEquals(three, tree.getRoot().getLeft().getLeft().getData());
        assertEquals(fifteen, tree.getRoot().getRight().getData());
        assertNull(tree.getRoot().getRight().getLeft());
        assertEquals(eighteen, tree.getRoot().getRight().getRight().getData());
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, remove
     */
    public void removeAllElementsTest() {
        tree.add(0);
        tree.add(1);
        tree.add(-1);

        assertEquals(3, tree.size());
        assertEquals((Integer) (0), tree.remove(0));
        assertEquals((Integer) 1, tree.getRoot().getData());
        assertEquals((Integer) (-1), tree.remove(-1));
        assertLeaf(tree.getRoot());
        assertEquals((Integer) (1), tree.remove(1));
        assertEquals(0, tree.size());
        assertNull(tree.getRoot());
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, get
     */
    public void getTest() {
        tree.add(ten);
        tree.add(five);
        tree.add(fifteen);
        tree.add(eighteen);
        tree.add(twelve);
        tree.add(six);
        tree.add(three);

        assertEquals((Integer) 10, tree.get(10));
        assertSame(ten, tree.get(10));

        assertEquals((Integer) 5, tree.get(5));
        assertSame(five, tree.get(5));

        assertEquals((Integer) 15, tree.get(15));
        assertSame(fifteen, tree.get(15));

        assertEquals((Integer) 18, tree.get(18));
        assertSame(eighteen, tree.get(18));

        assertEquals((Integer) 3, tree.get(3));
        assertSame(three, tree.get(3));

        assertEquals((Integer) 6, tree.get(6));
        assertSame(six, tree.get(6));

        assertEquals((Integer) 12, tree.get(12));
        assertSame(twelve, tree.get(12));
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * Will pass if these methods are functional:
     *      add, get
     */
    public void getTestIllegalArg() {
        tree.add(ten);
        tree.add(five);
        tree.add(fifteen);
        tree.add(eighteen);
        tree.add(twelve);
        tree.add(six);
        tree.add(three);

        tree.get(twelve);

        tree.get(null);
    }

    @Test(expected = NoSuchElementException.class)
    /**
     * Will pass if these methods are functional:
     *      add, get
     */
    public void getTestNoSuchElement() {
        tree.add(ten);
        tree.add(five);
        tree.add(fifteen);
        tree.add(eighteen);
        tree.add(twelve);
        tree.add(six);
        tree.add(three);

        tree.get(twelve);
        tree.get(6);

        tree.get(13);
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * Will pass if these methods are functional:
     *      add, contains (add is strictly not really necessary)
     */
    public void containsTestIllegalArg() {
        tree.add(ten);
        tree.add(five);
        tree.add(fifteen);
        tree.add(eighteen);
        tree.add(twelve);
        tree.add(six);
        tree.add(three);

        tree.contains(null);
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, contains
     */
    public void containsTest() {
        tree.add(ten);
        tree.add(five);
        tree.add(fifteen);
        tree.add(eighteen);
        tree.add(twelve);
        tree.add(six);
        tree.add(three);

        assertTrue(tree.contains(10));
        assertTrue(tree.contains(15));
        assertTrue(tree.contains(6));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(18));
        assertTrue(tree.contains(12));
        assertTrue(tree.contains(3));

        assertFalse(tree.contains(0));
        assertFalse(tree.contains(-5));
        assertFalse(tree.contains(13));
        assertFalse(tree.contains(16));
        assertFalse(tree.contains(2));
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, preorder
     */
    public void preorderTest() {
        provisionTraversalTree();
        assertListsEqual(tree.preorder(), Arrays.asList(new Integer[] {10, 5, 3, 1, 15, 13, 11, 14, 17, 16}));
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, inorder
     */
    public void inorderTest() {
        provisionTraversalTree();
        assertListsEqual(tree.inorder(), Arrays.asList(new Integer[] {1, 3, 5, 10, 11, 13, 14, 15, 16, 17}));
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, postorder
     */
    public void postorderTest() {
        provisionTraversalTree();
        assertListsEqual(tree.postorder(), Arrays.asList(new Integer[] {1, 3, 5, 11, 14, 13, 16, 17, 15, 10}));
    }

    /**
     * Will pass if these methods are functional:
     *      add, preorder, inorder, postorder, levelorder
     *
     * (They needn't be fully functional to pass, this is only checking that an empty
     * tree results in an empty list.)
     */
    @Test
    public void emptyTraversalTest() {
        assertListsEqual(tree.preorder(), new ArrayList<Integer>());
        assertListsEqual(tree.postorder(), new ArrayList<Integer>());
        assertListsEqual(tree.inorder(), new ArrayList<Integer>());
        assertListsEqual(tree.levelorder(), new ArrayList<Integer>());
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, postorder
     */
    public void levelorderTest() {
        provisionTraversalTree();
        assertListsEqual(tree.levelorder(), Arrays.asList(new Integer[] {10, 5, 15, 3, 13, 17, 1, 11, 14, 16}));
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, kLargest
     */
    public void kLargestTest() {
        // test for empty
        assertTrue(tree.kLargest(0).isEmpty());
        // for convenience, use the traversal provisioner, which will
        // generate this sequence:
        // 1, 3, 5, 10, 11, 13, 14, 15, 16, 17
        provisionTraversalTree();
        tree.add(2);
        tree.add(20);
        tree.add(21);
        tree.add(30);
        tree.add(7);
        tree.add(6);
        // tree now has these elements
        // 1, 2, 3, 5, 6, 7, 10, 11, 13, 14, 15, 16, 17, 20, 21, 30
        Integer[] arr = new Integer[] {1, 2, 3, 5, 6, 7, 10, 11, 13, 14, 15, 16, 17, 20, 21, 30};
        for (int i = 0; i < 15; i++) {
            assertListsEqual(tree.kLargest(i), Arrays.asList(Arrays.copyOfRange(arr, 16 - i, 16)));
        }
    }

    @Test
    /**
     * Will pass if these methods are functional:
     *      add, kLargest
     */
    public void kLargestTestIllegalArg() {
        try {
            tree.kLargest(1);
            fail("IllegalArg exception should have been thrown");
        } catch (IllegalArgumentException e) { }

        tree.add(0);
        try {
            tree.kLargest(2);
            fail("IllegalArg exception should have been thrown");
        } catch (IllegalArgumentException e) { }

        tree.add(1);
        try {
            tree.kLargest(3);
            fail("IllegalArg exception should have been thrown");
        } catch (IllegalArgumentException e) { }

        tree.add(-1);
        try {
            tree.kLargest(4);
            fail("IllegalArg exception should have been thrown");
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Asserts that the given node is a leaf (both children are null)
     * @param node node to be checked
     */
    private void assertLeaf(BSTNode<Integer> node) {
        assertNull(node.getLeft());
        assertNull(node.getRight());
    }

    /**
     * This tests that the tree is equal to this tree (which is used a few times in different
     * tests):
     *
     *         0
     *     -5       5
     *       -1  3     7
     *            4
     */
    private void testParticularTree() {
        assertEquals((Integer) 0, tree.getRoot().getData());
        assertEquals((Integer) (-5), tree.getRoot().getLeft().getData());
        assertEquals((Integer) (-1), tree.getRoot().getLeft().getRight().getData());
        assertLeaf(tree.getRoot().getLeft().getRight());
        assertNull(tree.getRoot().getLeft().getLeft());
        assertEquals((Integer) 5, tree.getRoot().getRight().getData());
        assertEquals((Integer) 3, tree.getRoot().getRight().getLeft().getData());
        assertNull(tree.getRoot().getRight().getLeft().getLeft());
        assertEquals((Integer) 4, tree.getRoot().getRight().getLeft().getRight().getData());
        assertLeaf(tree.getRoot().getRight().getLeft().getRight());
        assertEquals((Integer) 7, tree.getRoot().getRight().getRight().getData());
        assertLeaf(tree.getRoot().getRight().getRight());
    }

    /**
     * Create a tree with the elements added below for traversals
     */
    private void provisionTraversalTree() {
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(3);
        tree.add(1);
        tree.add(17);
        tree.add(13);
        tree.add(16);
        tree.add(14);
        tree.add(11);
    }

    /**
     * Assert that two Lists are equal
     * @param one first list
     * @param two second list
     */
    private void assertListsEqual(List<Integer> one, List<Integer> two) {
        assertEquals(one.size(), two.size());
        for (int i = 0; i < one.size(); i++) {
            assertEquals(one.get(i), two.get(i));
        }
    }
}
