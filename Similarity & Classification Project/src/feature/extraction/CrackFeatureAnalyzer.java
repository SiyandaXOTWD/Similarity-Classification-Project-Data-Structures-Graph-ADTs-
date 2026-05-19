package feature.extraction;

import java.awt.image.BufferedImage;

public class CrackFeatureAnalyzer {


    /**
     * Extracts the advanced image features such as edge density, clustering, gaps, and chaos
     * for classification.
     * 
     * @param img the input image
     * 
     * @return a feature vector representing the image
     */
    public  double[] extractFeatures(BufferedImage img) {

        img = normalizeLighting(img);

        int w = img.getWidth();
        int h = img.getHeight();

        int[][] gray = new int[w][h];
        int[][] edgeMap = new int[w][h];

        int min = 255;
        int max = 0;

        //Converting to grayscale.
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                int rgb = img.getRGB(x, y);

                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                int val = (r + g + b) / 3;

                gray[x][y] = val;

                if (val < min) min = val;
                if (val > max) max = val;
            }
        }

        double thresholdSum = 0;
        int count = 0;

        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {

                int gx =
                        (-1 * gray[x - 1][y - 1]) + (1 * gray[x + 1][y - 1]) +
                        (-2 * gray[x - 1][y]) + (2 * gray[x + 1][y]) +
                        (-1 * gray[x - 1][y + 1]) + (1 * gray[x + 1][y + 1]);

                int gy =
                        (-1 * gray[x - 1][y - 1]) + (-2 * gray[x][y - 1]) + (-1 * gray[x + 1][y - 1]) +
                        (1 * gray[x - 1][y + 1]) + (2 * gray[x][y + 1]) + (1 * gray[x + 1][y + 1]);

                int mag = (int) Math.sqrt(gx * gx + gy * gy);

                thresholdSum += mag;
                count++;
            }
        }

        double threshold = (thresholdSum / count) * 1.2;

        int edgeCount = 0;
        int clusterScore = 0;
        int gapScore = 0;
        int chaosScore = 0;

        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {

                int gx =
                        (-1 * gray[x - 1][y - 1]) + (1 * gray[x + 1][y - 1]) +
                        (-2 * gray[x - 1][y]) + (2 * gray[x + 1][y]) +
                        (-1 * gray[x - 1][y + 1]) + (1 * gray[x + 1][y + 1]);

                int gy =
                        (-1 * gray[x - 1][y - 1]) + (-2 * gray[x][y - 1]) + (-1 * gray[x + 1][y - 1]) +
                        (1 * gray[x - 1][y + 1]) + (2 * gray[x][y + 1]) + (1 * gray[x + 1][y + 1]);

                int mag = (int) Math.sqrt(gx * gx + gy * gy);

                if (mag > threshold) {

                    edgeMap[x][y] = 1;
                    edgeCount++;

                    if (Math.abs(gx) > 50 && Math.abs(gy) > 50) {
                        chaosScore++;
                    }
                }
            }
        }

        for (int x = 2; x < w - 2; x++) {
            for (int y = 2; y < h - 2; y++) {

                if (edgeMap[x][y] == 1) {

                    int neighbors = 0;

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (edgeMap[x + dx][y + dy] == 1)
                                neighbors++;
                        }
                    }

                    if (neighbors >= 5) {
                        clusterScore++;
                    }

                    if (edgeMap[x - 2][y] == 1 &&
                        edgeMap[x + 2][y] == 1 &&
                        edgeMap[x][y] == 0) {
                        gapScore++;
                    }
                }
            }
        }

        double edgeDensity = (double) edgeCount / (w * h);
        double clustering = (double) clusterScore / (w * h);
        double gaps = (double) gapScore / (w * h);
        double chaos = (double) chaosScore / (w * h);

        return new double[] { edgeDensity, clustering, gaps, chaos };
    }

    private static BufferedImage normalizeLighting(BufferedImage img) {
        return img;
    }
}