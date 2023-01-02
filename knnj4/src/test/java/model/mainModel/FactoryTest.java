package model.mainModel;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.factory.IrisFactory;
import model.factory.PassengerFactory;
import model.input.IrisBrut;
import model.input.IrisCSVReader;
import model.input.PassengerBrut;
import model.input.PassengerCSVReader;
import model.point.Iris;
import model.point.Passenger;

public class FactoryTest {
    List<IrisBrut> listIrisBrut = new ArrayList<>();
    List<PassengerBrut> listPassengerBrut = new ArrayList<>();

    @BeforeEach
    public void setup() throws IllegalStateException, IOException{
        IrisCSVReader irisReader = IrisCSVReader.getInstance();
        PassengerCSVReader passengerReader = PassengerCSVReader.getInstance();
        listIrisBrut = irisReader.readCSV("data/iris/iris.csv");
        listPassengerBrut = passengerReader.readCSV("data/titanic/titanic.csv");
    }

    @Test
    public void should_create_iris(){
        IrisFactory factory = IrisFactory.getInstance();
        List<Iris> listIris = factory.createPoints(listIrisBrut);
        assertNotNull(listIris);
    }

    @Test
    public void should_create_passenger(){
        PassengerFactory factory = PassengerFactory.getInstance();
        List<Passenger> listPassenger = factory.createPoints(listPassengerBrut);
        assertNotNull(listPassenger);
    }
}
