package model.classifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dataset.Dataset;
import model.distance.Distance;
import model.point.Passenger;
import model.point.PassengerHasSurvivedOrNot;
import model.robustness.PassengerRobustness;
import utils.ObservableProperty;

public class PassengerClassifier extends Classifier<PassengerHasSurvivedOrNot, Passenger> {

    public PassengerClassifier(Distance<Passenger> distance, Dataset dataset) {
        this.distanceUsed = distance;
        this.trainingDataset = dataset;
        this.k = new ObservableProperty<>(22);
        this.bestK = new ObservableProperty<Integer>();
    }

    @Override
    public Map<PassengerHasSurvivedOrNot, Integer> countPerClassificationValue(List<Passenger> knn) {
        Map<PassengerHasSurvivedOrNot, Integer> survivorOrNotCounter = new HashMap<>();
        survivorOrNotCounter.put(PassengerHasSurvivedOrNot.NO, 0);
        survivorOrNotCounter.put(PassengerHasSurvivedOrNot.YES, 0);
        PassengerHasSurvivedOrNot passAway = PassengerHasSurvivedOrNot.NO;
        for (Passenger passenger : knn) {
            if (passenger.getClassification() == passAway) {
                survivorOrNotCounter.merge(passAway, 1, Integer::sum);
            } else {
                survivorOrNotCounter.merge(PassengerHasSurvivedOrNot.YES, 1, Integer::sum);
            }
        }
        return survivorOrNotCounter;
    }

    @Override
    public int calculBestK() {
        PassengerRobustness robustness = new PassengerRobustness(distanceUsed, trainingDataset);
        return robustness.computeBestK();
    }
}
