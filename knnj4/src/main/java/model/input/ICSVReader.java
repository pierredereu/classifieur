package model.input;

import java.io.IOException;
import java.util.List;

public interface ICSVReader<IPointBrut> {

    public List<IPointBrut> readCSV(String file) throws IllegalStateException, IOException;
}
