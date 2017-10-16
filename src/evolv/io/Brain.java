package evolv.io;

import evolv.io.creature.components.Eye;
import evolv.io.creature.Creature;
import evolv.io.nn.NN;
import evolv.io.nn.activation.Activation;
import evolv.io.nn.structure.NetworkStructure;

public class Brain {

    private static final int BRAIN_HEIGHT = Configuration.NUM_EYES * Eye.EYE_SENSOR_ASPECT_COUNT + 2;
    private static final String[] INPUT_LABELS = new String[BRAIN_HEIGHT];
    private static final String[] OUTPUT_LABELS = new String[7];
    static final float NEURON_SPACING = 1.1f;
    static final float NEURON_OFFSET_X = -0.85f;
    static final float NEURON_OFFSET_Y = 0.20f;

    private final EvolvioColor evolvioColor;
    // Brain
    private final NN network;
    private double[] lastCalculation = new double[7];

    static {
        // input
        INPUT_LABELS[0] = "Size";
        INPUT_LABELS[1] = "M Hue";

        for (int i = 2; i < Configuration.NUM_EYES * 3 + 2; i += 3) {
            INPUT_LABELS[i] = "Hue " + i / 3;
            INPUT_LABELS[i + 1] = "Sat " + ((i - 1) / 3);
            INPUT_LABELS[i + 2] = "Bri " + ((i - 1) / 3);
        }

        // output
        OUTPUT_LABELS[0] = "Body Hue";
        OUTPUT_LABELS[1] = "Accelerate";
        OUTPUT_LABELS[2] = "Turn";
        OUTPUT_LABELS[3] = "Eat";
        OUTPUT_LABELS[4] = "Fight";
        OUTPUT_LABELS[5] = "Procreate";
        OUTPUT_LABELS[6] = "Mouth Hue";

        // TODO do we need a memory and const output?
        // memory
        for (int i = 0; i < Configuration.MEMORY_COUNT; i++) {
            INPUT_LABELS[i + Configuration.NUM_EYES * 3 + 2] = "Mem.";
        }

        // TODO is this the bias?
        // const
        INPUT_LABELS[BRAIN_HEIGHT - 1] = "Const.";
    }

    public Brain(EvolvioColor evolvioColor, NN network) {
        this.evolvioColor = evolvioColor;
        // initialize brain
        if (network == null) {
            this.network = getStructure().createNetwork();
        } else {
            this.network = network;
        }
    }

    private NetworkStructure getStructure() {
        NetworkStructure net = new NetworkStructure(BRAIN_HEIGHT, 7);
        net.addInputLayer(Activation.SIGMOID);
        net.addHiddenLayer(15, Activation.SIGMOID);
        net.addOutputLayer(Activation.TANH);
        return net;
    }

    public Brain evolve(Creature parent) {
        NN otherNet = parent.getBrain().getNetwork();
        NN newNet = network.reproduce(otherNet);
        return new Brain(this.evolvioColor, newNet);
    }

    public void reward(double[] values) {
        network.reward(values, lastCalculation);
    }

    public NN getNetwork() {
        return network;
    }

    public void draw(float scaleUp, int mX, int mY) {
        final float neuronSize = 0.4f;
        this.evolvioColor.noStroke();
//        this.evolvioColor.fill(0, 0, 0.4f);
//        this.evolvioColor.rect((-3.2f - neuronSize) * scaleUp, -neuronSize * scaleUp,
//                (3.8f + Configuration.BRAIN_WIDTH + neuronSize * 2) * scaleUp,
//                (BRAIN_HEIGHT + neuronSize * 2) * scaleUp);

        this.evolvioColor.ellipseMode(EvolvioColor.RADIUS);
        this.evolvioColor.strokeWeight(2);
        this.evolvioColor.textSize(0.2f * scaleUp);
        this.evolvioColor.fill(0, 0, 1);
        evolvioColor.textAlign(EvolvioColor.CENTER);
        network.draw(evolvioColor, scaleUp, NEURON_OFFSET_X, NEURON_OFFSET_Y);
    }

    public void input(double[] inputs) {
        lastCalculation = network.calculate(inputs);
    }

    public double[] outputs() {
        return lastCalculation;
    }
}
