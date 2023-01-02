package model.column;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dataset.Dataset;
import model.dataset.IDataset;
import model.point.IPoint;
import model.point.Iris;
import model.point.Passenger;

public class ColumnStringTest {
	protected ColumnStringAttribute type;
	protected List<IPoint> passengers = new ArrayList<>();
	protected IDataset passengerDataset;
	protected String passengerDatasetName;
	protected Passenger passenger1 = new Passenger(1, 2, "P1", "M", 20, 0, 0, "001", 10, "C1", "S", 0);
	protected Passenger passenger2 = new Passenger(2, 3, "P2", "F", 21, 1, 2, "002", 11.5, "C2", "C", 0);

	@BeforeEach
	public void setup() {
		passengerDatasetName = "PASSENGER";

		passengers.add(passenger1);
		passengers.add(passenger2);

		passengerDataset = new Dataset(passengers, passengerDatasetName);
	}

	@Test
	void should_give_default_configuration_then_generate_single_object() {
		EasyRandom generator = new EasyRandom();
		Iris iris = generator.nextObject(Iris.class);

		assertNotNull(iris);
	}
}
