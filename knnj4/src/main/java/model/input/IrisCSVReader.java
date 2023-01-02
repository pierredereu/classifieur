package model.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;

public class IrisCSVReader implements ICSVReader<IrisBrut> {
    private static IrisCSVReader instance = null;

    private IrisCSVReader() {
    }

    public static IrisCSVReader getInstance() {
        if ( instance == null ) {
            instance = new IrisCSVReader();
        }
        return instance;
    }

    @Override
    public List<IrisBrut> readCSV(String file) throws IllegalStateException, IOException {
        return new CsvToBeanBuilder<IrisBrut>(Files.newBufferedReader(Paths.get(file)))
                .withSeparator(',')
                .withType(IrisBrut.class)
                .build().parse();
    }

}
