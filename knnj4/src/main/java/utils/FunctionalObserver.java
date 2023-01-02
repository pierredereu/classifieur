package utils;

@FunctionalInterface
public interface FunctionalObserver {
    public void update(Subject subj, Object data);
}
