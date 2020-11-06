import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Graph {

    private Core core;
    public Graph(Core core) {
        this.core=core;
        initFrame();
        initPanel();
        initGraph();
    }

    public JFrame frame;
    public JPanel initBackPane;
    public JPanel graph;

    private void initFrame() {
        frame = new JFrame("Model");
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(1280, 720));
        frame.setResizable(false);
        frame.setSize(frame.getPreferredSize());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((dim.width / 2 - frame.getSize().width / 2) - 170, dim.height / 2 - frame.getSize().height / 2);

        initBackPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                paintBackgroundPanel((Graphics2D) g, core.N);
            }
        };
        frame.add(initBackPane);
    }

    private void initPanel() {
        graph = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                System.out.println("Printing data to graph...");
                final long start = System.nanoTime();

                paintGraph((Graphics2D) g);

                final long end = System.nanoTime();
                final long duration = end - start;
                final double miliseconds = duration / 1000000.0;
                System.out.println("SIR model painted successfully in " + miliseconds + "ms.");
            }
        };
        initBackPane.add(graph);
        graph.setPreferredSize(new Dimension(1156, 650));
        graph.setSize(graph.getPreferredSize());
        graph.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));

        GroupLayout groupLayout = new GroupLayout(initBackPane);
        initBackPane.setLayout(groupLayout);

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, groupLayout.createSequentialGroup()
                                .addGap(0, 124, Short.MAX_VALUE)
                                .addComponent(graph, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(graph, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 70, Short.MAX_VALUE))
        );

        frame.pack();
    }

    ArrayList<ModelElement> elements = new ArrayList<>();

    public void initGraph() {
        ArrayList<Double> dead = new ArrayList<>();
        ArrayList<Double> recovered = core.R;

        elements = new ArrayList<>();
        elements.add(new ModelElement("Susceptibles", core.S, new Color(30, 190, 255)));
        elements.add(new ModelElement("Exposed", core.E, Color.orange));
        elements.add(new ModelElement("Infected (I1)", core.I1, Color.red.darker()));
        elements.add(new ModelElement("Infected (I2)", core.I2, Color.red));
        elements.add(new ModelElement("Quarantined", core.Q, Color.pink));
        elements.add(new ModelElement("Hospitalized", core.H, Color.black));
        elements.add(new ModelElement("Recovered", recovered, Color.green));
        elements.add(new ModelElement("Dead", dead, Color.green.darker()));
    }

    private void paintGraph(Graphics2D g) {
        paintBackground(g);
        keyX = graph.getWidth() - 110;
        keyY = 10;
        keyH = 20;
        keyW = 100;

        ArrayList<Double> dead = core.R;
        ArrayList<Double> recovered = (ArrayList<Double>) dead.clone();
        for (int i = 1; i < recovered.size(); i++) {
            recovered.set(i, recovered.get(i) * (1.0 - core.modifiers.deathRateSlider.getValue() / 1000.0));
            dead.set(i, dead.get(i) * (core.modifiers.deathRateSlider.getValue() / 1000.0));
        }

        elements.get(0).array = core.S;
        elements.get(1).array = core.E;
        elements.get(2).array = core.I1;
        elements.get(3).array = core.I2;
        elements.get(4).array = core.Q;
        elements.get(5).array = core.H;
        elements.get(6).array = recovered;
        elements.get(7).array = dead;

        for (ModelElement element : elements) {
            paintArray(g, element.array, element.color);
            paintKey(g, element.name, element.color);
        }

    }

    int keyX;
    int keyY;
    int keyH;
    int keyW;
    private void paintKey(Graphics2D g, String name, Color color) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 75));
        g.fillRect(keyX, keyY, keyW, keyH);
        g.setColor(Color.black);
        drawCenteredString(g, name, g.getFont());
        keyY += keyH;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    private void paintBackground(Graphics2D g) {
        int graphLines = 21;


        g.setColor(new Color(192, 192, 192, 120));

        int heightSpace = graph.getHeight() / graphLines;
        int x = 0;
        int y = 0;
        while (x < graph.getWidth()) {
            if (x < graph.getWidth()) {
                g.drawLine(x, 0, x, graph.getHeight());
                x = x + heightSpace;
            }
            if (y < graph.getHeight()) {
                g.drawLine(0, y, graph.getWidth(), y);
                y = y + heightSpace;
            }
        }
    }

    private void paintArray(Graphics2D g, ArrayList<Double> arr, Color color) {
        double spaceBetween = Double.parseDouble(graph.getWidth() + "") / (arr.size() - 1.0);

        Path2D path = new Path2D.Double();
        path.moveTo(0.0, graph.getHeight());

        int peakNum = (int) (core.N * 1.05);
        for (int n = 0; n < arr.size(); n++) {
            double i = arr.get(n);
            double pointHeight = ((i / peakNum) * graph.getHeight());
            double y = graph.getHeight() - pointHeight;
            double x = spaceBetween * n;

            path.lineTo(x, y);
        }

        /*
        LINE
         */
        g.setColor(color);
        g.setStroke(new BasicStroke(3));
        g.draw(path);

        /*
        AREA
         */
        path.lineTo(graph.getWidth(), graph.getHeight());
        path.lineTo(0, graph.getHeight());
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        g.fill(path);
    }

    private void paintBackgroundPanel(Graphics2D g, int population) {
        g.setColor(Color.darkGray);

        /*
        INDIVIDUALS - Y AXIS
         */
        g.setFont(new Font("Helvetica Neue", Font.ITALIC, 20));

        final int yAxisTextCount = 12;
        final int yAxisPosBetween = population / yAxisTextCount;

        g.drawString("0", 99, 647);
        int y = 647;
        for (int i = 0; i < yAxisTextCount; i++) {
            int n = i + 1;
            double cases = n * yAxisPosBetween;
            String casesStr = NumberFormat.getIntegerInstance().format(Math.round(cases));
            int stringWidth = g.getFontMetrics().stringWidth(casesStr);
            int x = 110 - stringWidth;
            y -= 50;
            g.drawString(casesStr, x, y);
        }

        g.setColor(Color.black);
        g.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        g.drawString("Individuals", 10, initBackPane.getHeight() - 35);

        /*
        DAYS - X AXIS
         */

        final int xAxisTextCount = 12;
        final int xAxisSpaceBetween = core.totalDays / xAxisTextCount;
        g.setFont(new Font("Helvetica Neue", Font.ITALIC, 20));
        g.drawString("0", 120, initBackPane.getHeight() - 20);
        int x = 105;
        for (int i = 0; i < xAxisTextCount; i++) {
            int n = i + 1;
            double days = n * xAxisSpaceBetween;
            String daysStr = NumberFormat.getIntegerInstance().format(Math.round(days));
            int stringWidth = g.getFontMetrics().stringWidth(daysStr);
            x += stringWidth + 53;
            g.drawString(daysStr, x, initBackPane.getHeight() - 20);
        }

        g.setFont(new Font("Helvetica Neue", Font.BOLD, 15));
        AffineTransform at = new AffineTransform();
        AffineTransform previousAT = g.getTransform();
        at.setToRotation(Math.toRadians(90), 80, 100);
        g.setTransform(at);
        g.drawString("Days", graph.getHeight() - 15, 85);

        /*
        RESET
         */
        g.setTransform(previousAT);
    }

    public void drawCenteredString(Graphics g, String text, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = keyX + (keyW - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, keyY + 15);
    }

}

class ModelElement {
    public String name;
    public ArrayList<Double> array;
    public Color color;

    public ModelElement(String name, ArrayList<Double> array, Color color) {
        this.name=name;
        this.array=array;
        this.color=color;
    }
}