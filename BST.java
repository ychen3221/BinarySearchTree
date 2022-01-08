import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Your implementation of a BST.
 *
 * @author Yueqiao Chen
 * @version 1.0
 * @userid ychen3221
 * @GTID 903531127
 *
 * Collaborators: N/A
 *
 * Resources: canvas, lectures
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("cannot build BST with null collection");
        } else {
            for (T element: data) {
                if (element == null) {
                    throw new java.lang.IllegalArgumentException("cannot add null element in BST");
                }
                add(element);
            }
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("cannot add null data into BST");
        } else {
            root = addHelper(root, data);
        }
    }

    private BSTNode<T> addHelper(BSTNode<T> node, T data) {
        if (node == null) {
            size++;
            return new BSTNode<T>(data);
        } else if (data.compareTo(node.getData()) < 0) {
            node.setLeft(addHelper(node.getLeft(), data));
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRight(addHelper(node.getRight(), data));
        }
        return node;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("cannot remove null data");
        } else if (root == null) {
            throw new java.util.NoSuchElementException("cannot remove for empty BST");
        } else {
            BSTNode<T> temp = new BSTNode<>(null);
            root = removeHelper(root, data, temp);
            return temp.getData();
        }
    }

    private BSTNode<T> removeHelper(BSTNode<T> node, T data, BSTNode<T> temp) {
        if (node == null) {
            throw new java.util.NoSuchElementException("cannot remove data if data is not in BST");
        } else if (data.compareTo(node.getData()) < 0) {
            node.setLeft(removeHelper(node.getLeft(), data, temp));
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRight(removeHelper(node.getRight(), data, temp));
        } else {
            temp.setData(node.getData());
            size--;
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            } else if (node.getLeft() == null && node.getRight() != null) {
                return node.getRight();
            } else if (node.getRight() == null && node.getLeft() != null) {
                return node.getLeft();
            } else {
                BSTNode<T> successor = new BSTNode<>(null);
                node.setRight(successorFinder(node.getRight(), successor));
                node.setData(successor.getData());
            }
        }
        return node;
    }

    private BSTNode<T> successorFinder(BSTNode<T> node, BSTNode<T> temp) {
        if (node.getLeft() == null) {
            temp.setData(node.getData());
            return node.getRight();
        } else {
            node.setLeft(successorFinder(node.getLeft(), temp));
        }
        return node;
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("cannot get data if data is null");
        } else if (root == null) {
            throw new java.util.NoSuchElementException("cannot get data in empty BST");
        } else {
            return getHelper(root, data).getData();
        }
    }

    private BSTNode<T> getHelper(BSTNode<T> node, T data) {
        if (node != null) {
            if (data.equals(node.getData())) {
                return node;
            } else if (data.compareTo(node.getData()) < 0) {
                return getHelper(node.getLeft(), data);
            } else if (data.compareTo(node.getData()) > 0) {
                return getHelper(node.getRight(), data);
            }
        }
        throw new java.util.NoSuchElementException("cannot get data if data is not in BST");
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("BST doesn't contain null data");
        } else if (root == null) {
            return false;
        } else {
            return containsHelper(root, data);
        }
    }

    private boolean containsHelper(BSTNode<T> node, T data) {
        if (node != null) {
            if (node.getData().equals(data)) {
                return true;
            } else if (data.compareTo(node.getData()) < 0) {
                return containsHelper(node.getLeft(), data);
            } else if (data.compareTo(node.getData()) > 0) {
                return containsHelper(node.getRight(), data);
            }
        }
        return false;
    }

    /**
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> list = new ArrayList<>();
        preOrderHelper(root, list);
        return list;
    }

    private void preOrderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            list.add(node.getData());
            preOrderHelper(node.getLeft(), list);
            preOrderHelper(node.getRight(), list);
        }
    }

    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> list = new ArrayList<>();
        inOrderHelper(root, list);
        return list;
    }

    private void inOrderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            inOrderHelper(node.getLeft(), list);
            list.add(node.getData());
            inOrderHelper(node.getRight(), list);
        }
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> list = new ArrayList<>();
        postOrderHelper(root, list);
        return list;
    }

    private void postOrderHelper(BSTNode<T> node, List<T> list) {
        if (node != null) {
            postOrderHelper(node.getLeft(), list);
            postOrderHelper(node.getRight(), list);
            list.add(node.getData());
        }
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        Queue<BSTNode<T>> queue = new LinkedList<>();
        List<T> list = new ArrayList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode<T> node = queue.remove();
            if (node != null) {
                list.add(node.getData());
                queue.add(node.getLeft());
                queue.add(node.getRight());
            }
        }
        return list;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(BSTNode<T> node) {
        if (node == null) {
            return -1;
        } else if (node.getRight() == null && node.getLeft() == null) {
            return 0;
        } else {
            return Math.max(heightHelper(node.getLeft()), heightHelper(node.getRight())) + 1;
        }
    }


    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Finds and retrieves the k-largest elements from the BST in sorted order,
     * least to greatest.
     *
     * This must be done recursively.
     *
     * In most cases, this method will not need to traverse the entire tree to
     * function properly, so you should only traverse the branches of the tree
     * necessary to get the data and only do so once. Failure to do so will
     * result in an efficiency penalty.
     *
     * EXAMPLE: Given the BST below composed of Integers:
     *
     *                50
     *              /    \
     *            25      75
     *           /  \
     *          12   37
     *         /  \    \
     *        10  15    40
     *           /
     *          13
     *
     * kLargest(5) should return the list [25, 37, 40, 50, 75].
     * kLargest(3) should return the list [40, 50, 75].
     *
     * Should have a running time of O(log(n) + k) for a balanced tree and a
     * worst case of O(n + k).
     *
     * @param k the number of largest elements to return
     * @return sorted list consisting of the k largest elements
     * @throws java.lang.IllegalArgumentException if k > n, the number of data
     *                                            in the BST
     */
    public List<T> kLargest(int k) {
        if (k > size) {
            throw new java.lang.IllegalArgumentException("cannot have a list longer than size");
        } else {
            List<T> list = new LinkedList<>();
            helper(root, list, k);
            return list;
        }
    }

    private void helper(BSTNode<T> node, List<T> list, int k) {
        if (node != null) {
            helper(node.getRight(), list, k);
            if (list.size() < k) {
                list.add(0, node.getData());
            } else {
                return;
            }
            helper(node.getLeft(), list, k);
        }
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
