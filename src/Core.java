import javax.swing.*;
import java.util.ArrayList;

public class Core {

    public static void main(String[] args) {
        new Core();
    }

    public Modifiers modifiers;
    public Graph graph;

    public Core() {
        createData();
        graph = new Graph(this);
        modifiers = new Modifiers(this);

        SwingUtilities.invokeLater(() -> {
            graph.frame.setVisible(true);
            modifiers.frame.setVisible(true);
        });
    }

    int totalDays = 150; // How many days/times to loop
    int N = 65653; // population
    double alpha = 0.01; // immunity rate
    double beta1 = 0.1; // contact rate
    double beta2 = 0.3;
    double chi = 0.4; // probability of transmission per contract
    double theta1 = 0.01; // transition rate of exposure individuals to the infected class
    double theta2 = 0.02;
    double gamma1 = 0.05; // recovery rate of symptomatic individual to recovered
    double gamma2 = 0.06;
    double phi = 0.009; // rate of infectious with symptoms to hospitalised
    double Phi = 0.008; // recovered rate of quarantined individuals
    double lambda = 0.0004; // rate of quarantined class to the recovered class
    double rho1 = 1.0 / 14.0; // transisistion rate of quarantined exposed between the quarantined infected
    double rho2 = 0.002;
    double delta = 1; // external input from foreign countries
    double deathRate = 0.2;

    ArrayList<Double> S = new ArrayList<>();
    ArrayList<Double> E = new ArrayList<>();
    ArrayList<Double> I1 = new ArrayList<>();
    ArrayList<Double> I2 = new ArrayList<>();
    ArrayList<Double> R = new ArrayList<>();
    ArrayList<Double> H = new ArrayList<>();
    ArrayList<Double> Q = new ArrayList<>();

    private void createData() {
        // Total susceptibles acording to other variables
        final int E0 = 10;
        final int R0 = 0;
        final int S0 = N - E0 - R0;

        // Assigning these settings to ArrayLists
        S.add((double) S0);
        E.add(100.0);
        I1.add(0.0);
        I2.add(0.0);
        R.add(0.0);
        H.add(0.0);
        Q.add(0.0);

        System.out.println("----------------------------------");
        System.out.println("Calculating new SIR model data...");
        final long start = System.nanoTime();

        for (int day = 1; day < totalDays + 1; day++) {
            double[] derivative = deriv(day);
            S.add(derivative[0]);
            E.add(derivative[1]);
            I1.add(derivative[2]);
            I2.add(derivative[3]);
            R.add(derivative[4]);
            H.add(derivative[5]);
            Q.add(derivative[6]);
        }

        final long end = System.nanoTime();
        final long duration = end - start;
        final double milliseconds = duration / 1000000.0;
        System.out.println("SIR model calculated successfully in " + milliseconds + "ms.");
    }

    /* SEIRS W/ VITAL DYNAMICS
        double dS = (beta * S.get(day) * I.get(day)) / N;
        double newS = S.get(day) + ((u * N) -dS -(xi * R.get(day)) - (v * S.get(day)));
        double newE = E.get(day) + (dS - (a * E.get(day)) - (v * E.get(day)));
        double newI = I.get(day) + ((a * E.get(day)) - (gamma * I.get(day)) - (v * I.get(day)));
        double newR = R.get(day) + ((gamma * I.get(day)) - (xi * R.get(day)) - (v * R.get(day)));
     */

    private double[] deriv(int day) {
        day = day - 1;

        double newS = S.get(day) + (-(S.get(day)/N) * (beta1 * I1.get(day) + beta2 * I2.get(day) + chi * E.get(day)) + rho1 * Q.get(day) - rho2 * S.get(day) + alpha * R.get(day));
        double newE = E.get(day) + ((S.get(day)/N) * (beta1 * I1.get(day) + beta2 * I2.get(day) + chi * E.get(day)) - theta1 * E.get(day) - theta2 * E.get(day));
        double newI1 = I1.get(day) + (theta1 * E.get(day) - gamma1 * I1.get(day));
        double newI2 = I2.get(day) + (theta2 * E.get(day) - gamma2 * I2.get(day) - phi * I2.get(day) + lambda * (delta + Q.get(day)));
        double newR = R.get(day) + (gamma1 * I1.get(day) + gamma2 * I2.get(day) + Phi * H.get(day) - alpha * R.get(day));
        double newH = H.get(day) + (phi * I2.get(day) - Phi * H.get(day));
        double newQ = Q.get(day) + (delta + rho2 * S.get(day) - lambda * (delta + Q.get(day)) - rho1 * Q.get(day));
        return new double[] {newS, newE, newI1, newI2, newR, newH, newQ};
    }

    public void updateValuesAndGraph() {
        N = modifiers.populationSlider.getValue();
        totalDays = modifiers.dayCountSlider.getValue();
        alpha = modifiers.alphaSlider.getValue() / 1000.0;
        beta1 = modifiers.thetaSlider1.getValue() / 1000.0;
        beta2 = modifiers.thetaSlider2.getValue() / 1000.0;
        chi = modifiers.chiSlider.getValue() / 1000.0;
        theta1 = modifiers.thetaSlider1.getValue() / 1000.0;
        theta2 = modifiers.thetaSlider2.getValue() / 1000.0;
        gamma1 = modifiers.gammaSlider1.getValue() / 1000.0;
        gamma2 = modifiers.gammaSlider2.getValue() / 1000.0;
        phi = modifiers.phiSlider.getValue() / 10000.0;
        Phi = modifiers.PhiSlider.getValue() / 10000.0;
        lambda = modifiers.lambdaSlider.getValue() / 100000.0;
        rho1 = modifiers.rhoSlider1.getValue() / 100000.0;
        rho2 = modifiers.rhoSlider2.getValue() / 10000.0;
        delta = modifiers.deltaSlider.getValue();

        S = new ArrayList<>();
        E = new ArrayList<>();
        I1 = new ArrayList<>();
        I2 = new ArrayList<>();
        R = new ArrayList<>();
        H = new ArrayList<>();
        Q = new ArrayList<>();

        createData();
        graph.frame.repaint();
        modifiers.updateSizes();
    }

}