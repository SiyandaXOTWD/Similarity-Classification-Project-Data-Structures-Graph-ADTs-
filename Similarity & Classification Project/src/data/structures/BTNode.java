package data.structures;

public class BTNode<T> {

	private BTNode<T> parent;
	private BTNode<T> left;
	private BTNode<T> right;
	private T element;

	public BTNode(BTNode<T> parent, BTNode<T> left, BTNode<T> right, T element) {
		this.parent = parent;
		this.left = left;
		this.right = right;
		this.element = element;
	}

	public BTNode<T> parent() {
		return parent;
	}

	public BTNode<T> left() {
		return left;
	}

	public BTNode<T> right() {
		return right;
	}

	public T element() {
		return element;
	}

	public void setParent(BTNode<T> parent) {
		this.parent = parent;
	}

	public void setLeft(BTNode<T> left) {
		this.left = left;
	}

	public void setRight(BTNode<T> right) {
		this.right = right;
	}

	public void setElement(T element) {
		this.element = element;
	}

}
