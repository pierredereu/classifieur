
package model.robustness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import model.classifier.Classifier;
import model.dataset.Dataset;
import model.distance.Distance;
import model.point.IClassificationValue;
import model.point.IPoint;

public abstract class Robustness<ClassificationValueType extends IClassificationValue, ClassifierType extends Classifier<ClassificationValueType, PointType>, PointType extends IPoint> {
    protected Dataset fullDataTrainingSet;
    protected List<PointType> testList;
    protected List<PointType> trainingList;
    protected Distance<PointType> distance;
    protected ClassifierType classifierRobustness;
    protected final int SEED = 42;

    public void setTestAndTrainingList() {
        List<PointType> list = (List<PointType>) fullDataTrainingSet.getPoints();
        List<PointType> data = new ArrayList<>(list);
        Collections.shuffle(data, new Random(SEED));
        int idxSeparation = data.size() / 5;
        this.testList = data.subList(0, idxSeparation);
        this.trainingList = data.subList(idxSeparation, data.size());
    }

    public double getRobustnessValue(int k) {
        int currentK = classifierRobustness.getK().getValue();
        classifierRobustness.setK(k);
        int cptGoodResult = 0;
        for (PointType point : testList) {
            ClassificationValueType classificationExpected = (ClassificationValueType) ((PointType) point)
                    .getClassification();
            ClassificationValueType classification = (ClassificationValueType) classifierRobustness
                    .getClassification(point);
            if (classificationExpected == classification)
                cptGoodResult++;
        }
        classifierRobustness.setK(currentK);
        double robustness = cptGoodResult * 100.0 / testList.size();
        return robustness;
    }

    public double getMaxRobustnessValue() {
        List<Double> robustnessList = new ArrayList<>();
        int relevantLimitK = 30;
        for (int i = 1; i <= relevantLimitK; i++) {
            robustnessList.add(getRobustnessValue(i));
        }
        return robustnessList.stream().max(Comparator.naturalOrder()).orElse(-1.0);
    }

    public void updateToBestK() {
        classifierRobustness.setK(computeBestK());
    }

    public int computeBestK() {
        double maxRobustness = getMaxRobustnessValue();
        for (int k = 1; k <= 30; k++) {
            if (maxRobustness == getRobustnessValue(k))
                return k;
        }
        return 1;
    }

    public Dataset getFullTrainingDataset() {
        return this.fullDataTrainingSet;
    }

    public List<PointType> getTestList() {
        return this.testList;
    }

    public List<PointType> getTrainingList() {
        return this.trainingList;
    }

    public Distance<PointType> getDistanceUsed() {
        return this.distance;
    }

    public ClassifierType getRobustnessClassifier() {
        return this.classifierRobustness;
    }

    public abstract void initializeRobustnessClassifier();
}
