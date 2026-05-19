package ui.javafx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import data.structures.Graph;
import data.structures.Node;
import data.structures.RoadList;
import data.training.TrainingSample;
import feature.extraction.CrackFeatureAnalyzer;
import image.classifier.ClassificationTask;
import image.classifier.KNNClassifier;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Design {

    private Button btnClassificationHome;
    private Button btnSimilarityHome;

    private Button btnChoose;
    private Button btnUpload;
    private Button btnClassify;
    private Button btnSimilarity;
    private Button btnBack;

    private TextArea txtArea;
    private Label lblStatus;
    private BufferedImage choosenImage;
    private ImageView imageView;
    
    private HBox similarityRow;

    private Graph databaseGraph;
    private KNNClassifier knnClassifier;

    public Design() {
        txtArea = new TextArea();
        txtArea.setEditable(false);
        txtArea.setWrapText(true);
        
        txtArea.setPrefHeight(120);
        txtArea.setMaxWidth(800);
        txtArea.setStyle("-fx-control-inner-background: #1E293B; -fx-text-fill: #00FFCC;");
        txtArea.setFont(Font.font("Consolas", 16));
        txtArea.setBorder(new Border(new BorderStroke(Color.CYAN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        
        loadTrainingData();
    }

    private void loadTrainingData() {
        lblStatus = new Label("Status: Waiting for action...");
        TrainingSample trainer = new TrainingSample();
        trainer.train();
        databaseGraph = trainer.getDatabaseGraph();
        knnClassifier = new KNNClassifier(databaseGraph);
    }

    public void display(Stage primaryStage) {
        Label title = new Label("Road Damage Detection System");
        title.setFont(Font.font("Arial Black", FontWeight.BOLD, 60));
        title.setTextFill(Color.CYAN);

        Label subtitle = new Label("Welcome to the system");
        subtitle.setFont(Font.font("Consolas", 24));
        subtitle.setTextFill(Color.WHITE);

        Label desc = new Label("Road damage classification and similarity detection system.");
        desc.setFont(Font.font("Arial", 18));
        desc.setTextFill(Color.web("#BBBBBB"));

        btnClassificationHome = new Button("START CLASSIFICATION");
        btnSimilarityHome = new Button("SIMILARITY DETECTION");

        applyButtonStyle(btnClassificationHome);
        applyButtonStyle(btnSimilarityHome);

        VBox content = new VBox(30, title, subtitle, desc, btnClassificationHome, btnSimilarityHome);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(content);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root, 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnClassificationHome.setOnAction(e -> showSystemUI(primaryStage, true));
        btnSimilarityHome.setOnAction(e -> showSystemUI(primaryStage, false));
    }

    private void applyButtonStyle(Button b) {
        b.setPrefWidth(280); 
        b.setPrefHeight(70); 
        b.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        b.setTextFill(Color.CYAN);
        b.setCursor(Cursor.HAND);
        b.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.75), new CornerRadii(20), Insets.EMPTY)));
        b.setBorder(new Border(new BorderStroke(Color.CYAN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
        addHoverAnimation(b);
    }

    private void showSystemUI(Stage primaryStage, boolean isClassificationMode) {
        btnChoose = new Button("Choose Image");
        btnUpload = new Button("Upload");
        btnClassify = new Button("Classify");
        btnSimilarity = new Button("Similar Images");
        btnBack = new Button("Back");

        Button[] buttons = { btnChoose, btnUpload, btnClassify, btnSimilarity, btnBack };
        for (Button b : buttons) {
            applyButtonStyle(b);
            b.setPrefWidth(220); 
            b.setPrefHeight(65); 
        }

        btnUpload.setDisable(true);
        btnClassify.setDisable(true);
        btnSimilarity.setDisable(true);

        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(240);
        imageView.setPreserveRatio(true);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setPrefSize(320, 260);
        imageContainer.setBackground(new Background(new BackgroundFill(Color.web("#111827"), new CornerRadii(20), Insets.EMPTY)));
        imageContainer.setEffect(new DropShadow(20, Color.BLACK));

        lblStatus.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblStatus.setTextFill(Color.CYAN);
        
        VBox imageColumn = new VBox(10, lblStatus, imageContainer);
        VBox bottomSection = new VBox(15);

        if (isClassificationMode) {
            Label classTitle = new Label("Classification Results");
            classTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
            classTitle.setTextFill(Color.WHITE);
            bottomSection.getChildren().addAll(classTitle, txtArea);
        } else {
            Label simTitle = new Label("Similarity Results");
            simTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
            simTitle.setTextFill(Color.WHITE);
            similarityRow = new HBox(15);
            for (int i = 0; i < 5; i++) {
                StackPane box = new StackPane();
                box.setPrefSize(160, 160);
                box.setBackground(new Background(new BackgroundFill(Color.web("#1E293B"), new CornerRadii(14), Insets.EMPTY)));
                box.setEffect(new DropShadow(15, Color.BLACK));
                similarityRow.getChildren().add(box);
            }
            bottomSection.getChildren().addAll(simTitle, similarityRow);
        }

        VBox buttonColumn = new VBox(15, btnChoose, btnUpload, isClassificationMode ? btnClassify : btnSimilarity, btnBack);
        HBox topRow = new HBox(40, buttonColumn, imageColumn);
        VBox mainLayout = new VBox(40, topRow, bottomSection);
        mainLayout.setPadding(new Insets(50, 30, 30, 30)); 
        mainLayout.setAlignment(Pos.TOP_LEFT);

        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#020617")), new Stop(1, Color.web("#111827")));
        mainLayout.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        primaryStage.setScene(new Scene(mainLayout, 1100, 750));
        btnBack.setOnAction(e -> display(primaryStage));
        setActions(primaryStage);
    }

    public void showResults(String status) {
        javafx.application.Platform.runLater(() -> {
            txtArea.setText(status);
            lblStatus.setText(status);
        });
    }

    public void showClassificationResult(String result) {
        javafx.application.Platform.runLater(() -> {
            txtArea.setText(result);
            lblStatus.setText("Status: Classification complete");
        });
    }

    private void setActions(Stage stage) {
        btnChoose.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                try {
                    choosenImage = ImageIO.read(file);
                    imageView.setImage(new Image(file.toURI().toString()));
                    lblStatus.setText("Status: Image Selected");
                    btnUpload.setDisable(false);
                    btnClassify.setDisable(true);
                    btnSimilarity.setDisable(true);
                } catch (IOException ex) {}
            }
        });

        btnUpload.setOnAction(e -> {
            lblStatus.setText("Status: Image Uploaded");
            btnClassify.setDisable(false);
            btnSimilarity.setDisable(false);
        });

        btnClassify.setOnAction(e -> {
            lblStatus.setText("Status: Classifying...");
            ClassificationTask task = new ClassificationTask(choosenImage, this, databaseGraph, knnClassifier);
            new Thread(task).start();
        });

        btnSimilarity.setOnAction(e -> {
            lblStatus.setText("Status: Finding Similar Images...");
            CrackFeatureAnalyzer feature = new CrackFeatureAnalyzer();
            double[] features = feature.extractFeatures(choosenImage);
            RoadList<Node> results = knnClassifier.similarityCheck(features, 5);
            showSimilarityResults(results);
        });
    }

    private void showSimilarityResults(RoadList<Node> results) {
        if (similarityRow == null) return;
        for (int i = 0; i < results.size() && i < similarityRow.getChildren().size(); i++) {
            Node n = results.get(i);
            if (n.getPath() == null || n.getPath().isBlank()) continue;
            
            StackPane box = (StackPane) similarityRow.getChildren().get(i);
            box.getChildren().clear();
            ImageView iv = new ImageView(new Image("file:" + n.getPath()));
            iv.setFitWidth(140);
            iv.setFitHeight(140);
            iv.setPreserveRatio(true);
            box.getChildren().add(iv);
        }
        lblStatus.setText("Status: Search Complete");
    }

    private void addHoverAnimation(Button button) {
        ScaleTransition up = new ScaleTransition(Duration.millis(150), button);
        up.setToX(1.05); up.setToY(1.05);
        ScaleTransition down = new ScaleTransition(Duration.millis(150), button);
        down.setToX(1); down.setToY(1);
        button.setOnMouseEntered(e -> up.playFromStart());
        button.setOnMouseExited(e -> down.playFromStart());
    }
}