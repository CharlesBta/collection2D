package com.cab;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Collection2D<E extends ICollection2DElement<E>> extends HashMap<Point, List<E>> {
    public void notifyElementHasMoved(final E collection2DElementTest, final Point oldPosition) {

    }

    public void add(final E element) {
        if (element == null) throw new IllegalArgumentException("Element cannot be null");
        final Point position = element.getPosition();
        if (position == null || position.x < 0 || position.y < 0)
            throw new ArrayIndexOutOfBoundsException("Position cannot be negative");

        if (element.getCollection() == this) return;
        if (element.getCollection() != null) throw new IllegalArgumentException("Element already in a collection");
        List<E> actualList = this.get(element.getPosition());
        if (actualList == null) actualList = new ArrayList<>();
        actualList.add(element);
        this.put(new Point(element.getPosition()), actualList);
        element.setCollection(this);
    }

    public void remove(final E element) {
        if (element == null) throw new IllegalArgumentException("Element cannot be null");
        if (element.getCollection() != this) throw new IllegalArgumentException("Element is not in this collection");

        final List<E> actualList = this.get(element.getPosition());
        if (!actualList.contains(element)) throw new IllegalArgumentException("Element is not in this collection");
        actualList.remove(element);
        element.setCollection(null);
        if (actualList.isEmpty()) this.remove(element.getPosition());
    }

    public boolean contains(final E elementTest) {
        return false;
    }

    public List<E> toList() {
        return null;
    }

    public E getDimension() {
        return null;
    }
}
