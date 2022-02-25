package ui;

public interface TableFilter {
    default public boolean isReserve(Object[] row) {// 不是使用未渲染的数据库原数据，而是使用渲染过后的数据
        return true;
    }
}
