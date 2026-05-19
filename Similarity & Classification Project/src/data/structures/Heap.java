package data.structures;

import java.util.LinkedList;
import java.util.Queue;

public class Heap<K extends Comparable<K>, V> {

	protected BTNode<Entry<K, V>> root;
	protected int size;
	protected BTNode<Entry<K, V>> lastNode;

	public Heap() {
		this.size = 0;
		this.lastNode = null;
		this.root = null;
	}

	public void insert(K key, V value) {
		BTNode<Entry<K, V>> insertNode = null;
		if (root == null) {
			root = new BTNode<Entry<K, V>>(null, null, null, new Entry<K, V>(key, value));
			insertNode = root;
			lastNode = root;
			size++;
		} else {
			BTNode<Entry<K, V>> parent = findParentForInsert();
			BTNode<Entry<K, V>> newNode = new BTNode<Entry<K, V>>(parent, null, null, new Entry<K, V>(key, value));
			if (parent.left() == null) {
				parent.setLeft(newNode);
			} else {
				parent.setRight(newNode);
			}
			insertNode = newNode;
			lastNode = newNode;
			size++;
		}
		upheap(insertNode);
	}

	private BTNode<Entry<K, V>> findParentForInsert() {
		if (root == null)
			return null;
		Queue<BTNode<Entry<K, V>>> queue = new LinkedList<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			BTNode<Entry<K, V>> current = queue.poll();
			if (current.left() == null || current.right() == null) {
				return current;
			}
			queue.add(current.left());
			queue.add(current.right());
		}
		return null;
	}

	public Entry<K, V> removeMin() {
		if (size == 0) {
			return null;
		}
		Entry<K, V> itemToRemove = root.element();
		if (size == 1) {
			root = null;
			lastNode = null;
			size--;
			return itemToRemove;
		}
		BTNode<Entry<K, V>> last = getLastNode();
		root.setElement(last.element());
		last.setElement(null);
		BTNode<Entry<K, V>> parent = last.parent();
		if (parent != null) {
			if (parent.left() == last) {
				parent.setLeft(null);
			} else if (parent.right() == last) {
				parent.setRight(null);
			}
		}
		size--;
		if (size > 0) {
			lastNode = getLastNode();
		} else {
			lastNode = null;
		}
		if (size > 0) {
			downheap(root);
		}
		return itemToRemove;
	}

	private BTNode<Entry<K, V>> getLastNode() {
		if (root == null)
			return null;
		Queue<BTNode<Entry<K, V>>> queue = new LinkedList<>();
		queue.add(root);
		BTNode<Entry<K, V>> last = null;
		while (!queue.isEmpty()) {
			last = queue.poll();
			if (last.left() != null) {
				queue.add(last.left());
			}
			if (last.right() != null) {
				queue.add(last.right());
			}
		}
		return last;
	}

	private void upheap(BTNode<Entry<K, V>> node) {
		if (node == null || node.parent() == null) {
			return;
		}
		BTNode<Entry<K, V>> parent = node.parent();
		if (node.element().getKey().compareTo(parent.element().getKey()) < 0) {
			Entry<K, V> temp = node.element();
			node.setElement(parent.element());
			parent.setElement(temp);
			upheap(parent);
		}
	}

	private void downheap(BTNode<Entry<K, V>> node) {
		if (node == null || (node.left() == null && node.right() == null)) {
			return;
		}
		BTNode<Entry<K, V>> left = node.left();
		BTNode<Entry<K, V>> right = node.right();
		BTNode<Entry<K, V>> smallest = node;
		if (left != null && left.element() != null) {
			if (left.element().getKey().compareTo(smallest.element().getKey()) < 0) {
				smallest = left;
			}
		}
		if (right != null && right.element() != null) {
			if (right.element().getKey().compareTo(smallest.element().getKey()) < 0) {
				smallest = right;
			}
		}
		if (smallest != node) {
			Entry<K, V> temp = node.element();
			node.setElement(smallest.element());
			smallest.setElement(temp);
			downheap(smallest);
		}
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

}
