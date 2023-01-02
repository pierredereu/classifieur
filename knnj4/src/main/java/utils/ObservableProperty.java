package utils;

public class ObservableProperty<T> extends Subject {

	protected T value;

	public ObservableProperty(){
		
	}

	public ObservableProperty(T value){
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T val) {
		value = val;
		notifyObservers(val);
	}
}
