
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

//import C:\Users\Juan Prada\Documents\Bio7\features\commons-lang3-3.4\apidocs\org\apache\commons\lang3 org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.eco.bio7.actions.Bio7Action;
import com.eco.bio7.batch.Bio7Dialog;
import com.eco.bio7.batch.FileRoot;
import com.eco.bio7.image.ImageMethods;
import com.eco.bio7.rbridge.RServe;
import com.eco.bio7.rbridge.RState;
import com.eco.bio7.compile.RScript;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.OvalRoi;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/* All methods and variables have to be public in Bio7!
 * for JavaFX events see:
 * http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
 */
public class PleaseProvideControllerClassName implements MouseListener {
	double[] y;

	@FXML // ResourceBundle that was given to the FXMLLoader
	public ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	public URL location;

	@FXML // fx:id="para_1"
	public TextField para_1; // Value injected by FXMLLoader

	@FXML // fx:id="para_2"
	public TextField para_2; // Value injected by FXMLLoader

	@FXML // fx:id="para_3"
	public TextField para_3; // Value injected by FXMLLoader
	
	@FXML // fx:id="CriticCB"
    private CheckBox CriticCB; // Value injected by FXMLLoader
	
	@FXML // fx:id="VarShow"
    private CheckBox VarShow; // Value injected by FXMLLoader
	
	@FXML // fx:id="RoiAnalysis"
    private CheckBox RoiAnalysis; // Value injected by FXMLLoader

    @FXML // fx:id="WinSize"
    private TextField WinSize; // Value injected by FXMLLoader
    
    @FXML // fx:id="MinActCount"
    private TextField MinActCount; // Value injected by FXMLLoader

	public ImageWindow win;

	public ImageCanvas canvas;

	private PleaseProvideControllerClassName instance;

	private double parameter_1;

	private double parameter_2;

	private double parameter_3;
	
	private double WindowSize;
	
	private double MiniActs;

	double[] coordinatesX;
	int posX;
	int posY;
	double[] coordinatesY;
	double[] coordinatesYPlot;
	double[] reverse;
	double[][] matri;
	//double[][] signals;
	double[][] peaks;
	double CircDia;
	double MaxAct;
	//double[] yload;
	//double[] xload;
	double[][] indi;
	int size;
	String critical="FALSE";
	String Variance="FALSE";
	String RoiAna="FALSE";
	// ImageProcessor ip = img.getProcessor();
	int w;
	int h;
	// int Start = 1;
	// int End = 2000;
	//public int tamano = 12;
	// int MNT = 15;
	// int SNR = 3;
	
	public PleaseProvideControllerClassName() {
		this.instance = this;
	}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	public void initialize() {
		/*
		 * Here we have to install the listeners for textfields, slider, etc.!
		 */
		para_1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				try {
					parameter_1 = new Double(para_1.getText());
					para_1.setStyle("-fx-text-inner-color: black;");

				} catch (Exception e) {
					/* Change the color if we have not typed a double value! */
					para_1.setStyle("-fx-text-inner-color: red;");

				}
			}
		});
		para_2.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				try {
					parameter_2 = new Double(para_2.getText());
					para_2.setStyle("-fx-text-inner-color: black;");

				} catch (Exception e) {
					/* Change the color if we have not typed a double value! */
					para_2.setStyle("-fx-text-inner-color: red;");

				}
			}
		});
		para_3.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				try {
					parameter_3 = new Double(para_3.getText());
					para_3.setStyle("-fx-text-inner-color: black;");

				} catch (Exception e) {
					/* Change the color if we have not typed a double value! */
					para_3.setStyle("-fx-text-inner-color: red;");

				}
			}
		});
		
        assert CriticCB != null : "fx:id=\"CriticCB\" was not injected: check your FXML file 'Main.fxml'.";

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int offscreenX = canvas.offScreenX(x);
		int offscreenY = canvas.offScreenY(y);

		ImagePlus imp = WindowManager.getCurrentImage();
		/* We control if an image has been loaded! */
		if (imp != null) {
			float val = imp.getProcessor().getPixelValue(offscreenX, offscreenY);

			Bio7Dialog.message("Mouse pressed! " + "x:" + offscreenX + "," + "y:" + offscreenY + "\n" + "\n"
					+ "Pixel value: " + val + "\n" + "\n" + "Parameters " + "\n" + "\n" + "Window Size: " + parameter_3
					+ "\n" + "\n" + "SAT: " + parameter_1 + "\n" + "\n" + "SNR: " + parameter_2);

			
			if (RServe.isAliveDialog()) {
				if (RState.isBusy() == false) {
					/* Notify that R is busy! */
					RState.setBusy(true);
					Job job = new Job("Plot intensity signal") {

						@Override
						protected IStatus run(IProgressMonitor monitor) {
							monitor.beginTask("Plot signal and results ...", IProgressMonitor.UNKNOWN);

							runMouseClicked(offscreenX, offscreenY);

							monitor.done();
							return Status.OK_STATUS;
						}

					};
					job.addJobChangeListener(new JobChangeAdapter() {
						public void done(IJobChangeEvent event) {
							if (event.getResult().isOK()) {
								
								/*To allow plotting!*/
                                int countDev = RServe.getDisplayNumber();
                                RState.setBusy(false);
                                if (countDev > 0) {
                                    RServe.closeAndDisplay();
                                    }
								

								//markResultsInImage();

							} else {

								RState.setBusy(false);
							}
						}
					});

					job.schedule();
				} else {
					System.out.println("RServer is busy. Can't execute the R script!");
				}
			}
		}
		
		
	}
	
	
	// runMouseClicked is the function that calls graphic.R in order to generate the intensity signal from the region where the user clicked.
	private void runMouseClicked(int offscreenX, int offscreenY) {
		RConnection c = RServe.getConnection();
		ImagePlus imp = WindowManager.getCurrentImage();
		/* We control if an image has been loaded! */
		if (imp != null) {
			try {
				c.eval("rm(coordX)");
				c.eval("rm(coordY)");
				c.eval("coordX=" + offscreenX);
				c.eval("coordY=" + offscreenY);
			} catch (RserveException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			RServe.callRScript("/NeuronActivityTool_ROI/src/graphic.R");

		}

	}
	
	

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	 @FXML
	    public void loadAndStart(ActionEvent event) {
		 if(RServe.isConnected()==false){
			 Bio7Action.callRserve();
			 }

		 IJ.getInstance().doCommand("Open...");
		 RConnection c = RServe.getConnection();
		 try {
				// c.eval("imageSizeY<-" + h);
				c.eval("rm(list = ls())");
			} catch (RserveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 		 
	    }
	 
		@FXML
	    void PerformRoiAnalysis(ActionEvent event) {
			
			if(RoiAnalysis.isSelected()){
		        RoiAna = "TRUE";
		        System.out.println(RoiAna); 
		     }

	    }

	/* Running in an Eclipse job! */
	@FXML
	public void loadAndProcess(ActionEvent event) {
		ImagePlus imp = WindowManager.getCurrentImage();
		/* We control if an image has been loaded! */
		if (imp != null) {
			size = imp.getImageStackSize();
			w = imp.getWidth();
			h = imp.getHeight();
			parameter_3 = new Double(para_3.getText());
			if (RServe.isAliveDialog()) {
				if (RState.isBusy() == false) {
					/* Notify that R is busy! */
					RState.setBusy(true);
					Job job = new Job("Transfer from R") {

						@Override
						protected IStatus run(IProgressMonitor monitor) {
							monitor.beginTask("Transfer Image Data And Convolve ...", size);
							runLoadAndProcess(monitor);
							monitor.done();
							return Status.OK_STATUS;
						}

					};
					job.addJobChangeListener(new JobChangeAdapter() {
						public void done(IJobChangeEvent event) {
							if (event.getResult().isOK()) {

								RState.setBusy(false);
							} else {

								RState.setBusy(false);
							}
						}
					});

					job.schedule();
				} else {
					System.out.println("RServer is busy. Can't execute the R script!");
				}
			}
		} else {
			Bio7Dialog.message("No image available!");
		}

	}

	public void runLoadAndProcess(IProgressMonitor monitor) {

		if(!RoiAnalysis.isSelected()){
	        RoiAna = "FALSE";
	    }
		
		try {
			RConnection c = RServe.getConnection();
			// define the parameters necessary for R to perform the image convolution and the intensity signal extraction
			c.eval("numFrames<-" + size);
			c.eval("tamano=" + parameter_3);
			c.eval("numCol=" + h);
			c.eval("numRow=" + w);
			c.eval("RoiAna=" + RoiAna);
			c.eval("defArray = array(0,  c(ceiling(numRow/tamano), ceiling(numCol/tamano), numFrames))");
			
			if(RoiAnalysis.isSelected()){
		    c.eval("DefArrayRoi = matrix(0,length(roiXSelectionCoordinates),numFrames)");
			RServe.callRScriptSilent("/NeuronActivityTool_ROI/src/ROI_Definer.R");
			}
			
			int slice = 1;
			// Loop over the stack!
			while (slice <= size) {
				if (monitor.isCanceled() == false) {
					IJ.run("Set Slice...", "slice=" + slice);
					// Transfer as integer! ->0=double, 1=integer,
					// 2=byte,
					// 3=RGB
					try {
						// c.eval("imageSizeY<-" + h);
						c.eval("frame<-" + slice);
					} catch (RserveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// RServe.evaluateExt("frame<-" + slice);
					ImageMethods.imageToR("current", true, 1, null);
					RServe.callRScriptSilent("/NeuronActivityTool_ROI/src/convolve.R");
					monitor.worked(1);
					slice++;
				} else {
					break;
				}
			}
			
		} catch (RserveException e) {

			System.out.println(e.getRequestErrorDescription());
		}
	}
	
	@FXML
    void enableCritical(ActionEvent event) {
		
		if(CriticCB.isSelected()){
	        critical = "TRUE";
	        System.out.println(critical); 
	     }

    }
	
	 @FXML
	    void ShowVariance(ActionEvent event) {
		 
		 if(VarShow.isSelected()){
		        Variance = "TRUE";
		        System.out.println(critical); 
		     }

	    }

	/* Running in an Eclipse job! */
	@FXML
	public void capturePoints(ActionEvent event) {

		if (RServe.isAliveDialog()) {
			if (RState.isBusy() == false) {
				/* Notify that R is busy! */
				RState.setBusy(true);
				Job job = new Job("Transfer from R") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("Transfer Image Data And Convolve ...", IProgressMonitor.UNKNOWN);

						runCapturePoints();

						monitor.done();
						return Status.OK_STATUS;
					}

				};
				job.addJobChangeListener(new JobChangeAdapter() {
					public void done(IJobChangeEvent event) {
						if (event.getResult().isOK()) {

							RState.setBusy(false);
							
							//if(!RoiAnalysis.isSelected()){
								markResultsInImage();
							//}
							

						} else {

							RState.setBusy(false);
						}
					}
				});

				job.schedule();
			} else {
				System.out.println("RServer is busy. Can't execute the R script!");
			}
		}

	}
	
    // the function runCapturePoints perform the peak detection stage by calling the r function WaveletCalc
	private void runCapturePoints() {
		RConnection c = RServe.getConnection();
		ImagePlus imp = WindowManager.getCurrentImage();
		/* We control if an image has been loaded! */
		if (imp != null) {

			canvas = imp.getCanvas();

			MouseListener[] ml = canvas.getMouseListeners();
			/* Remove listeners if already available! */
			if (ml.length > 0) {
				for (int i = 0; i < ml.length; i++) {

					canvas.removeMouseListener(ml[i]);
				}
			}
			canvas.addMouseListener(instance);
			/*Get the parameters from the Text to ensure that we get default values if the textfields weren't changed!*/
			parameter_1 = new Double(para_1.getText());
			parameter_2 = new Double(para_2.getText());
			parameter_3 = new Double(para_3.getText());
			WindowSize = new Double(WinSize.getText());
			MiniActs = new Double(MinActCount.getText());
			ImageMethods.imageToR("current", true, 1, null);
			System.out.println("Parameters are:");
            System.out.println("Window Size: "+parameter_3);
            System.out.println("SNR: "+parameter_2);
            System.out.println("SAT: "+parameter_1);
            
            if(!VarShow.isSelected()){
		        Variance = "FALSE";
		    }
            if(!CriticCB.isSelected()){
		        critical = "FALSE";
		    }
			try {
				//c.eval("rm(MNT)");
				//c.eval("rm(SNR)");
				c.eval("MNT=" + parameter_1);
				c.eval("SNR=" + parameter_2);
				c.eval("WinSize=" + WindowSize);
				c.eval("minPeaks=" + MiniActs);
				c.eval("criticalEnable=" + critical);
				c.eval("VarianceEnable=" + Variance);
			} catch (RserveException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			RServe.callRScriptSilent("/NeuronActivityTool_ROI/src/WaveletCalc2.R");

			try {
				coordinatesX = c.eval("puntosBio[,1]").asDoubles();
				coordinatesY = c.eval("puntosBio[,2]").asDoubles();
				MaxAct = c.eval("max(indi)").asDouble(); 
				matri = c.eval("indiBio").asDoubleMatrix();
			} catch (REXPMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RserveException e) {

				System.out.println(e.getRequestErrorDescription());
			}
			
		}

	}
    /*This has to be run outside of a job because the image methods are threaded!*/
	private void markResultsInImage() {
		ImagePlus imp = WindowManager.getCurrentImage();
		
		/* We control if an image has been loaded! */
		if (imp != null) {
			
			for (int ind = 0; ind <= (coordinatesX.length); ind++) {
				
				int WS = (int) parameter_3;
				posX = (int) coordinatesX[ind];
				posY = (int) coordinatesY[ind];
				
				CircDia = matri[(int) coordinatesX[ind]-1][(int) coordinatesY[ind]-1]*(parameter_3/(MaxAct));
				imp.setRoi(new OvalRoi(((coordinatesX[ind] * parameter_3 - (parameter_3 / 2)) - (CircDia / 2)),
						((coordinatesY[ind] * parameter_3 - (parameter_3 / 2)) - (CircDia / 2)), CircDia, CircDia));
				IJ.run(imp, "Add Selection...", "");
				IJ.run("Overlay Options...", "stroke=red width=1 fill=none apply");

			} 
		}
	}

	@FXML
	public void stopCapture(ActionEvent event) {
		ImagePlus imp = WindowManager.getCurrentImage();
		/* We control if an image has been loaded! */
		if (imp != null) {

			canvas = imp.getCanvas();

			MouseListener[] ml = canvas.getMouseListeners();
			if (ml.length > 0) {
				for (int i = 0; i < ml.length; i++) {

					canvas.removeMouseListener(ml[i]);
				}
			}
			/*Remove the overlays!*/
			IJ.run("Remove Overlay", "");
			/*Remove the selection, too!*/
			IJ.run("Select None");
		}

	}

}
