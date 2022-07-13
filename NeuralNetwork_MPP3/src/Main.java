import models.NeuralNetwork;
import services.CsvToStringVectorsService;

public class Main {
    public static void main(String[] args) {

        String trainDataPath = args[0];
        String testDataPath = args[1];
        double alpha = Double.parseDouble(args[2]);
        int k = Integer.parseInt(args[3]);

        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.train(new CsvToStringVectorsService().csvToVectors(trainDataPath), k, alpha);

        neuralNetwork.testAll(testDataPath);
        neuralNetwork.showWeightsAndThetaOfPerceptrons();

    }
}
