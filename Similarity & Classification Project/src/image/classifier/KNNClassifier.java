package image.classifier;

import data.structures.Graph;
import data.structures.Node;
import data.structures.RoadList;
import data.structures.Heap;

import data.structures.Entry;

public class KNNClassifier {

	private Graph databaseGraph;

	public KNNClassifier(Graph databaseGraph) {
		this.databaseGraph = databaseGraph;
	}

	/**
	 * Calculates Euclidean distance between two feature vectors.
	 * 
	 * @param a first vector
	 * @param b second vector
	 * @return distance value
	 */
	private double distance(double[] a, double[] b) {

		double sum = 0;

		for (int i = 0; i < a.length; i++) {
			double diff = a[i] - b[i];
			sum += diff * diff;
		}

		return Math.sqrt(sum);
	}

	/**
	 * Performs a k-Nearest Neighbors (kNN) similarity check.
	 *
	 * @param query the query feature vector
	 * @param k     number of nearest neighbors
	 * @return a list of similar nodes
	 */
	public String classify(double[] query, int k) {

		Heap<Double, Node> heap = new Heap<>();

		int n = databaseGraph.getNodes().size();

		for (int i = 0; i < n; i++) {

			Node node = databaseGraph.getNodes().get(i);

			double d = distance(node.getFeatures(), query);

			heap.insert(d, node);
		}

		double safeWeight = 0;
		double damagedWeight = 0;

		int limit = Math.min(k, heap.size());

		for (int i = 0; i < limit; i++) {

			Entry<Double, Node> e = heap.removeMin();
			Node node = e.getValue();

			double d = e.getKey();
			double w = 1.0 / (d + 0.0001);

			String status = node.getStatus();

			if (status.equalsIgnoreCase("safe")) {
				safeWeight += w;
			} else {
				damagedWeight += w;
			}
		}

		return (damagedWeight > safeWeight) ? "damaged" : "safe";
	}

	/**
	 * Performs a k-Nearest Neighbors (kNN) similarity check.
	 * 
	 * @param query the query feature vector
	 * @param k     number of nearest neighbors
	 * @return a list of similar nodes
	 */
	public RoadList<Node> similarityCheck(double[] query, int k) {

		Heap<Double, Node> heap = new Heap<>();

		int n = databaseGraph.getNodes().size();

		for (int i = 0; i < n; i++) {

			Node node = databaseGraph.getNodes().get(i);

			double d = distance(node.getFeatures(), query);

			heap.insert(d, node);
		}

		double safeWeight = 0;
		double damagedWeight = 0;

		int limit = Math.min(k, heap.size());

		RoadList<Node> tempK = new RoadList<>();

		for (int i = 0; i < limit; i++) {

			Entry<Double, Node> e = heap.removeMin();
			Node node = e.getValue();

			tempK.add(node);

			double d = e.getKey();
			double w = 1.0 / (d + 0.0001);

			String status = node.getStatus();

			if (status.equalsIgnoreCase("safe")) {
				safeWeight += w;
			} else {
				damagedWeight += w;
			}
		}

		boolean isSafe = safeWeight >= damagedWeight;

		RoadList<Node> result = new RoadList<>();

		for (int i = 0; i < tempK.size(); i++) {

			Node nnode = tempK.get(i);

			if (isSafe && nnode.getStatus().equalsIgnoreCase("safe")) {
				result.add(nnode);
			} else if (!isSafe && nnode.getStatus().equalsIgnoreCase("damaged")) {
				result.add(nnode);
			}
		}

		return result;
	}
}