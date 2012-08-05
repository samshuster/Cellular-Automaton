package practice;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class FullyJustifiedGridLayout implements LayoutManager {
    private final int rows;
    private final int columns;

    public FullyJustifiedGridLayout(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            double clientWidth = parent.getWidth() - parent.getInsets().left - parent.getInsets().right; 
            double clientHeight = parent.getHeight() - parent.getInsets().top - parent.getInsets().bottom; 
            for (int i = 0; i < parent.getComponents().length; i++) {
                Component component = parent.getComponents()[i];
                int row = i / rows, column = i % columns;
                int x = (int)(clientWidth * column / columns);
                int y = (int)(clientHeight * row / rows);
                int nextX = (int)(clientWidth * (column + 1) / columns);
                int nextY = (int)(clientHeight * (row + 1) / rows);
                component.setBounds(x, y, nextX - x, nextY - y);
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(20 * 10, 20 * 10);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(20 * 20, 20 * 20);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }
}

