package reflection_database.bll.validators;

public interface Validator<T> {
    public void validate(T obj);
}
