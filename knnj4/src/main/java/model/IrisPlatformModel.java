package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.classifier.IrisClassifier;
import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.column.ColumnString;
import model.column.ColumnStringAttribute;
import model.dataset.Dataset;
import model.distance.DistanceAlgorithm;
import model.distance.DistanceIris;
import model.factory.IrisFactory;
import model.input.IrisBrut;
import model.input.IrisCSVReader;
import model.point.IPoint;
import model.point.Iris;
import model.point.IrisVariety;
import model.robustness.IrisRobustness;

public class IrisPlatformModel extends PlatformModel<IrisVariety, Iris, IrisClassifier> {

	public IrisPlatformModel() {
		List<IPoint> trainingList = new ArrayList<>();
		List<IPoint> suspectList = new ArrayList<>();
		this.trainingDataset = new Dataset(trainingList, "Modele Iris");
		this.suspectDataset = new Dataset(suspectList, "Iris suspectes");
		initializeAxisX();
		initializeAxisY();
		initializeAxisXColumn();
		initializeAxisYColumn();
		initializeClassifier();
	}

	@Override
	public void updateRobustness() {
		this.robustness = new IrisRobustness(getDistanceUsed(), trainingDataset);
	}

	@Override
	public void importTrainingDataset(String file) throws IllegalStateException, IOException {
		IrisCSVReader reader = IrisCSVReader.getInstance();
		List<IrisBrut> dataBrut = reader.readCSV(file);
		IrisFactory factory = IrisFactory.getInstance();
		List<IPoint> iris = transformToIPoints(factory.createPoints(dataBrut));
		this.trainingDataset.clear();
		this.trainingDataset.addAllLine(iris);
		updateAxisColumnsDataset();
		updateRobustness();
	}

	@Override
	public void importSuspectDataset(String file) throws IllegalStateException, IOException {
		IrisCSVReader reader = IrisCSVReader.getInstance();
		IrisFactory factory = IrisFactory.getInstance();
		List<IrisBrut> dataBrut = reader.readCSV(file);
		List<IPoint> iris = transformToIPoints(factory.createPoints(dataBrut));
		this.suspectDataset.addAllLine(iris);
	}

	@Override
	public void initializeAxisX() {
		this.axisX = ColumnNumberAttribute.PETAL_LENGTH;
	}

	@Override
	public void initializeAxisY() {
		this.axisY = ColumnNumberAttribute.PETAL_WIDTH;
	}

	@Override
	public void initializeClassifier() {
		DistanceIris distance = new DistanceIris(true, DistanceAlgorithm.MANHATTAN,
				getColumnsNumber().get(ColumnNumberAttribute.SEPAL_LENGTH),
				getColumnsNumber().get(ColumnNumberAttribute.SEPAL_WIDTH),
				getColumnsNumber().get(ColumnNumberAttribute.PETAL_LENGTH),
				getColumnsNumber().get(ColumnNumberAttribute.PETAL_WIDTH));
		this.classifier = new IrisClassifier(distance, trainingDataset);
	}

	@Override
	public Map<ColumnNumberAttribute, ColumnNumber> getColumnsNumber() {
		Map<ColumnNumberAttribute, ColumnNumber> res = new HashMap<>();
		res.put(ColumnNumberAttribute.PETAL_LENGTH,
				new ColumnNumber(ColumnNumberAttribute.PETAL_LENGTH, "LONGUEUR PETALE", trainingDataset,
						suspectDataset));
		res.put(ColumnNumberAttribute.PETAL_WIDTH,
				new ColumnNumber(ColumnNumberAttribute.PETAL_WIDTH, "LARGEUR PETALE", trainingDataset, suspectDataset));
		res.put(ColumnNumberAttribute.SEPAL_LENGTH,
				new ColumnNumber(ColumnNumberAttribute.SEPAL_LENGTH, "LONGUEUR SEPALE", trainingDataset,
						suspectDataset));
		res.put(ColumnNumberAttribute.SEPAL_WIDTH,
				new ColumnNumber(ColumnNumberAttribute.SEPAL_WIDTH, "LARGEUR SEPALE", trainingDataset, suspectDataset));
		return res;
	}

	@Override
	public Map<ColumnStringAttribute, ColumnString> getColumnsString() {
		return new HashMap<ColumnStringAttribute, ColumnString>();
	}

}
