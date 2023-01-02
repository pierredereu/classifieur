package model.point;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IrisTest {
    Iris iris;

    @BeforeEach
    public void setup() {
        iris = new Iris(1.0, 2.0, 3.0, 0.5, "Setosa");
    }

    @Test
    public void should_get_iris_attributes() {
        assertEquals(1, iris.getSepalLength());
        assertEquals(2, iris.getSepalWidth());
        assertEquals(3, iris.getPetalLength());
        assertEquals(0.5, iris.getPetalWidth());
        assertEquals(IrisVariety.SETOSA, iris.getClassification());
    }

    @Test
    public void should_return_to_string() {
        assertEquals(
                "               1.0                    2.0                       3.0                         0.5             Setosa",
                iris.toString());
    }

}
