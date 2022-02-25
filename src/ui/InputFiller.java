package ui;

public interface InputFiller {
    default public void fill(Object[] s) {// 转换并填入
    }

    default public Object[] convert(Object[] s) {//转换但不填入
        return s;
    }
}
