package mappers;

public interface CSVMapper<T> {
    T map(String[] cols);
}
