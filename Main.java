import javafx.scene.Scene;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javafx.scene.control.TextField;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class Main extends Application{
    public static void main(String[] args) throws Exception{
        launch(args);
        
    }
    BigDecimal x = new BigDecimal("0.75");
    BigDecimal y = new BigDecimal("0");
    BigDecimal w = new BigDecimal("1920");
    BigDecimal h = new BigDecimal("1080");
    BigDecimal zoom = new BigDecimal("0.25");
    int Precision = 10;
    BigDecimal iter = new BigDecimal("100");
    @Override
    public void start(Stage stage) throws Exception{
        Group root1 = new Group();
        TextField wIn = new TextField("1920");
        TextField hIn = new TextField("1080");
        TextField xIn = new TextField("0.75");
        TextField yIn = new TextField("0");
        TextField zoomIn = new TextField("0.25");
        TextField PrecisionIn = new TextField("10");
        TextField iterIn = new TextField("100");
        Label xLabel = new Label("x Value");
        Label yLabel = new Label("y Value");
        Label wLabel = new Label("Target Width");
        Label hLabel = new Label("Target Height");
        Label zoomLabel = new Label("zoom");
        Label precisionLabel = new Label("precision (High performance impact)");
        Label iterLabel = new Label("amount of iterations (High performance impact)"); 
        Label runtimeLabel = new Label("Press start to begin computing. press save to attempt save. press stop to stop the computation");
        Button Begin = new Button("Start"); 
        Button Save = new Button("Save");
        Button Stop = new Button("Stop");
        GridPane pane = new GridPane();
        Scene scene2 = new Scene(pane, 1200, 600);
        Scene MBScene = new Scene(root1, 1920, 1080, Color.BLACK);
        MandelThread mt = new MandelThread();
        EventHandler<ActionEvent> wEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    w = new BigDecimal(wIn.getText());
                } catch (NumberFormatException f) {
                    
                }
            }
        };
        EventHandler<ActionEvent> hEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    h = new BigDecimal(hIn.getText());
                } catch (NumberFormatException f) {
                    
                }
            }
        };
        EventHandler<ActionEvent> xEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    x = new BigDecimal(xIn.getText());
                } catch (NumberFormatException f) {
                    
                }
            }
        };
        EventHandler<ActionEvent> yEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    y = new BigDecimal(yIn.getText());
                } catch (NumberFormatException f) {
                    
                }   
            }
        };
        EventHandler<ActionEvent> zoomEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    zoom = new BigDecimal(zoomIn.getText());
                } catch (NumberFormatException f) {
                    
                }
            }
        };
        EventHandler<ActionEvent> precisionEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    Precision = Integer.parseInt(PrecisionIn.getText());
                } catch (NumberFormatException f) {
                    
                }
            }
        };
        
        EventHandler<ActionEvent> iterEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    iter = new BigDecimal(iterIn.getText());
                } catch (NumberFormatException f) {
                    
                }
            }
        };
        EventHandler<ActionEvent> StartComputing = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                if(mt.checkIfRan()){
                    runtimeLabel.setText("Already running. press stop to stop");
                }else{
                    runtimeLabel.setText("Running. press save periodically to check if ready to save");
                    Canvas canvas = new Canvas(w.intValue(),h.intValue());
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    PixelWriter pix = gc.getPixelWriter();
                    mt.setVars(pix, x, y, w, h, zoom, Precision, iter, root1, canvas, MBScene);
                    mt.start();
                }  
                } catch (NullPointerException AlertNull) {
                    runtimeLabel.setText("Please input all variables first");
                }
                
                
            }
        };
        EventHandler<ActionEvent> interruptComputing = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if (mt.checkIfRan()){
                mt.interrupt();      
                runtimeLabel.setText("Stopped");
                stage.close();    
                }else{
                    runtimeLabel.setText("Not running");
                }
            }
        };
        EventHandler<ActionEvent> SaveEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
            if(mt.checkIfDone()){
                runtimeLabel.setText("We are Done.");
                SaveToFile(mt.ReturnScene());
                stage.close();
            }else{
                runtimeLabel.setText("not done yet. check in 5 minutes");
            }
            }
        };
        Begin.setOnAction(StartComputing);
        Save.setOnAction(SaveEvent);
        Stop.setOnAction(interruptComputing);
        wIn.setOnAction(wEvent);
        hIn.setOnAction(hEvent);
        xIn.setOnAction(xEvent);
        yIn.setOnAction(yEvent);
        zoomIn.setOnAction(zoomEvent);
        PrecisionIn.setOnAction(precisionEvent);
        iterIn.setOnAction(iterEvent);
        pane.setHgap(0);
        pane.setVgap(0);
        pane.add(wIn,150,100);
        pane.add(wLabel,200,100);
        pane.add(hIn,150,150);
        pane.add(hLabel,200,150);
        pane.add(xIn,150,200);
        pane.add(xLabel,200,200);
        pane.add(yIn,150,250);
        pane.add(yLabel,200,250);
        pane.add(zoomIn,150,300);
        pane.add(zoomLabel,200,300);
        pane.add(PrecisionIn,150,350);
        pane.add(precisionLabel,200,350);
        pane.add(iterIn,150,400);
        pane.add(iterLabel,200,400);
        pane.add(Begin,100,100);
        pane.add(Stop,100,150);
        pane.add(Save,100,200); 
        pane.add(runtimeLabel,150,450);
        stage.setTitle("app");
        stage.setScene(scene2);
        stage.show();
        
    }

    public void SaveToFile(Scene scene){
        FileChooser fileChooser = new FileChooser();
    
        //Set extension filter
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
    
        //Prompt user to select a file
        File file = fileChooser.showSaveDialog(null);
    
        if(file != null){
            try {
                //Creates a new writable Image
                WritableImage WI = new WritableImage((int)scene.getWidth(), (int)scene.getHeight());
                scene.snapshot(WI);
                RenderedImage RI = SwingFXUtils.fromFXImage(WI, null);
                //Write the snapshot to the chosen file
                ImageIO.write(RI, "png", file);
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
}

class MandelThread extends Thread{
   private PixelWriter pix;
   private BigDecimal xStart, yStart, xSize, ySize, zoom, maxIter;
   private int Precision;
   private Group root1; 
   private Canvas canvas;
   private Scene MBScene;
   private boolean isDone=false;
   private boolean isRunning=false;
   public void setVars(PixelWriter pix,BigDecimal xStart,BigDecimal yStart,BigDecimal xSize, BigDecimal ySize,BigDecimal zoom,int Precision,BigDecimal maxIter,Group root1, Canvas canvas, Scene MBScene){
        this.pix=pix;
        this.xStart=xStart;
        this.yStart=yStart;
        this.xSize=xSize;
        this.ySize=ySize;
        this.zoom = zoom;
        this.Precision=Precision;
        this.maxIter=maxIter;
        this.root1=root1;
        this.canvas=canvas;
        this.MBScene=MBScene;
    }
    public void run(){
        isDone=false;
        isRunning=true;
        Mandelbrot mand = new Mandelbrot();
        isDone = mand.computeMandelBrot(pix, xStart, yStart, xSize, ySize,zoom,Precision,maxIter);
        root1.getChildren().add(canvas);
        
    }
    public void isRunningSetFalse(){
        isRunning=false;
    }
    public boolean checkIfRan(){
        return isRunning;
    }
    public boolean checkIfDone(){
        return isDone;
    }
    public Scene ReturnScene(){
        return MBScene;
    }
}
class Complex{
    //default precision
    private int precision;
    private MathContext mc = new MathContext(precision);
    private BigDecimal[] complexBD;
    //Allows Precision to be changed.
    public void setPrecision(int precision){
        this.precision=precision;
    }
    //Gets the real component
    public BigDecimal GetReal(){
        return complexBD[0];
    }
    //Gets the imaginary component
    public BigDecimal GetImaginary(){
        return complexBD[1];
    }
    // Gets The complex number
    public BigDecimal[] GetComplex(){
        return complexBD;
    }
    // finds the magnitude. Uses class bigDecimalExpansion to allow usage of sqrt.
    public BigDecimal Magnitude(){
        bigDecimalExpansion bex = new bigDecimalExpansion();
        bex.init(precision);
        BigDecimal x=bex.sqrt(((complexBD[0].pow(2)).add(complexBD[1].pow(2))).round(mc));
        
        return x;
    } 
    //constructs a complex number
    public void setComplex(BigDecimal Re,BigDecimal Im){
         complexBD = new BigDecimal[] {Re,Im};
         return;
    }
    // allows a double to be used in place of a big decimal
    public void setComplex(double Re,double Im){
        complexBD = new BigDecimal[] {new BigDecimal(Re,mc),new BigDecimal(Im,mc)};
        return;
    }
    //allows Bigdecimal to be set with string. used for maximum precision.
    public void setComplex(String Re,String Im){
        complexBD = new BigDecimal[] {new BigDecimal(Re,mc),new BigDecimal(Im,mc)};
        return;
    }
    //allows a complex to set an complex
    public void setComplex(BigDecimal[] complexBD){
        this.complexBD=complexBD;
    }
    
    //Outputs Complex numbeer to the terminal
    public void printlnComplexNumber(){
        System.out.println("(" + complexBD[0].toString() + " Re, " + complexBD[1].toString() + " Im)");
    }

    //Allows for addition of complex numbers
    public void add(BigDecimal[] complexBD){
        this.complexBD[0] = this.complexBD[0].add(complexBD[0]);
        this.complexBD[1] = this.complexBD[1].add(complexBD[1]);
    }
    //Allows for subtraction of complex numbers
    public void subtract(BigDecimal[] complexBD){
        this.complexBD[0] = this.complexBD[0].subtract(complexBD[0]);
        this.complexBD[1] = this.complexBD[1].subtract(complexBD[1]);
    }
    //Allows for multiplication of complex numbers
    public void multiply(BigDecimal[] complexBD){
        BigDecimal[] complexHld = new BigDecimal[2];
        complexHld[0] = this.complexBD[0].multiply(complexBD[0]).subtract(this.complexBD[1].multiply(complexBD[1]));
        complexHld[1] = this.complexBD[0].multiply(complexBD[1]).add(this.complexBD[1].multiply(complexBD[0])); 
        this.complexBD=complexHld;

    }
    //Allows for division of complex numbers
    public void divide(BigDecimal[] complexBD){
        BigDecimal[] complexHld = new BigDecimal[2];
        complexHld[0]=((this.complexBD[0].multiply(complexBD[0])).add(this.complexBD[1].multiply(complexBD[1]))).divide((complexBD[0].pow(2)).add(complexBD[1].pow(2)),precision,RoundingMode.HALF_EVEN);
        complexHld[1]=((this.complexBD[1].multiply(complexBD[0])).subtract(this.complexBD[0].multiply(complexBD[1]))).divide((complexBD[0].pow(2)).add(complexBD[1].pow(2)),precision,RoundingMode.HALF_EVEN);
        this.complexBD=complexHld;
    }
    
}
class Mandelbrot{
    public boolean computeMandelBrot(PixelWriter pix,BigDecimal xStart,BigDecimal yStart,BigDecimal xSize, BigDecimal ySize,BigDecimal zoom,int Precision,BigDecimal maxIter){
        // z=(z^2)+C
        Complex z = new Complex();
        Complex c = new Complex();
        c.setPrecision(Precision);
        z.setPrecision(Precision);
        MathContext mc = new MathContext(Precision);
        z.setComplex("0", "0");
        for (BigDecimal y = new BigDecimal("0"); (y.compareTo(ySize)<0); y=y.add(BigDecimal.ONE)) {
            for (BigDecimal x = new BigDecimal("0"); (x.compareTo(xSize)<0); x=x.add(BigDecimal.ONE)) {
                BigDecimal xVar=((x.divide(xSize.multiply(zoom),Precision,RoundingMode.HALF_EVEN)).subtract((BigDecimal.ONE).divide((new BigDecimal("2")).multiply(zoom),Precision,RoundingMode.HALF_EVEN))).subtract(xStart);
                BigDecimal yVar=((y.divide(ySize.multiply(zoom),Precision,RoundingMode.HALF_EVEN)).subtract((BigDecimal.ONE).divide((new BigDecimal("2")).multiply(zoom),Precision,RoundingMode.HALF_EVEN))).subtract(yStart);
                c.setComplex(xVar,yVar);
                z.setComplex("0", "0");
                for(BigDecimal i = new BigDecimal("0");i.compareTo(maxIter)<0;i=i.add(BigDecimal.ONE)){
                    z.setComplex(z.GetReal().round(mc),z.GetImaginary().round(mc));
                    z.multiply((z.GetComplex()));
                    z.setComplex(z.GetReal().round(mc),z.GetImaginary().round(mc));
                    z.add(c.GetComplex());
                    z.setComplex(z.GetReal().round(mc),z.GetImaginary().round(mc));
                    if((z.GetReal().pow(2).add(z.GetImaginary().pow(2))).compareTo(new BigDecimal("4"))>0){
                        pix.setColor((x.intValue()), (y.intValue()),Color.hsb((i.intValue())*360/maxIter.intValue(),1,1));
                        break;
                    }
                    if(i.compareTo(maxIter)==0){
                        pix.setColor((x.intValue()), (y.intValue()),Color.rgb(0,0,0));
                    }
                    
                }
                
            }
        }
        return true;

    }
}
class bigDecimalExpansion{
    private int precision;
    private BigDecimal LN2;
    public void init(int precision){
        this.precision=precision;
        this.LN2=lnOf2();
    }
    // integer factorial recursion formula
    public BigDecimal factorial(BigDecimal n){
        BigDecimal nHld=n;
        if(n.intValue()==0){
            return BigDecimal.ONE;
        }
        for(BigDecimal i=new BigDecimal("1");i.compareTo(n)<0;i=i.add(BigDecimal.ONE)) {
            nHld=nHld.multiply(i);
        }
        return nHld;
    }
    //combinate formula
    public BigDecimal nCr(BigDecimal n, BigDecimal r){
        return factorial(n).divide(factorial(r).multiply(factorial(n.subtract(r))));
    }
    // uses series representation to calclate ln(2)
    private BigDecimal lnOf2(){
        BigDecimal PrecisionBD = new BigDecimal(Integer.toString(precision));
        BigDecimal out = new BigDecimal("0");
        for(BigDecimal n = new BigDecimal("0");n.compareTo(PrecisionBD)<=0;n=n.add(BigDecimal.ONE)){
            out = out.add((BigDecimal.ONE).divide((((new BigDecimal("2")).multiply(n)).add(BigDecimal.ONE)).multiply((new BigDecimal("9")).pow(n.intValue())),PrecisionBD.intValue(), RoundingMode.HALF_EVEN));
        }
        out = out.multiply(new BigDecimal("2").divide(new BigDecimal("3"),PrecisionBD.intValue(),RoundingMode.HALF_EVEN));
        return out;
    }
    //Uses maclaurin series to calculate ln
    public BigDecimal ln(BigDecimal x){
        BigDecimal PrecisionBD = new BigDecimal(Integer.toString(precision));
        BigDecimal out = new BigDecimal("0");
        BigDecimal s = new BigDecimal("0");
        // reconfigures x to be a value between 0.5 and 1. saves the amount of times ln(2) will need to be added/subtracted for later
        while(x.compareTo(BigDecimal.ONE)>0){
            s = s.add(BigDecimal.ONE);
            x = x.multiply(new BigDecimal("0.5"));
            
        }
        while(x.compareTo(new BigDecimal("0.5"))<0){
            s = s.subtract(BigDecimal.ONE);
            x = x.multiply(new BigDecimal("2"));
        }
        //calculates ln(x), for x is greater than 0.5 but less than 1
        for(BigDecimal n = new BigDecimal("1");n.compareTo(PrecisionBD.multiply(new BigDecimal("5")))<=0;n=n.add(BigDecimal.ONE)){
            out = out.add((((BigDecimal.ONE).subtract(x)).pow(n.intValue())).divide(n,precision,RoundingMode.HALF_EVEN));
        }
        out=out.multiply(new BigDecimal("-1"));
        //reads ln(2) an s number of times. negative s means we subtract.
        out=out.add(LN2.multiply(s));
        return out;
    }
    //uses laws of logarithms to allow any base to be used
    public BigDecimal logb(BigDecimal x,BigDecimal b){
        return ln(x).divide(ln(b));

    }
    //maclaurin series representation of e to the power of x
    public BigDecimal epow(BigDecimal x){
        BigDecimal PrecisionBD = new BigDecimal(Integer.toString(precision));
        BigDecimal out = new BigDecimal("0");
        for(BigDecimal n = new BigDecimal("0");n.compareTo(PrecisionBD.multiply(new BigDecimal("5")))<=0;n=n.add(BigDecimal.ONE)){
            out = out.add((x.pow(n.intValue())).divide(factorial(n),precision,RoundingMode.HALF_EVEN));
        }
        return out;
    }
    // uses a logarithm to allow other bases to be used.
    public BigDecimal pow(BigDecimal x, BigDecimal b){
        return epow(x.multiply(ln(b)));
    }
    // shorthand for square root used to simplify pow(). for use in square roots only.
    public BigDecimal sqrt(BigDecimal x){
        return epow((new BigDecimal("0.5").multiply(ln(x))));
    }

}