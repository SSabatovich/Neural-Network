package models;

import services.VectorNormService;

public class Perceptron {
    private String language;
    private Double[] weights;
    private double theta;

    public Perceptron(String language, int weightsCount) {
        this.language = language;

        weights = new Double[weightsCount];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() + 0.01;
        }

        weights = new VectorNormService().normalize(weights);

        this.theta = Math.random();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double[] getWeights() {
        return weights;
    }

    public void setWeights(Double[] weights) {
        this.weights = weights;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
