package model.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;

public class PassengerCSVReader implements ICSVReader<PassengerBrut> {
    private static PassengerCSVReader instance = null;

    private PassengerCSVReader() {
    }

    public static PassengerCSVReader getInstance() {
        if ( instance == null ) {
            instance = new PassengerCSVReader();
        }
        return instance;
    }

    @Override
    public List<PassengerBrut> readCSV(String file) throws IllegalStateException, IOException {
        return new CsvToBeanBuilder<PassengerBrut>(Files.newBufferedReader(Paths.get(file)))
                .withSeparator(',')
                .withType(PassengerBrut.class)
                .build().parse();
    }
}
