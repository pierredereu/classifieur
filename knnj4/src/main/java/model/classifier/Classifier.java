package model.classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import model.dataset.Dataset;
import model.distance.Distance;
import model.point.ClassificationNull;
import model.point.IClassificationValue;
import model.point.IPoint;
import utils.ObservableProperty;

public abstract class Classifier<ClassificationValueType extends IClassificationValue,PointType extends IPoint>{
     protected Distance<PointType> distanceUsed;
     protected ObservableProperty<Integer> bestK;
     protected ObservableProperty<Integer> k;
     protected Dataset trainingDataset;


     public Distance<PointType> getDistanceUsed(){
          return this.distanceUsed;
     }

     public void setDistanceUsed(Distance<PointType> distance){
          this.distanceUsed = distance;
     }

     public void setK(int k){
          this.k.setValue(k);
     }

     public ObservableProperty<Integer> getK(){
          return this.k;
     }

     public abstract int calculBestK();

     public ObservableProperty<Integer> getBestK(){
          return this.bestK;
     }

     public void setBestK(int k){
          this.bestK.setValue(k);
     }

     public void setDataset(Dataset dataset){
          this.trainingDataset = dataset;
     }

     public Dataset getDataset(){
          return this.trainingDataset;
     }

     public ClassificationValueType getClassification(PointType point){
          int saveCurrentK = getK().getValue();
          List<PointType> knn = getKnnPoint(point);
          Map<ClassificationValueType,Integer> maxClassification = maxCountClassificationValue(countPerClassificationValue(knn));
          while(maxClassification.size()>1) {
               setK(getK().getValue()+1);
               knn = getKnnPoint(point);
               maxClassification = maxCountClassificationValue(countPerClassificationValue(knn));
          }
          setK(saveCurrentK);
          return maxClassification.keySet().stream().findFirst().orElse((ClassificationValueType) new ClassificationNull());
     }

     public List<PointType> getKnnPoint(PointType point) {
          return getKnnPointsWithDistanceValue(point, getK().getValue())
               .entrySet()
               .stream()
               .map(Map.Entry::getKey)
               .collect(Collectors.toList());
     }

     public Map<PointType, Double> getKnnPointsWithDistanceValue(PointType point, int k){
          Map<PointType,Double> irisDistanceMap = getDistanceBetweenSuspectAndDatasetTraining(point);
          return irisDistanceMap
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(k)
                    .collect(Collectors.toMap(
                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
     }

     public Map<PointType, Double> getDistanceBetweenSuspectAndDatasetTraining(PointType point) {
          Map<PointType, Double> irisDistanceMap = new HashMap<>();
          for (PointType iris : getDatasetPoints()) {
               irisDistanceMap.put(iris, getDistanceUsed().distance(iris, point));
          }
          return irisDistanceMap;
     }

     public abstract Map<ClassificationValueType, Integer> countPerClassificationValue(List<PointType> knn);

     public Map<ClassificationValueType, Integer> maxCountClassificationValue(Map<ClassificationValueType, Integer> counter) {
          Map<ClassificationValueType, Integer> maxCountVariety = new HashMap<>();
          int maxValueInMap = (Collections.max(counter.values()));
          for (Entry<ClassificationValueType, Integer> entry : counter.entrySet()) {
               if (entry.getValue() == maxValueInMap) {
                    maxCountVariety.put(entry.getKey(), entry.getValue());
               }
          }
          return maxCountVariety;
     }

     public List<PointType> getDatasetPoints(){
          List<PointType> list = new ArrayList<>();
          Iterator<PointType> iterator = (Iterator<PointType>) getDataset().iterator();
          while(iterator.hasNext()){
               PointType point = iterator.next();
               list.add(point);
          }
          return list;
     }
}
