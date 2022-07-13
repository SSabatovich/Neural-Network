package models;

import services.CsvToStringVectorsService;
import services.VectorNormService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NeuralNetwork {
    private Perceptron[] perceptrons;

    private void initialize(List<String[]> trainVectors) {
        List<String> possibleTypes = trainVectors.stream().map(e -> e[e.length - 1]).distinct().collect(Collectors.toList());
        int weightsCount = trainVectors.get(0).length - 1;

        perceptrons = new Perceptron[possibleTypes.size()];
        for (int i = 0; i < perceptrons.length; i++) {
            perceptrons[i] = new Perceptron(possibleTypes.get(i), weightsCount);
        }
    }

    public void train(List<String[]> trainVectors, int k, double alpha) {
        initialize(trainVectors);

        VectorNormService normService = new VectorNormService();

        for (int i = 0; i < k; i++) {

            for (String[] stringVec : trainVectors) {

                Double[] vector = new Double[stringVec.length - 1];
                String type = stringVec[stringVec.length - 1];

                for (int j = 0; j < stringVec.length - 1; j++) {
                    vector[j] = Double.parseDouble(stringVec[j]);
                }

                vector = normService.normalize(vector);


                for (int p = 0; p < perceptrons.length; p++) {

                    int d = 0;

                    if (perceptrons[p].getLanguage().equals(type)) {
                        d = 1;
                    }

                    double net = 0;


                    for (int j = 0; j < perceptrons[p].getWeights().length; j++) {
                        net += perceptrons[p].getWeights()[j] * vector[j];
                    }

                    net -= perceptrons[p].getTheta();

                    double y;

                    if (net < -1) {
                        y = -1;
                    } else if (net > 1) {
                        y = 1;
                    } else {
                        y = net;
                    }


                    if (d != y) {

                        for (int j = 0; j < perceptrons[p].getWeights().length; j++) {
                            perceptrons[p].getWeights()[j] = perceptrons[p].getWeights()[j] + alpha * vector[j] * (d - y) * (1 - y) * (1 - y) / 2;
                        }

                        perceptrons[p].setTheta(perceptrons[p].getTheta() - alpha * (d - y) * (1 - y) * (1 - y) / 2);
                    }
                }
            }
        }

    }

    public String test(Double[] vector) {

        double[] activationTable = new double[perceptrons.length];

        for (int p = 0; p < perceptrons.length; p++) {

            double net = 0;

            for (int i = 0; i < perceptrons[p].getWeights().length; i++) {
                net += perceptrons[p].getWeights()[i] * vector[i];
            }

            net -= perceptrons[p].getTheta();

            double y;

            if (net < -1) {
                y = -1;
            } else if (net > 1) {
                y = 1;
            } else {
                y = net;
            }

            activationTable[p] = y;
        }

        double max = Arrays.stream(activationTable).max().orElse(0);

        for (int p = 0; p < perceptrons.length; p++) {
            if (activationTable[p] == max) {
                return perceptrons[p].getLanguage();
            }
        }

        return null;
    }


    public void testAll(String testPath) {
        List<String[]> vectorsToTest = new CsvToStringVectorsService().csvToVectors(testPath);
        VectorNormService normService = new VectorNormService();

        double successCount = 0;
        double allTests = 0;

        int[][] matrix = new int[perceptrons.length][perceptrons.length];


        for (String[] stringVec : vectorsToTest) {
            Double[] vector = new Double[stringVec.length - 1];
            String type = stringVec[stringVec.length - 1];

            for (int j = 0; j < stringVec.length - 1; j++) {
                vector[j] = Double.parseDouble(stringVec[j]);
            }

            vector = normService.normalize(vector);

            String resultType = test(vector);

            if (resultType.equals(type)) {
                successCount++;
            }
            allTests++;

            int indexOfPerceptronWithGivenType = -1;

            for (int i = 0; i < perceptrons.length; i++) {
                if (perceptrons[i].getLanguage().equals(type)) {
                    indexOfPerceptronWithGivenType = i;
                }
            }

            int indexOfPerceptronWithResultType = -1;

            for (int i = 0; i < perceptrons.length; i++) {
                if (perceptrons[i].getLanguage().equals(resultType)) {
                    indexOfPerceptronWithResultType = i;
                }
            }

            matrix[indexOfPerceptronWithGivenType][indexOfPerceptronWithResultType]++;

        }

        System.out.println("===============SKUTECZNOSC===============");
        System.out.println();
        System.out.println(successCount / allTests);


        System.out.println();
        System.out.println();

        System.out.println("=============MACIERZ OMYLEK=============");
        System.out.println();

        String firstLine = "Zaklasyfikowano jako -> \t ";
        for (Perceptron p : perceptrons) {
            firstLine = firstLine + p.getLanguage() + "\t";
        }

        System.out.println(firstLine);
        for (int i = 0; i < perceptrons.length; i++) {
            String nextLine = perceptrons[i].getLanguage() + "\t";
            for (int val : matrix[i]) {
                nextLine += val;
                nextLine += "\t";
            }
            System.out.println(String.format("%41s", nextLine));
        }

        System.out.println();
        System.out.println();
    }

    public void showWeightsAndThetaOfPerceptrons() {
        System.out.println("=============WAGI ORAZ PROGI=============");
        System.out.println();

        for (Perceptron p : perceptrons) {
            System.out.println("Jezyk: " + p.getLanguage());
            System.out.println("Prog: " + p.getTheta());
            System.out.println("Wagi: " + Arrays.toString(p.getWeights()));
            System.out.println();
        }

        System.out.println();
    }

}
