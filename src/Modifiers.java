import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Modifiers {

    private Core core;
    public Modifiers(Core core) {
        this.core = core;
        initFrames();
        initPanels();
        initVariables();
        frame.pack();
    }

    public JFrame frame;

    private void initFrames() {
        frame = new JFrame("Variables");
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(300, 720));
        frame.setResizable(false);
        frame.setSize(frame.getPreferredSize());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width - 350, dim.height / 2 - frame.getSize().height / 2);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public JPanel variablePanel;
    public JSlider dayCountSlider;
    public JSlider populationSlider;
    public JSlider alphaSlider;
    public JSlider deathRateSlider;
    public JSlider betaSlider1;
    public JSlider betaSlider2;
    public JSlider thetaSlider1;
    public JSlider thetaSlider2;
    public JSlider chiSlider;
    public JSlider gammaSlider1;
    public JSlider gammaSlider2;
    public JSlider phiSlider;
    public JSlider PhiSlider;
    public JSlider lambdaSlider;
    public JSlider rhoSlider1;
    public JSlider rhoSlider2;
    public JSlider deltaSlider;

    private void initPanels() {
        variablePanel = new JPanel();
        FlowLayout variablePanelLayout = new FlowLayout();
        variablePanelLayout.setAlignment(FlowLayout.CENTER);
        variablePanelLayout.setHgap(20);
        variablePanelLayout.setVgap(0);
        variablePanel.setLayout(variablePanelLayout);
    }

    private void initVariables() {
        final DecimalFormat df = new DecimalFormat("#.######");

        JTextField dayCountLabel = createLabel("Day Count: [150]");
        dayCountSlider = new JSlider(20, 1000);
        dayCountSlider.setValue(150);
        dayCountSlider.addChangeListener(e -> {
            dayCountLabel.setText("Day Count: [" + dayCountSlider.getValue() + "]");
            core.updateValuesAndGraph();
        });

        JTextField populationLabel = createLabel("Population: [65,653]");
        populationSlider = new JSlider(30000, 10000000);
        populationSlider.setValue(65653);
        populationSlider.addChangeListener(e -> {
            populationLabel.setText("Population: [" + NumberFormat.getInstance(Locale.ENGLISH).format(populationSlider.getValue()) + "]");
            core.updateValuesAndGraph();
        });

        JTextField alphaLabel = createLabel("Temporary Immunity Rate: [0.01]");
        alphaSlider = new JSlider(1, 999);
        alphaSlider.setValue(10);
        alphaSlider.addChangeListener(e -> {
            alphaLabel.setText("Temporary Immunity Rate: [" + df.format(alphaSlider.getValue() / 1000.0) + "]");
            core.updateValuesAndGraph();
        });

        JTextField deathRateLabel = createLabel("Death Rate: [0%]");
        deathRateSlider = new JSlider(0, 1000);
        deathRateSlider.setValue(0);
        deathRateSlider.addChangeListener(e -> {
            deathRateLabel.setText("Death Rate: [" + df.format(deathRateSlider.getValue() / 10.0) + "%]");
            core.updateValuesAndGraph();
        });

        JTextField betaLabel = createLabel("Contact/Infection Rate of Transmission: [0.01] [0.3]");
        betaSlider1 = new JSlider(1, 999);
        betaSlider2 = new JSlider(1, 999);
        betaSlider1.setValue(10);
        betaSlider2.setValue(300);
        betaSlider1.addChangeListener(e -> {
            betaLabel.setText("Contact/Infection Rate of Transmission: [" + df.format(betaSlider1.getValue() / 1000.0) + "] [" + df.format(betaSlider2.getValue() / 1000.0) + "]");
            core.updateValuesAndGraph();
        });
        betaSlider2.addChangeListener(e -> {
            betaLabel.setText("Contact/Infection Rate of Transmission: [" + df.format(betaSlider1.getValue() / 1000.0) + "] [" + df.format(betaSlider2.getValue() / 1000.0) + "]");
            core.updateValuesAndGraph();
        });

        JTextField chiLabel = createLabel("Probability of Transmission: [0.4]");
        chiSlider = new JSlider(1, 999);
        chiSlider.setValue(400);
        chiSlider.addChangeListener(e -> {
            chiLabel.setText("Probability of Transmission: [" + df.format(chiSlider.getValue() / 1000.0) + "]");
            core.updateValuesAndGraph();
        });

        JTextField thetaLabel = createLabel("Exposed -> Infected: [0.01] [0.02]");
        thetaSlider1 = new JSlider(1, 999);
        thetaSlider2 = new JSlider(1, 999);
        thetaSlider1.setValue(10);
        thetaSlider2.setValue(20);
        thetaSlider1.addChangeListener(e -> {
            thetaLabel.setText("Exposed -> Infected: [" + df.format(thetaSlider1.getValue() / 1000.0) + "] [" + df.format(thetaSlider2.getValue() / 1000.0) + "]");
            core.updateValuesAndGraph();
        });
        thetaSlider2.addChangeListener(e -> {
            thetaLabel.setText("Exposed -> Infected: [" + df.format(thetaSlider1.getValue() / 1000.0) + "] [" + df.format(thetaSlider2.getValue() / 1000.0) + "]");
            core.updateValuesAndGraph();
        });

        JTextField gammaLabel = createLabel("Recovery Rate: [0.05] [0.06] ");
        gammaSlider1 = new JSlider(1, 999);
        gammaSlider2 = new JSlider(1, 999);
        gammaSlider1.setValue(50);
        gammaSlider2.setValue(60);
        gammaSlider1.addChangeListener(e -> {
            gammaLabel.setText("Recovery Rate: [" + df.format(gammaSlider1.getValue() / 1000.0) + "] [" + df.format(gammaSlider2.getValue() / 1000.0) + "]");
            core.updateValuesAndGraph();
        });
        gammaSlider2.addChangeListener(e -> {
            gammaLabel.setText("Recovery Rate: [" + df.format(gammaSlider1.getValue() / 1000.0) + "] [" + df.format(gammaSlider2.getValue() / 1000.0) + "]");
            core.updateValuesAndGraph();
        });

        JTextField phiLabel = createLabel("Infected -> Hospitalised Rate: [0.009]");
        phiSlider = new JSlider(1, 9999);
        phiSlider.setValue(90);
        phiSlider.addChangeListener(e -> {
            phiLabel.setText("Infected -> Hospitalised Rate: [" + df.format(phiSlider.getValue() / 10000.0) + "]");
            core.updateValuesAndGraph();
        });

        JTextField PhiLabel = createLabel("Quarantined -> Susceptible: [0.008]");
        PhiSlider = new JSlider(1, 9999);
        PhiSlider.setValue(80);
        PhiSlider.addChangeListener(e -> {
            PhiLabel.setText("Quarantined -> Susceptible: [" + df.format(PhiSlider.getValue() / 10000.0) + "]");
            core.updateValuesAndGraph();
        });

        JTextField lambdaLabel = createLabel("Quarantined -> Recovered: [0.0004]");
        lambdaSlider = new JSlider(1, 99999);
        lambdaSlider.setValue(40);
        lambdaSlider.addChangeListener(e -> {
            lambdaLabel.setText("Quarantined -> Recovered: [" + df.format(lambdaSlider.getValue() / 100000.0));
            core.updateValuesAndGraph();
        });

        JTextField rhoLabel = createLabel("Quarantined E -> I: [0.0714]");
        rhoSlider1 = new JSlider(1, 99999);
        rhoSlider1.setValue(7140);
        rhoSlider2 = new JSlider(1, 9999);
        rhoSlider2.setValue(20);
        rhoSlider1.addChangeListener(e -> {
            rhoLabel.setText("Quarantined E -> I: [" + df.format(rhoSlider1.getValue() / 100000.0) + "] [" + df.format(rhoSlider2.getValue() / 10000.0) + "]");
            core.updateValuesAndGraph();
        });
        rhoSlider2.addChangeListener(e -> {
            rhoLabel.setText("Quarantined E -> I: [" + df.format(rhoSlider1.getValue() / 100000.0) + "] [" + df.format(rhoSlider2.getValue() / 10000.0) + "]");
            core.updateValuesAndGraph();
        });

        JTextField deltaLabel = createLabel("External Cases (from holidays): [10]");
        deltaSlider = new JSlider(1, 10000);
        deltaSlider.setValue(10);
        deltaSlider.addChangeListener(e -> {
            deltaLabel.setText("External Cases (from holidays): [" + deltaSlider.getValue() + "]");
            core.updateValuesAndGraph();
        });

        createVariablesDisclaimer();
        variablePanel.add(createSectionLabel("Simulation Variables"));
        variablePanel.add(createVariablePanel(dayCountLabel, dayCountSlider));
        variablePanel.add(createVariablePanel(populationLabel, populationSlider));
        variablePanel.add(createSectionLabel("Virus Variables"));
        variablePanel.add(createVariablePanel(alphaLabel, alphaSlider));
        variablePanel.add(createVariablePanel(deathRateLabel, deathRateSlider));
        variablePanel.add(createVariablePanel(betaLabel, betaSlider1, betaSlider2));
        variablePanel.add(createVariablePanel(chiLabel, chiSlider));
        variablePanel.add(createVariablePanel(thetaLabel, thetaSlider1, thetaSlider2));
        variablePanel.add(createVariablePanel(gammaLabel, gammaSlider1, gammaSlider2));
        variablePanel.add(createVariablePanel(phiLabel, phiSlider));
        variablePanel.add(createVariablePanel(PhiLabel, PhiSlider));
        variablePanel.add(createVariablePanel(rhoLabel, rhoSlider1, rhoSlider2));
        variablePanel.add(createVariablePanel(deltaLabel, deltaSlider));

        variablePanel.setPreferredSize(new Dimension(frame.getPreferredSize().width - 20, 1200));
        JScrollPane scrollPane = new JScrollPane(variablePanel);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        frame.add(scrollPane);
    }

    private void createVariablesDisclaimer() {
        Font font = new Font("Helvetica Neue", Font.ITALIC, 10);
        JTextField field = createLabel("Hover over variables for info if confused.");
        field.setFont(font);
        field.setForeground(Color.gray);
        field.setPreferredSize(new Dimension(720, 20));
        JTextField field1 = createLabel("Scroll down for more variables...");
        field1.setFont(font);
        field1.setForeground(Color.gray);
        field1.setPreferredSize(new Dimension(720, 15));

        //variablePanel.add(field);
        variablePanel.add(field1);
    }

    private JTextField createSectionLabel(String label) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Helvetica Neue", Font.BOLD, 27));
        textField.setText(label);
        textField.setEditable(false);
        textField.setBorder(null);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBackground(frame.getBackground());
        textField.setPreferredSize(new Dimension(300, 50));
        return textField;
    }

    private JTextField createLabel(String string) {
        JTextField label = new JTextField(string);
        label.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        while (label.getFontMetrics(label.getFont()).stringWidth(string) > frame.getWidth() - 10) {
            label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize() - 1));
        }
        label.setEditable(false);
        label.setBackground(frame.getBackground());
        label.setBorder(null);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(frame.getWidth(), 50));
        return label;
    }

    private JPanel createVariablePanel(JTextField label, JSlider slider) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 80));
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        panel.setLayout(flowLayout);
        label.setPreferredSize(new Dimension(270, 30));
        slider.setPreferredSize(new Dimension(270, 20));
        panel.add(label);
        panel.add(slider);
        return panel;
    }

    private JPanel createVariablePanel(JTextField label, JSlider slider, JSlider slider2) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 80));
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        panel.setLayout(flowLayout);
        label.setPreferredSize(new Dimension(label.getFontMetrics(label.getFont()).stringWidth(label.getText() + 10), 30));
        slider.setPreferredSize(new Dimension(270, 20));
        slider2.setPreferredSize(new Dimension(270, 20));
        panel.add(label);
        panel.add(slider);
        panel.add(slider2);
        return panel;
    }

    public void updateSizes() {
        for (Component component : variablePanel.getComponents()) {
            if (component instanceof JTextField) {
                JTextField label = (JTextField) component;
                while (label.getFontMetrics(label.getFont()).stringWidth(label.getText()) > frame.getWidth() - 10) {
                    label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize() - 1));
                }
            }
        }
    }
}