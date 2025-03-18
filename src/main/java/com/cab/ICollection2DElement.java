package com.cab;

import java.awt.*;

public interface ICollection2DElement<E extends ICollection2DElement<E>> {
    Point getPosition();

    Collection2D<E> getCollection();

    void setCollection(Collection2D<E> collection2D);
}
