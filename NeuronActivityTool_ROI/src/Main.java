import com.eco.bio7.batch.FileRoot;
import com.eco.bio7.collection.CustomView;
import com.eco.bio7.collection.Work;

public class Main extends com.eco.bio7.compile.Model {

	public void setup() {

		/* Open the custom perspective! */
		// Work.openPerspective("com.eco.bio7.CustomPerspective");

		/* Open the ImageJ view in the current perspective! */
		Work.openView("com.eco.bio7.imagej");
		/* Create a custom view and embed the ModelGUI! */

		CustomView view = new CustomView();

		view.setFxmlCanvas("left", FileRoot.getFileRoot() + "/NeuronActivityTool_ROI/src/Main.fxml",
				new PleaseProvideControllerClassName());

		// System.out.println(Arrays.toString(y));

	}

	public void run() {

	}
	

}