
import javafx.application.Application;
import javafx.stage.Stage;
import ui.javafx.Design;

public class Main extends Application {

	public static void main(String[] args) {

		launch(args);

	}

	@Override
	public void start(Stage arg0) throws Exception {

		Design design = new Design();

		design.display(arg0);

	}

}
