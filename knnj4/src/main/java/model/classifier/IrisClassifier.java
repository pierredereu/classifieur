
package model.classifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.Observable;
import model.dataset.Dataset;
import model.distance.Distance;
import model.point.Iris;
import model.point.IrisVariety;
import model.robustness.IrisRobustness;
import utils.ObservableProperty;

public class IrisClassifier extends Classifier<IrisVariety, Iris> {

    public IrisClassifier(Distance<Iris> distance, Dataset dataset) {
        this.distanceUsed = distance;
        this.trainingDataset = dataset;
        this.k = new ObservableProperty<>(4);
        this.bestK = new ObservableProperty<Integer>();
    }

    @Override
    public Map<IrisVariety, Integer> countPerClassificationValue(List<Iris> knn) {
        Map<IrisVariety, Integer> varietyCounter = new HashMap<>();
        varietyCounter.put(IrisVariety.SETOSA, 0);
        varietyCounter.put(IrisVariety.VERSICOLOR, 0);
        varietyCounter.put(IrisVariety.VIRGINICA, 0);

        for (Iris iris : knn) {
            if (iris.getClassification() == IrisVariety.SETOSA) {
                varietyCounter.merge(IrisVariety.SETOSA, 1, Integer::sum);
            } else if (iris.getClassification() == IrisVariety.VERSICOLOR) {
                varietyCounter.merge(IrisVariety.VERSICOLOR, 1, Integer::sum);
            } else {
                varietyCounter.merge(IrisVariety.VIRGINICA, 1, Integer::sum);
            }
        }
        return varietyCounter;
    }

    @Override
    public int calculBestK() {
        IrisRobustness robustness = new IrisRobustness(distanceUsed, trainingDataset);
        return robustness.computeBestK();
    }

}
