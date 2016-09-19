package com.dukescript.jstitch;

import com.dukescript.api.canvas.GraphicsContext2D;
import com.dukescript.api.canvas.Style;
import com.dukescript.jstitch.DSTModel.Stitch;
import com.dukescript.jstitch.js.SVGPathTool;
import com.dukescript.jstitch.js.SVGPathTool.Path.MoveTo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.Property;

@Model(
        className = "Loader", properties = {
            @Property(name = "file", type = String.class),
            @Property(name = "idx", type = int.class)
        }, targetId = ""
)
final class DataModel {

    static Loader LOADER;

    /**
     * Called when the page is ready.
     */
    static void onPageLoad() throws Exception {
        LOADER = new Loader();
        LOADER.applyBindings();
    }

    @Function
    public static void convert(Loader loader) {
        List<SVGPathTool.Path> paths = SVGPathTool.getPaths();
        DSTModel design = new DSTModel();
        String motiv = "Duke";
        design.setMotiv(motiv);
        final int colors = 0;
        design.setColors(colors);
        final int h = 100;
        design.setHeight(h);
        final int mD = 100;
        design.setMaxDist(mD);
        final int st = 48;
        design.setNumStitches(st);
        final int w = 100;
        design.setWidth(w);
        final int xMax = 30;
        design.setxMax(xMax);
        final int xMin = 30;
        design.setxMin(xMin);
        final int yMax = 30;
        design.setyMax(yMax);
        final int yMin = 30;
        design.setyMin(yMin);
        design.setPd("******");

        ArrayList<Stitch> stitches = new ArrayList<>();
        int i = 0;
        int x = 0, y = 0;
        for (SVGPathTool.Path path : paths) {
            System.out.println("###path");
            List<SVGPathTool.Path.Segment> segments = path.getSegments();
            Vector p0 = null;
            for (SVGPathTool.Path.Segment segment : segments) {
                System.out.println("###segment");
                if (segment instanceof MoveTo) {
                    p0 = ((MoveTo) segment).getP0();
                    int distX = x - (int) p0.getX();
                    int distY = y - (int) p0.getY();
                    stitches.add(new DSTModel.Stitch(true, false, distX, distY));
                    System.out.format("distX: %d distY: %d x1: %d y1:%d x2:%d y2: %d\n", distX, distY, x, y, (int) p0.getX(), (int) p0.getY());
                    x = (int) p0.getX();
                    y = (int) p0.getY();
                } else {
                    SVGPathTool.Path.CurveTo curve = (SVGPathTool.Path.CurveTo) segment;
                    BezierCurve bezierCurve = new BezierCurve(p0, curve.getP0(), curve.getP1(), curve.getP2(), 2);//        List<Vector> satinPoints = bezierCurve.calculateSatinStitch(25);
                    List<Vector> satinPoints = bezierCurve.calculateSatinStitch(3);
                    g.setFillStyle(new Style.Color("#0000ff"));
                    g.beginPath();
                    g.moveTo(bezierCurve.getStart().getX(), bezierCurve.getStart().getY());
                    int distX = x - (int) bezierCurve.getStart().getX();
                    int distY = y - (int) bezierCurve.getStart().getY();
                    stitches.add(new DSTModel.Stitch(false, false,
                            distX, distY
                    ));
                    System.out.format("distX: %d distY: %d x1: %d y1:%d x2:%d y2: %d\n", distX, distY, x, y, (int) bezierCurve.getStart().getX(), (int) bezierCurve.getStart().getY());
                    x = (int) bezierCurve.getStart().getX();
                    y = (int) bezierCurve.getStart().getY();
                    for (Vector vector : satinPoints) {
                        g.lineTo((float) vector.getX(), (float) vector.getY());
                        distX = x - (int) vector.getX();
                        distY = y - (int) vector.getY();
                        stitches.add(new DSTModel.Stitch(false, false,
                                distX,
                                distY));
                        System.out.format("distX: %d distY: %d x1: %d y1:%d x2:%d y2: %d\n", distX, distY, x, y, (int) vector.getX(), (int) vector.getY());
                        x = (int) vector.getX();
                        y = (int) vector.getY();

                    }
                    g.lineTo(bezierCurve.getEnd().getX(), bezierCurve.getEnd().getY());
                    distX = x - (int) bezierCurve.getEnd().getX();
                    distY = y - (int) bezierCurve.getEnd().getY();
                    stitches.add(new DSTModel.Stitch(false, false,
                            distX, distY
                    ));
                    System.out.format("distX: %d distY: %d x1: %d y1:%d x2:%d y2: %d\n", distX, distY, x, y, (int) bezierCurve.getEnd().getX(), (int) bezierCurve.getEnd().getY());
                    x = (int) bezierCurve.getEnd().getX();
                    y = (int) bezierCurve.getEnd().getY();

                    g.stroke();
                    p0 = curve.getP2();
                }
            }

        }

        stitches.add(new DSTModel.Stitch(false, false, 0, 0));
        stitches.add(new DSTModel.Stitch(true, false, 0, 0));
        stitches.add(new DSTModel.Stitch(true, false, 95, 102));
        stitches.add(new DSTModel.Stitch(false, true, 0, 0));
        design.setStitches(stitches.toArray(new Stitch[stitches.size()]));
        design.correctMaxMin();
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream("Duke.DST");
            design.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readDukeFromSVGPaths() {
        List<SVGPathTool.Path> paths = SVGPathTool.getPaths();
        g.setFillStyle(new Style.Color("#ffffff"));
        g.fillRect(0, 0, 200, 200);
        g.setFillStyle(new Style.Color("#000000"));
        g.setStrokeStyle(new Style.Color("#000000"));

        for (SVGPathTool.Path path : paths) {
            List<SVGPathTool.Path.Segment> segments = path.getSegments();

            Vector p0 = null;

            for (SVGPathTool.Path.Segment segment : segments) {
                if (segment instanceof MoveTo) {
                    p0 = ((MoveTo) segment).getP0();
                } else {
                    SVGPathTool.Path.CurveTo curve = (SVGPathTool.Path.CurveTo) segment;
                    BezierCurve bezierCurve = new BezierCurve(p0, curve.getP0(), curve.getP1(), curve.getP2(), 2);//        List<Vector> satinPoints = bezierCurve.calculateSatinStitch(25);
                    List<Vector> satinPoints = bezierCurve.calculateSatinStitch(3);
                    g.setFillStyle(new Style.Color("#0000ff"));
                    g.beginPath();
                    g.moveTo(bezierCurve.getStart().getX(), bezierCurve.getStart().getY());
                    for (Vector vector : satinPoints) {
                        g.lineTo((float) vector.getX(), (float) vector.getY());
                    }
                    g.lineTo(bezierCurve.getEnd().getX(), bezierCurve.getEnd().getY());
                    g.stroke();
                    p0 = curve.getP2();
                }
            }

        }
    }

    public static void satinStitchSample() {

        final BezierCurve bezierCurve = new BezierCurve(new Vector(10, 30),
                new Vector(500, 20),
                new Vector(300, 400),
                new Vector(60, 400),
                2);

        drawBezierCurve(bezierCurve);
        List<Vector> satinPoints = bezierCurve.calculateSatinStitch(25);
        g.setFillStyle(new Style.Color("#0000ff"));
        g.beginPath();
        g.moveTo(bezierCurve.getStart().getX(), bezierCurve.getStart().getY());
        for (Vector vector : satinPoints) {
            g.lineTo((float) vector.getX(), (float) vector.getY());
        }
        g.lineTo(bezierCurve.getEnd().getX(), bezierCurve.getEnd().getY());
        g.stroke();
    }

    @Function
    static void uploadFile(Loader model, String data) {
        System.out.println("File: " + data);
    }

    @Function
    static void loadFile(Loader model) {
        try {
            read(g, model.getFile());
        } catch (IOException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static final GraphicsContext2D g = GraphicsContext2D.getOrCreate("stitch");

    /**
     * @param args the command line arguments
     */
    public static void read(GraphicsContext2D g2d, String fileName) throws IOException {
        final BrwsrCtx ctx = BrwsrCtx.findDefault(DataModel.class);
        File file = new File(fileName);
        FileInputStream fs = new FileInputStream(file);
        DSTModel design = DSTModel.readDesign(fs);
        int min = Math.min(g.getWidth(), g.getHeight());
        double scale = Math.min((min / design.getMaxDist()), 1.0) - .2;
        g2d.setTransform(scale, 0, 0, scale, design.getWidth() - design.getxMax(), design.getHeight() - design.getyMax());
        g2d.setLineWidth(1);
        int x = 0, y = 0;
        DSTModel.Stitch[] stitches = design.getStitches();
        final StitchAnimation stitchAnimation = new StitchAnimation(stitches, g2d);
        timer = new Timer();
        final Runnable stitcher = new Runnable() {
            @Override
            public void run() {
                if (!stitchAnimation.stitch(g)) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ctx.execute(stitcher);
            }
        }, 100, 10);

//        for (DSTModel.Stitch stitch : stitches) {
//            x += stitch.getIncX();
//            y -= stitch.getIncY();
//            if (stitch.isColorChange() || stitch.isJump()) {
//                g2d.stroke();
//                g2d.beginPath();
//                g2d.moveTo(x, y);
//            } else {
//                g2d.lineTo(x, y);
//            }
//
//        }
    }
    private static Timer timer;

    private static void drawBezierCurve(BezierCurve curve) {
        List<Vector> points = curve.getPoints();
        g.beginPath();
        for (Vector point : points) {
            g.fillCircle((float) point.getX(), (float) point.getY(), 2);
        }
        g.setFillStyle(new Style.Color("#ff0000"));
        g.fillCircle((float) curve.getStart().getX(), (float) curve.getStart().getY(), 2);
        g.fillCircle((float) curve.getControl1().getX(), (float) curve.getControl1().getY(), 2);
        g.fillCircle((float) curve.getControl2().getX(), (float) curve.getControl2().getY(), 2);
        g.fillCircle((float) curve.getEnd().getX(), (float) curve.getEnd().getY(), 2);

    }

    private static void drawBezierCurve(Vector vector, Vector vector0, Vector vector1, Vector vector2) {
        double max = 50;
        double seg = 1 / max;
        Vector start = BezierMath.calculateBezierPoint(0, vector, vector0, vector1, vector2);
//        g.beginPath();
//        g.moveTo(vector.getX(), vector.getY());
//        g.lineTo(vector0.getX(), vector0.getY());
//        g.stroke();
//        g.beginPath();
//        g.moveTo(vector1.getX(), vector1.getY());
//        g.lineTo(vector2.getX(), vector2.getY());
//        g.stroke();
//        g.fillCircle((float) vector.getX(), (float) vector.getY(), 4);
//        g.fillCircle((float) vector0.getX(), (float) vector0.getY(), 4);
//        g.fillCircle((float) vector1.getX(), (float) vector1.getY(), 4);
//        g.fillCircle((float) vector2.getX(), (float) vector2.getY(), 4);

        g.beginPath();
        g.moveTo(start.getX(), start.getY());
        for (int i = 0; i <= max; i++) {
            double t = i * seg;
            Vector calc = BezierMath.calculateBezierPoint(t, vector, vector0, vector1, vector2);
            g.lineTo(calc.getX(), calc.getY());
//            g.fillCircle((float) calc.getX(), (float) calc.getY(), 2);
        }
        g.stroke();
    }

}
