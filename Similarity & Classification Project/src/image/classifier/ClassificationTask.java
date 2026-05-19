package image.classifier;

import java.awt.image.BufferedImage;

import data.structures.Graph;
import data.structures.Node;
import data.structures.RoadList;
import feature.extraction.CrackFeatureAnalyzer;
import ui.javafx.Design;

public class ClassificationTask implements Runnable {

	private BufferedImage chosenImage;
	private Design design;
	private Graph graph;
	private KNNClassifier knnClassifier;

	/**
	 * a constuctor for initialisation
	 * 
	 * @param image         - the uploaded image
	 * @param design        - the gui design class
	 * @param trainingGraph - The graph from the training data
	 * @param knnClassifier - instance of the class that deals with the
	 *                      classification
	 * 
	 */
	public ClassificationTask(BufferedImage image, Design design, Graph trainingGraph, KNNClassifier knnClassifier) {

		this.chosenImage = image;
		this.design = design;
		this.knnClassifier = knnClassifier;
		this.graph = trainingGraph;

	}

	/**
	 * 
	 */
	@Override
	public void run() {

		CrackFeatureAnalyzer features = new CrackFeatureAnalyzer();

		// extract the features for the chosen image

		double[] queryFeatures = features.extractFeatures(chosenImage);

		// use knn to find the status of the uploaded image
		String knnLabel = knnClassifier.classify(queryFeatures, 5);

		// get a list of similar nodes of the uploaded image
		RoadList<Node> neighBours = knnClassifier.similarityCheck(queryFeatures, 5);

		// creaate a new node using the features of the uploaded image

		Node node = new Node("", queryFeatures, knnLabel);

		// add the node to the graph

		graph.addNode(node);

		// connet the node to its most similar nodes
		for (int i = 0; i < neighBours.size(); i++) {

			graph.addEdge(node, neighBours.get(i));

		}

		// Run BFS to confirm the status

		int newIndex = graph.getNodeIndex(node);
		int[] result = bfs(newIndex);

		int damaged = result[0];
		int safe = result[1];

		String finalStatus;
		String recommendation;

		if (knnLabel.equalsIgnoreCase("damaged") && damaged >= safe) {
			finalStatus = "NOT SAFE";
			recommendation = "Find an Alternative Road .";
		} else {
			finalStatus = "SAFE";
			recommendation = "Drive safely";
		}

		design.showResults(finalStatus + "\n" + recommendation);

	}

	/**
	 * performs Breadth-First Search using ther given node as a starting point and
	 * count damaged and safe neigbours
	 * 
	 * @param startIndex - the index we are starting to search from
	 * @return the array of intergers containing damaged and safe nodes count
	 */
	private int[] bfs(int startIndex) {

		boolean[] visited = new boolean[graph.getNumNodes()];

		RoadList<Integer> queue = new RoadList<>();
		queue.add(startIndex);

		int damaged = 0;
		int safe = 0;

		while (queue.size() > 0) {

			int currentIndex = queue.get(0);
			queue.removeAt(0);

			if (visited[currentIndex])
				continue;
			visited[currentIndex] = true;

			Node current = graph.getNode(currentIndex);

			// skip the starting node itself

			if (currentIndex != startIndex) {

				if (current.getStatus().equalsIgnoreCase("damaged")) {
					damaged++;
				} else if (current.getStatus().equalsIgnoreCase("safe")) {
					safe++;
				}
			}

			RoadList<Node> neighbors = current.getNeighbors();

			for (int i = 0; i < neighbors.size(); i++) {

				int ni = graph.getNodeIndex(neighbors.get(i));

				if (!visited[ni]) {
					queue.add(ni);
				}
			}
		}

		return new int[] { damaged, safe };
	}

}