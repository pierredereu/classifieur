package model.factory;

import java.util.ArrayList;
import java.util.List;

import model.input.IPointBrut;
import model.point.IPoint;

public interface IFactory<PointType extends IPoint, PointBrutType extends IPointBrut>{
    public PointType createPoint(PointBrutType pointBrut);
    
    public default List<PointType> createPoints(List<PointBrutType> list){
        List<PointType> res = new ArrayList<>();
        for(PointBrutType data : list){
            res.add(createPoint(data));
        }
        return res;
    }
}