package data.structures;

public class Graph {

	private RoadList<Node> nodes;

	/**
	 * Default constructor used to initialize the graph node list.
	 */
	public Graph() {

		nodes = new RoadList<>();
	}

	/**
	 * Adds a node to the graph.
	 * 
	 * @param node the node to add
	 */
	public void addNode(Node node) {

		nodes.add(node);
	}

	/**
	 * Finds the index of a given node in the graph.
	 * 
	 * @param n the node to search for
	 * @return the index of the node if found, -1 otherwise
	 */
	public int getNodeIndex(Node n) {

		for (int i = 0; i < nodes.size(); i++) {

			if (nodes.get(i) == n) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Connects two nodes together 
	 * 
	 * @param node1 the first node
	 * @param node2 the second node
	 */
	public void addEdge(Node node1, Node node2) {

		node1.addEdge(node2);
		node2.addEdge(node1);
	}

	/**
	 * Removes a node from the graph.
	 * 
	 * @param n the node to remove
	 */
	public void removeNode(Node n) {

		nodes.remove(n);

		for (int i = 0; i < nodes.size(); i++) {

			Node current = nodes.get(i);

			RoadList<Node> neighbors = current.getNeighbors();

			neighbors.remove(n);
		}
	}

	/**
	 * Returns the total number of nodes in the graph.
	 * 
	 * @return number of nodes
	 */
	public int getNumNodes() {
		return nodes.size();
	}

	/**
	 * Returns a node at a given index.
	 * 
	 * @param index the node index
	 * @return the node at the specified index
	 */
	public Node getNode(int index) {
		return nodes.get(index);
	}

	
	public RoadList<Node> getNodes() {
		return nodes;
	}

	
	public void setNodes(RoadList<Node> nodes) {
		this.nodes = nodes;
	}
}