
package model.robustness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import model.classifier.IrisClassifier;
import model.dataset.Dataset;
import model.distance.Distance;
import model.point.IPoint;
import model.point.Iris;
import model.point.IrisVariety;

public class IrisRobustness extends Robustness<IrisVariety,IrisClassifier,Iris>{

    public IrisRobustness(Distance<Iris> distance,Dataset dataTraining) {
        this.distance = distance;
        this.fullDataTrainingSet = dataTraining;
        setTestAndTrainingList();
        initializeRobustnessClassifier();
    }

    public List<IPoint> transformIrisListToPointList(List<Iris> irisList){
        List<IPoint> points = new ArrayList<>();
        for(Iris iris : irisList){
            points.add((IPoint) iris);
        }
        return points;
    }

    @Override
    public void initializeRobustnessClassifier(){
        Dataset dataset = new Dataset(transformIrisListToPointList(trainingList), "TRAINING_SET_ROBUSTNESS");
        this.classifierRobustness = new IrisClassifier(distance, dataset);
    }
  
}
