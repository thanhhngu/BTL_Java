package presentation_layer.itf;

import javax.swing.*;

public interface HandleActionCallBack {
    @FunctionalInterface
    public interface RowDoubleClickHandler<T> {
        JPanel buildMessage(T item);
    }
}
