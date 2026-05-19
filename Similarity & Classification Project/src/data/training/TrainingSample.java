package data.training;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import data.structures.Entry;
import data.structures.Graph;
import data.structures.Heap;
import data.structures.RoadList;
import data.structures.Node;
import feature.extraction.CrackFeatureAnalyzer;

public class TrainingSample {

	private Graph databaseGraph;

	public Graph getDatabaseGraph() {
		return databaseGraph;
	}

	public TrainingSample() {
		databaseGraph = new Graph();
	}

	/**
	 * Reads images from a cetain folder and store them in a list
	 * 
	 * @param path - the path of a folder
	 * @return the list of images
	 */
	public RoadList<File> loadReferenceDataSet(String path) {

		File folder = new File(path);
		RoadList<File> list = new RoadList<>();

		if (folder.exists() && folder.isDirectory()) {

			File[] files = folder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png")
					|| name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(".gif")
					|| name.endsWith(".tiff") || name.endsWith(".webp"));

			if (files != null) {
				for (File file : files) {
					list.add(file);
				}
			}
		}

		return list;
	}

	/**
	 * Reads images from a dataset and train the system using the names of the
	 * folder and
	 */
	public void train() {

		RoadList<File> damaged = loadReferenceDataSet("dataset/damaged");
		trainOnDataset(damaged, "damaged", 500);

		RoadList<File> safe = loadReferenceDataSet("dataset/safe");
		trainOnDataset(safe, "safe", 500);

		addEdges(databaseGraph, 5);
	}

	/**
	 * @param list
	 * @param label
	 * @param maxImages
	 */
	private void trainOnDataset(RoadList<File> list, String label, int maxImages) {

		int imagesAdded = 0;

		for (int i = 0; i < list.size() && imagesAdded < maxImages; i++) {

			try {
				File file = list.get(i);

				BufferedImage image = ImageIO.read(file);
				if (image == null)
					continue;

				// Normalize full image
				image = normalizeLighting(image);

				// Extract features

				CrackFeatureAnalyzer analyzer = new CrackFeatureAnalyzer();
				double[] features = analyzer.extractFeatures(image);

				// Create Node
				Node node = new Node(file.getAbsolutePath(), features, label);

				databaseGraph.addNode(node);

				imagesAdded++;

			} catch (IOException e) {
				System.out.println("Error loading: " + list.get(i).getName());
			}
		}
	}

	/**
	 * @param g
	 * @param k
	 */
	private void addEdges(Graph g, int k) {

		int n = g.getNodes().size();

		for (int i = 0; i < n; i++) {

			Node current = g.getNodes().get(i);

			Heap<Double, Node> heap = new Heap<>();

			// insert all other nodes into heap
			for (int j = 0; j < n; j++) {

				if (i == j)
					continue;

				Node other = g.getNodes().get(j);

				double d = distance(current.getFeatures(), other.getFeatures());

				heap.insert(d, other);
			}

			// extract k nearest and create edges
			for (int t = 0; t < k && heap.size() > 0; t++) {

				Entry<Double, Node> e = heap.removeMin();

				g.addEdge(current, e.getValue());
			}
		}
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
	 * Normalizes lighting in an image by scaling grayscale values.
	 * 
	 * @param img the input image
	 * 
	 * @return a normalized image
	 */
	public static BufferedImage normalizeLighting(BufferedImage img) {

		int w = img.getWidth();
		int h = img.getHeight();

		int[][] gray = new int[w][h];

		int min = 255;
		int max = 0;

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				int rgb = img.getRGB(x, y);

				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = rgb & 0xff;

				int value = (r + g + b) / 3;

				gray[x][y] = value;

				if (value < min)
					min = value;
				if (value > max)
					max = value;
			}
		}

		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				int val = (gray[x][y] - min) * 255 / (max - min + 1);
				int rgb = (val << 16) | (val << 8) | val;

				out.setRGB(x, y, rgb);
			}
		}

		return out;
	}
}