package model.robustness;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import model.classifier.PassengerClassifier;
import model.dataset.Dataset;
import model.distance.Distance;
import model.point.IPoint;
import model.point.Passenger;
import model.point.PassengerHasSurvivedOrNot;

public class PassengerRobustness extends Robustness<PassengerHasSurvivedOrNot,PassengerClassifier,Passenger>{

    public PassengerRobustness(Distance<Passenger> distance, Dataset fullDataTrainingSet) {
        this.fullDataTrainingSet = fullDataTrainingSet;
        this.distance = distance;
        setTestAndTrainingList();
        initializeRobustnessClassifier();
    }

    public List<IPoint> transformIrisListToPointList(List<Passenger> passengerList){
        List<IPoint> points = new ArrayList<>();
        for(Passenger passenger : passengerList){
            points.add((IPoint) passenger);
        }
        return points;
    }

    @Override
    public void initializeRobustnessClassifier() {
        this.classifierRobustness = new PassengerClassifier(distance, new Dataset(transformIrisListToPointList(trainingList), "TRAINING_SET_FOR_ROBUSTNESS"));   
    }
    
}
