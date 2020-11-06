# EpidemiologyModeller

EpidemiologyModeller is a project on representing viruses based on their properties. 

![](https://i.imgur.com/L13vLzZ.png)

*Note: Settings and default variables in screenshot above have been turned up to showcase the graph better. The model does not include real life variables such as mask usage, social distancing, etc. This is a virus modeller, not a scenario simulator.*

## Features

The model is based off the SEIR model but has been adapted to include the following classes:

* **Susceptible** (those who susceptible to catch the virus)
* **Exposed** (those who have the virus but are not infected)
* **Infectious** (mild)
* **Infectious** (severe)
* **Quarantined**
* **Hospitalised**
* **Recovered**
* **Dead**

The program has the ability to **change virus variables**, **change day count**, and then **visualise** the model on the graph.

## How It works

### Algorithm

The equations for the model are:

![](https://i.imgur.com/WydASeg.png)

In code this is represented by: *(see variables below)*

```java
double newS = S.get(day) + (-(S.get(day)/N) * (beta1 * I1.get(day) + beta2 * I2.get(day) + chi * E.get(day)) + rho1 * Q.get(day) - rho2 * S.get(day) + alpha * R.get(day));
double newE = E.get(day) + ((S.get(day)/N) * (beta1 * I1.get(day) + beta2 * I2.get(day) + chi * E.get(day)) - theta1 * E.get(day) - theta2 * E.get(day));
double newI1 = I1.get(day) + (theta1 * E.get(day) - gamma1 * I1.get(day));
double newI2 = I2.get(day) + (theta2 * E.get(day) - gamma2 * I2.get(day) - phi * I2.get(day) + lambda * (delta + Q.get(day)));
double newR = R.get(day) + (gamma1 * I1.get(day) + gamma2 * I2.get(day) + Phi * H.get(day) - alpha * R.get(day));
double newH = H.get(day) + (phi * I2.get(day) - Phi * H.get(day));
double newQ = Q.get(day) + (delta + rho2 * S.get(day) - lambda * (delta + Q.get(day)) - rho1 * Q.get(day));
```

Which are held in the following arrays:

```java
ArrayList<Double> S = new ArrayList<>();
ArrayList<Double> E = new ArrayList<>();
ArrayList<Double> I1 = new ArrayList<>();
ArrayList<Double> I2 = new ArrayList<>();
ArrayList<Double> R = new ArrayList<>();
ArrayList<Double> H = new ArrayList<>();
ArrayList<Double> Q = new ArrayList<>();
```

You can then create the following loop to create the data:

```java
double population = 65000;
S.add(population - 10.0);
E.add(10.0);
I1.add(0.0);
I2.add(0.0);
R.add(0.0);
H.add(0.0);
Q.add(0.0);

final int totalDays = 150;

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
```

### Variables

| Parameter(s)            | Description                                                  | Code Variable Name | Default Value  |
| ----------------------- | ------------------------------------------------------------ | ------------------ | -------------- |
| $a$                     | Temporary immunity rate                                      | alpha              |  0.01          |
| $\beta_1, $ $\beta_2$   | Contact rate                                                 | beta1, beta2       |  0.1, 0.3      |
| 𝜒                       | Probability of transmission per contract from exposed individuals | chi                |  0.4           |
| $\theta_1, $ $\theta_2$ | Transition rate of exposed individuals to the infected classes | theta1, theta2     |  0.01, 0.02    |
| $\gamma_1, \gamma_2$    | Recovery rate of symptomatic infected individuals to recovered | gamma1, gamma2     |  0.05, 0.06    |
| 𝜑                       | Rate of infectious with symptoms to hospitalised             | phi                |  0.009         |
| $\Phi$                  | Recovered rate of quarantined individuals                    | Phi                |  0.008         |
| $\lambda$               | Rate of the quarantined class to the recovered class         | lambda             |  0.0004        |
| $\rho_1, \rho_2$        | Transition rate of quarantined exposed between the quarantined infected class and the wider community | rho1, rho2         |  0.071, 0.002  |
| $\Delta$                | External input from the foreign populations                  | delta              |  10            |
| $N$                     | Population                                                   | N                  |  65,653        |

In code these defaults are as shown:

```java
int totalDays = 150; // How many days/times to loop
    int N = 65653; // population
    double alpha = 0.01; // immunity rate
    double beta1 = 0.1; // contract rate
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
```

### Graphing

Graphing is very simple. When initialising we create an array to hold each class, their name and colours:

```java
ArrayList<ModelElement> elements = new ArrayList<>();

public void initGraph() {
  	 elements = new ArrayList<>();
    elements.add(new ModelElement("Susceptibles", core.S, new Color(30, 190, 255)));
    elements.add(new ModelElement("Exposed", core.E, Color.orange));
    elements.add(new ModelElement("Infected (I1)", core.I1, Color.red.darker()));
    elements.add(new ModelElement("Infected (I2)", core.I2, Color.red));
    elements.add(new ModelElement("Quarantined", core.Q, Color.pink));
    elements.add(new ModelElement("Hospitalized", core.H, Color.black));
    elements.add(new ModelElement("Recovered", core.R, Color.green));
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
```

Then whenever the graph is called to be painted (by overriding the paintComponent method in JPanel):

```java
keyX = graph.getWidth() - 110;
keyY = 10;
keyH = 20;
keyW = 100;

elements.get(0).array = core.S;
elements.get(1).array = core.E;
elements.get(2).array = core.I1;
elements.get(3).array = core.I2;
elements.get(4).array = core.Q;
elements.get(5).array = core.H;
elements.get(6).array = core.R;
  
for (ModelElement element : elements) {
  	paintArray(g, element.array, element.color);
   paintKey(g, element.name, element.color);
}
```

```java
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
```

```java
int keyX;
int keyY;
int keyH;
int keyW;
private void paintKey(Graphics2D g, String name, Color color) {
    g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 75));
    g.fillRect(keyX, keyY, keyW, keyH);
    g.setColor(Color.black);
    drawCenteredString(g, name, g.getFont());
    keyY += keyH;
}
```

