package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvToStringVectorsService {
    public List<String[]> csvToVectors(String filePath) {
        List<String[]> vectors = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            int vectorSize = lines.get(0).split(";").length;

            for (int i = 1; i < lines.size(); i++) {

                String line = lines.get(i);

                String[] vector = line.split(";");


                vectors.add(vector);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vectors;
    }
}
