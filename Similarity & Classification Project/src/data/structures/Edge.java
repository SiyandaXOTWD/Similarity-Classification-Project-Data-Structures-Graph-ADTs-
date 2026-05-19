package data.structures;

public class Edge {

	private Node start;
	private Node end;

	public Edge(Node node, Node target) {
		super();
		this.start = node;
		this.end = target;
	}

	public Node getStart() {
		return start;
	}

	public void setStart(Node start) {
		this.start = start;
	}

	public Node getEnd() {
		return end;
	}

	public void setEnd(Node end) {
		this.end = end;
	}
}