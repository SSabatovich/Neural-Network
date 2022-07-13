package services;

public class VectorNormService {
    public Double[] normalize(Double[] vector) {
        double len = 0;

        for (Double val : vector) {
            len += val * val;
        }

        len = Math.sqrt(len);

        if (len == 0) {
            return vector;
        }


        Double[] normalizedVec = new Double[vector.length];

        int i = 0;
        for (Double val : vector) {
            normalizedVec[i] = val / len;
            i++;
        }

        return normalizedVec;
    }
}