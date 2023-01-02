package model.dataset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.point.IPoint;

public class Dataset implements IDataset{
    protected ObservableList<IPoint> points = FXCollections.observableArrayList();
    protected String title;

    public Dataset(List<IPoint> points, String title) {
        this.points.addAll(points);
        this.title = title;
    }

    @Override
    public Iterator<IPoint> iterator() {
        return this.points.iterator();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public int getNbLines() {
        return this.points.size();
    }

    @Override
    public void setLines(List<IPoint> lines) {
        List<IPoint> list = new ArrayList<>();
        for(IPoint point : lines){
            list.add(point);
        }
        this.points.setAll(list);
    }

    @Override
    public void addLine(IPoint point) {
        this.points.add(point);
    }

    @Override
    public void addAllLine(List<IPoint> points) {
        for(IPoint point : points){
            addLine(point);
        }
    }

    public List<IPoint> getPoints(){
        return this.points;
    }

    public ObservableList<IPoint> getObservablePoints(){
        return this.points;
    }

    @Override
    public void clear() {
        this.points.clear();  
    }
    
}
