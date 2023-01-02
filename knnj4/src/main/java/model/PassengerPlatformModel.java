package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.classifier.PassengerClassifier;
import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.column.ColumnString;
import model.column.ColumnStringAttribute;
import model.dataset.Dataset;
import model.distance.DistanceAlgorithm;
import model.distance.DistancePassenger;
import model.factory.PassengerFactory;
import model.input.PassengerBrut;
import model.input.PassengerCSVReader;
import model.point.IPoint;
import model.point.Passenger;
import model.point.PassengerHasSurvivedOrNot;
import model.robustness.PassengerRobustness;

public class PassengerPlatformModel extends PlatformModel<PassengerHasSurvivedOrNot, Passenger, PassengerClassifier> {

	public PassengerPlatformModel() {
		List<IPoint> trainingList = new ArrayList<>();
		List<IPoint> suspectList = new ArrayList<>();
		trainingDataset = new Dataset(trainingList, "Modele Passenger");
		suspectDataset = new Dataset(suspectList, "Passenger suspectes");
		initializeAxisX();
		initializeAxisY();
		initializeAxisXColumn();
		initializeAxisYColumn();
		initializeClassifier();
		updateRobustness();
	}

	public void updateRobustness() {
		this.robustness = new PassengerRobustness(getDistanceUsed(), trainingDataset);
	}

	@Override
	public void initializeClassifier() {
		DistancePassenger distance = new DistancePassenger(true, DistanceAlgorithm.MANHATTAN,
				getColumnsNumber().get(ColumnNumberAttribute.PASSENGER_CLASS),
				getColumnsNumber().get(ColumnNumberAttribute.PASSENGER_AGE),
				getColumnsNumber().get(ColumnNumberAttribute.SIBSP),
				getColumnsNumber().get(ColumnNumberAttribute.PARCH),
				getColumnsNumber().get(ColumnNumberAttribute.PASSENGER_TICKET_PRICE),
				getColumnsString().get(ColumnStringAttribute.PASSENGER_SEX),
				getColumnsString().get(ColumnStringAttribute.PASSENGER_CABIN),
				getColumnsString().get(ColumnStringAttribute.PASSENGER_PORT_OF_EMBARKATION));
		this.classifier = new PassengerClassifier(distance, trainingDataset);
	}

	@Override
	public void initializeAxisX() {
		this.axisX = ColumnNumberAttribute.PASSENGER_AGE;
	}

	@Override
	public void initializeAxisY() {
		this.axisY = ColumnNumberAttribute.PARCH;
	}

	@Override
	public void importTrainingDataset(String file) throws IllegalStateException, IOException {
		PassengerCSVReader reader = PassengerCSVReader.getInstance();
		PassengerFactory factory = PassengerFactory.getInstance();
		List<PassengerBrut> dataBrut = reader.readCSV(file);
		List<IPoint> passengers = transformToIPoints(factory.createPoints(dataBrut));
		this.trainingDataset.clear();
		this.trainingDataset.addAllLine(passengers);
		updateAxisColumnsDataset();
		updateRobustness();
	}

	@Override
	public void importSuspectDataset(String file) throws IllegalStateException, IOException {
		PassengerCSVReader reader = PassengerCSVReader.getInstance();
		PassengerFactory factory = PassengerFactory.getInstance();
		List<PassengerBrut> dataBrut = reader.readCSV(file);
		List<IPoint> passenger = transformToIPoints(factory.createPoints(dataBrut));
		this.suspectDataset.addAllLine(passenger);
	}

	@Override
	public Map<ColumnNumberAttribute, ColumnNumber> getColumnsNumber() {
		Map<ColumnNumberAttribute, ColumnNumber> res = new HashMap<>();
		res.put(ColumnNumberAttribute.PARCH,
				new ColumnNumber(ColumnNumberAttribute.PARCH, "PARCH", trainingDataset, suspectDataset));
		res.put(ColumnNumberAttribute.PASSENGER_AGE,
				new ColumnNumber(ColumnNumberAttribute.PASSENGER_AGE, "AGE", trainingDataset, suspectDataset));
		res.put(ColumnNumberAttribute.PASSENGER_CLASS,
				new ColumnNumber(ColumnNumberAttribute.PASSENGER_CLASS, "CLASS", trainingDataset, suspectDataset));
		res.put(ColumnNumberAttribute.PASSENGER_TICKET_PRICE,
				new ColumnNumber(ColumnNumberAttribute.PASSENGER_TICKET_PRICE, "PRICE", trainingDataset,
						suspectDataset));
		res.put(ColumnNumberAttribute.SIBSP,
				new ColumnNumber(ColumnNumberAttribute.SIBSP, "SIBSP", trainingDataset, suspectDataset));
		return res;
	}

	@Override
	public Map<ColumnStringAttribute, ColumnString> getColumnsString() {
		Map<ColumnStringAttribute, ColumnString> res = new HashMap<>();
		res.put(ColumnStringAttribute.PASSENGER_SEX,
				new ColumnString(ColumnStringAttribute.PASSENGER_SEX, "SEX", trainingDataset, suspectDataset));
		res.put(ColumnStringAttribute.PASSENGER_CABIN,
				new ColumnString(ColumnStringAttribute.PASSENGER_CABIN, "CABIN", trainingDataset, suspectDataset));
		res.put(ColumnStringAttribute.PASSENGER_PORT_OF_EMBARKATION,
				new ColumnString(ColumnStringAttribute.PASSENGER_PORT_OF_EMBARKATION, "PORT", trainingDataset,
						suspectDataset));
		return res;
	}
}
