package model.dataset;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.factory.IrisFactory;
import model.input.IrisBrut;
import model.input.IrisCSVReader;
import model.point.IPoint;
import model.point.Iris;

public class DatasetTest {
    List<IPoint> listPoint;
    Dataset irisDataset;
    @BeforeEach
    public void setup() throws IllegalStateException, IOException{
        IrisCSVReader reader = IrisCSVReader.getInstance();
        List<IrisBrut> listIrisBrut = reader.readCSV("data/iris/iris.csv");
        IrisFactory factory = IrisFactory.getInstance();
        List<Iris> listIris = factory.createPoints(listIrisBrut);
        listPoint = new ArrayList<>();
        for(Iris iris : listIris){
            listPoint.add(iris);
        }
        irisDataset = new Dataset(listPoint, "irisTrainingDataset");
    }

    @Test
    public void should_return_points_iris_dataset(){
        assertEquals(listPoint, irisDataset.getPoints());
    }

    @Test void shoud_get_dataset_title(){
        assertEquals("irisTrainingDataset", irisDataset.getTitle());
    }

    @Test
    public void should_get_dataset_observable_points(){
        assertEquals(listPoint, irisDataset.getObservablePoints());
    }

    @Test
    public void should_set_dataset_lines(){
        assertEquals(listPoint, irisDataset.getObservablePoints());

        List<IPoint> newList = new ArrayList<>();
        Iris iris = new Iris(2, 1, 2, 0.3, "Setosa");
        newList.add(iris);
        irisDataset.setLines(newList);
        assertEquals(newList, irisDataset.getObservablePoints());
    }

}
