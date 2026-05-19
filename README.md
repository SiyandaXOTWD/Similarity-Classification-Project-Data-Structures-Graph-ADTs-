# Road Hazard Similarity & Classification System 🚗🛑

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![IDE](https://img.shields.io/badge/IDE-Eclipse-blue.svg)](https://www.eclipse.org/downloads/)
[![UI Framework](https://img.shields.io/badge/UI-JavaFX-blue.svg)](https://openjfx.io/)
[![Algorithm](https://img.shields.io/badge/Classification-K--Nearest%20Neighbors%20(KNN)-green.svg)](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm)

An intelligent, interactive desktop application built in Java to **help municipal workers, drivers, and surveyors quickly classify whether a road is damaged or safe, and automatically find other roads with similar degradation patterns**. 

By evaluating structural pavement imagery and mapping road segments onto a custom topological graph network, the system automates hazard detection and groups matching road profiles using a custom **K-Nearest Neighbors (KNN)** similarity engine.

---

## 🏗️ Core Project Functions

The application assists users through two primary machine learning and data structure pipelines:

```text
                                  ┌───> [ 1. CLASSIFY ] ───> Is the road Damaged or Safe?
 [ Pavement Image Input ] ────────┤
                                  └───> [ 2. FIND SIMILAR ] ─> Locate matching hazard profiles

1. Road Damage Classification: Users feed road surface images into the system. The application analyzes pavement fissures, cracks, and anomalies to instantly predict whether the infrastructure is damaged or safe.

2. Similarity Searching: Once a road is evaluated, the system uses distance-sorting metrics to scan the existing database network, surfacing a list of other roads that share similar structural deterioration characteristics. This helps prioritize scaling repair operations across similarly impacted zones.

📂 Repository Directory Tree
'''text
[Directory Tree]
├── Similarity & classification Project/
│   ├── .settings/                  # Eclipse project-specific environment configuration settings
│   ├── bin/                        # Compiled Java bytecode class outputs
│   ├── doc/                        # Generated Javadoc code documentation files
│   │
│   ├── src/                        # Main Application Source Code
│   │   ├── data/structures/        # Custom Network Data Structures (Dependency-Free)
│   │   │   ├── BTNode.java         # Binary Tree Node component helper
│   │   │   ├── Edge.java           # Relational link weight connector between road segments
│   │   │   ├── Entry.java          # Key-Value pair encapsulation for similarity distance rows
│   │   │   ├── Graph.java          # Full topological map network tracking all registered roads
│   │   │   ├── Heap.java           # Array-backed Binary Priority Queue for sorting similar entries
│   │   │   ├── Node.java           # Graph vertices holding geo-coordinates and feature vectors
│   │   │   └── RoadList.java       # Custom index collection tracking scannable road systems
│   │   │
│   │   ├── feature/extraction/     # Pavement Computer Vision Layer
│   │   │   └── CrackFeatureAnalyzer.java # Image analyzer evaluating surface cracks and fissure density
│   │   │
│   │   ├── image/classifier/       # Classification & Proximity Engine
│   │   │   ├── ClassificationTask.java   # Concurrent worker thread processing asynchronous calculations
│   │   │   └── KNNClassifier.java  # Custom K-Nearest Neighbors implementation calculating similarity
│   │   │
│   │   └── ui/javafx/              # Graphical Presentation Layer
│   │       └── Design.java         # Main JavaFX view layout displaying user dashboards & maps
│   │
│   ├── .classpath                  # Eclipse project path metadata dependencies references
│   └── .project                    # Eclipse global project nature signature definitions
│
└── dataset/                        # Image Evaluation Testing Corpuses
    ├── damaged/                    # Training/testing assets showing compromised roads (potholes/cracks)
    └── safe/                       # Reference images showing clear, smooth, well-maintained roads

⚙️ How the Similarity Engine Works

1. Feature Extraction
When an image is loaded, the CrackFeatureAnalyzer transforms visual pixels into a numerical feature vector
2. Finding Similar RoadsTo find roads with matching damage profiles, the KNNClassifier calculates the mathematical distance between the current road vector and other roads stored in the network Graph.These distance results are thrown into a custom array-based Min-Heap Priority Queue (Heap.java). The heap organizes the entries such that the closest, most identical road matches can be pulled off the top in $O(\log n)$ time, providing instantaneous similarity lookups on the JavaFX interface dashboard.

🛠️ Running the Project in Eclipse

Prerequisites
- Eclipse IDE (Eclipse IDE for Java Developers).
- Java 17 SDK or higher configured in your Eclipse workspace.
- JavaFX SDK downloaded locally on your machine.

- Setup & Run Instructions

1. Open Eclipse and go to File ──> Import...
2. Choose General ──> Existing Projects into Workspace and click Next.
3. Browse to select the 2026_Mini_Project folder as the root directory and click Finish.
4. Right-click the project in the Package Explorer and select Build Path ──> Configure Build Path...
5. Ensure that your JavaFX SDK .jar libraries are linked under the Modulepath tab.
6. Locate Design.java inside src/ui/javafx/, right-click it, and select Run As ──> Run Configurations...
7. Go to the (x)= Arguments tab.
  -Under the VM Arguments box, paste the following runtime configurations (make sure to update the path to match your local JavaFX SDK location): --module-path "C:\path\to\javafx-sdk-     17\lib" --add-modules javafx.controls,javafx.fxml
8. Clear the "Use the -XstartOnFirstThread argument when launching with SWT" checkbox if you are on macOS.
9.  Click Apply, and then click Run to spin up the application dashboard interface window.
