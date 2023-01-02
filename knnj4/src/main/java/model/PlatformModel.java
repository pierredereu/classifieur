package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import model.classifier.Classifier;
import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.column.ColumnString;
import model.column.ColumnStringAttribute;
import model.dataset.Dataset;
import model.distance.Distance;
import model.distance.DistanceAlgorithm;
import model.point.IClassificationValue;
import model.point.IPoint;
import model.robustness.Robustness;
import utils.ObservableProperty;
import utils.Subject;

public abstract class PlatformModel<ClassificationValueType extends IClassificationValue, PointType extends IPoint, ClassifierType extends Classifier<ClassificationValueType, PointType>>
        extends Subject {

    protected ClassifierType classifier;
    protected Dataset trainingDataset;
    protected Dataset suspectDataset;
    protected ColumnNumberAttribute axisX;
    protected ColumnNumberAttribute axisY;
    protected ColumnNumber axisXColumnTrainingset;
    protected ColumnNumber axisYColumnTrainingset;
    protected Robustness<ClassificationValueType, ClassifierType, PointType> robustness;

    public abstract void initializeAxisX();

    public abstract void initializeAxisY();

    public abstract void updateRobustness();

    public abstract void importTrainingDataset(String file) throws IllegalStateException, IOException ;

    public abstract void importSuspectDataset(String file) throws IllegalStateException, IOException ;

    public abstract void initializeClassifier();

    public abstract Map<ColumnNumberAttribute, ColumnNumber> getColumnsNumber();

    public abstract Map<ColumnStringAttribute, ColumnString> getColumnsString();



    public ClassificationValueType getClassificationPoint(PointType point){
		return this.classifier.getClassification(point);
	}

    public void classify(){
        for(IPoint point : this.getSuspectDataset().getPoints()){
            PointType pointType = (PointType) point;
            pointType.setClassification(getClassificationPoint((PointType) point));
        }
    }

    public Robustness<ClassificationValueType, ClassifierType, PointType> getRobustness() {
        return this.robustness;
    }

    public ObservableList<IPoint> getObservableTrainingDatasetPoints(){
        return this.trainingDataset.getObservablePoints();
    }

    public ObservableList<IPoint> getObservableSuspectDatasetPoints(){
        return this.suspectDataset.getObservablePoints();
    }
    
    public void addSuspectPoint(PointType point){
        this.suspectDataset.addLine(point);
    }

    public double getRobustnessValue(int k){
        return this.robustness.getRobustnessValue(k);
    }

    public double getMaxRobustnessValue(){
        return this.robustness.getMaxRobustnessValue();
    }
    
    public void addTrainingPoint(PointType point){
        this.trainingDataset.addLine(point);
        updateAxisColumnsDataset();
        updateRobustness();
    }

    public List<IPoint> transformToIPoints(List<PointType> list) {
        List<IPoint> res = new ArrayList<>();
        for (PointType point : list) {
            res.add(point);
        }
        return res;
    }

    public ObservableProperty<Boolean> isDistanceNormalized(){
        return this.classifier.getDistanceUsed().isDistanceNormalized();
    }

    public void setDistanceNormalized(boolean value){
        this.classifier.getDistanceUsed().setDistanceNormalize(value);
    }

    public ObservableProperty<Integer> getK() {
        return this.classifier.getK();
    }

    public void setK(int k) {
        this.classifier.setK(k);
    }

    public ObservableProperty<Integer> getBestK(){
       return this.classifier.getBestK();
    }

    public void findBestK() {
        this.classifier.setBestK(this.classifier.calculBestK());
    }

    public void clearSuspectDataset() {
        this.suspectDataset.clear();
    }

    public void clearTrainingDataset() {
        this.trainingDataset.clear();
        updateAxisColumnsDataset();
    }

    public ColumnNumberAttribute getAxisX() {
        return this.axisX;
    }

    public ColumnNumberAttribute getAxisY() {
        return this.axisY;
    }

    public Dataset getSuspectDataset() {
        return this.suspectDataset;
    }

    public ClassifierType getClassifier() {
        return classifier;
    }

    public void setClassifier(ClassifierType classifier) {
        this.classifier = classifier;
        this.notifyObservers(this.classifier);
    }

    public Dataset getTrainingDataset() {
        return this.trainingDataset;
    }

    public Distance<PointType> getDistanceUsed() {
        return this.classifier.getDistanceUsed();
    }

    public void setDistanceAlgorithm(DistanceAlgorithm algorithm){
        this.classifier.getDistanceUsed().setDistanceAlgorithm(algorithm);
    }

    public void initializeAxisXColumn() {
        this.axisXColumnTrainingset = new ColumnNumber(axisX, axisX.toString(), trainingDataset, suspectDataset);
        this.notifyObservers(this.axisXColumnTrainingset);
    }

    public void initializeAxisYColumn() {
        this.axisYColumnTrainingset = new ColumnNumber(axisY, axisY.toString(), trainingDataset, suspectDataset);
		this.notifyObservers(this.axisYColumnTrainingset);
    }

    protected void updateAxisXColumnDataset(){
        this.axisXColumnTrainingset.setTrainingDataset(trainingDataset);
    }

    protected void updateAxisYColumnDataset(){
        this.axisYColumnTrainingset.setTrainingDataset(trainingDataset);
    }

    public void updateAxisColumnsDataset(){
        updateAxisXColumnDataset();
        updateAxisYColumnDataset();
    }

    public ColumnNumber getAxisXColumnTrainingset(){
        return this.axisXColumnTrainingset;
    }

    public ColumnNumber getAxisYColumnTrainingset(){
        return this.axisYColumnTrainingset;
    }

    

}
