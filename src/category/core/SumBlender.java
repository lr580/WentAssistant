package category.core;

public interface SumBlender {
    default public String blend_line(int value, int dt) {// 前导渲染
        return "...";
    }

    default public String blend_value(String name, int value, int dt) {// 值渲染
        return name + ": " + value + "\n";
    }
}