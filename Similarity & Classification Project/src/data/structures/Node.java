package data.structures;

public class Node {

	private RoadList<Edge> edges;
	private double[] features;
	private String status;
	private String path;

	/**
	 * @param path
	 * @param features
	 * @param status
	 */

	public Node(String path, double[] features, String status) {
		this.path = path;
		this.features = features;
		this.status = status;
		this.edges = new RoadList<>();
	}

	/**
	 * @param target
	 */
	public void addEdge(Node target) {
		edges.add(new Edge(this, target));
	}

	public RoadList<Edge> getEdges() {
		return edges;
	}

	/**
	 * @return
	 */
	public RoadList<Node> getNeighbors() {

		RoadList<Node> neighbors = new RoadList<>();

		for (int i = 0; i < edges.size(); i++) {
			neighbors.add(edges.get(i).getEnd());
		}

		return neighbors;
	}

	public double[] getFeatures() {
		return features;
	}

	public void setFeatures(double[] features) {
		this.features = features;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}