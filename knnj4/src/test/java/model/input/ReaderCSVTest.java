package model.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.factory.IrisFactory;
import model.factory.PassengerFactory;
import model.point.Iris;
import model.point.Passenger;

public class ReaderCSVTest {
    @Test
    public void load_csv_iris_test() throws IllegalStateException, IOException{
        IrisCSVReader irisCSVReader = IrisCSVReader.getInstance();
        List<IrisBrut> irisBrut = irisCSVReader.readCSV("data/iris/iris.csv");
        assertFalse(irisBrut.isEmpty());
    }

    @Test
    public void read_csv_iris_data_test() throws IllegalStateException, IOException{
        IrisCSVReader irisCSVReader = IrisCSVReader.getInstance();
        List<IrisBrut> irisBrutList = irisCSVReader.readCSV("data/iris/iris.csv");
        Iris irisFirstLine = new Iris(5.1,3.5,1.4,0.2,"Setosa");
        Iris irisLastline = new Iris(5.9,3,5.1,1.8,"Virginica");
        IrisFactory factory = IrisFactory.getInstance();
        List<Iris> iris = new ArrayList<>();
        for(IrisBrut data : irisBrutList){
            iris.add(factory.createPoint(data));
        }
        assertEquals(irisFirstLine, iris.get(0));
        assertEquals(irisLastline, iris.get(irisBrutList.size()-1));
    }

    @Test
    public void load_csv_passenger_test() throws IllegalStateException, IOException{
        PassengerCSVReader passengerCSVReader = PassengerCSVReader.getInstance();
        List<PassengerBrut> passengers = passengerCSVReader.readCSV("data/titanic/titanic.csv");
        assertFalse(passengers.isEmpty());
    }

    @Test
    public void read_csv_titanic_data_test() throws IllegalStateException, IOException{
        PassengerCSVReader passengerCSVReader = PassengerCSVReader.getInstance();
        List<PassengerBrut> passengersBrut = passengerCSVReader.readCSV("data/titanic/titanic.csv");
        
        Passenger passengerFirstLine = new Passenger(1,3,"Braund, Mr. Owen Harris", "male", 22, 1, 0, "A/5 21171" , 7.25,"","S", 0);
        
        Passenger passengerLastLine = new Passenger(891, 3, "Dooley, Mr. Patrick", "male", 32, 0, 0, "370376", 7.75, "", "Q",0);

        PassengerFactory factory = PassengerFactory.getInstance();
        List<Passenger> passengers = new ArrayList<>();
        for(PassengerBrut data : passengersBrut){
            passengers.add(factory.createPoint(data));
        }

        assertEquals(passengerFirstLine, passengers.get(0));
        assertEquals(passengerLastLine, passengers.get(passengers.size()-1));
    }

    
    
}
