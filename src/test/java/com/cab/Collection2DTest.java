package com.cab;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Collection2DTest {

    public static final int NB_ELEMENTS_TO_ADD = 10;
    public static final int NB_ROWS = 100;
    public static final int NB_COLUMNS = 10;

    private static Collection2D<Collection2DElementTest> collection2D;
    private static List<Collection2DElementTest> elements;

    @BeforeEach
    void beforeEach() {
        Collection2DTest.collection2D = new Collection2D<>();
        Collection2DTest.elements = new ArrayList<>();

        for (int numElements = 0; numElements < NB_ELEMENTS_TO_ADD; numElements++) {
            for (int row = 0; row < NB_ROWS; row++) {
                for (int column = 0; column < NB_COLUMNS; column++) {
                    Collection2DElementTest element = new Collection2DElementTest();
                    element.setPosition(new Point(column, row));
                    collection2D.add(element);
                    elements.add(element);
                }
            }
        }
    }

    @Test
    void addAndGet() {
        for (Collection2DElementTest elementTest : Collection2DTest.elements) {
            List<Collection2DElementTest> actualElementsAtPosition = collection2D.get(elementTest.getPosition());

            assertNotNull(actualElementsAtPosition, "Element list at position " + elementTest.position + " is null");
            assertEquals(Collection2DTest.NB_ELEMENTS_TO_ADD, actualElementsAtPosition.size(), "Element list at position " + elementTest.position + " has more than one element");
            assertTrue(actualElementsAtPosition.contains(elementTest), "Element list at position " + elementTest.position + " does not contain the element");
        }

        Collection2DElementTest newElement = new Collection2DElementTest();
        newElement.setPosition(new Point(-100, -10));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Collection2DTest.collection2D.add(newElement), "Element with negative position has been added");
        newElement.setPosition(new Point(100, -10));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Collection2DTest.collection2D.add(newElement), "Element with negative position has been added");
        newElement.setPosition(new Point(-100, 10));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Collection2DTest.collection2D.add(newElement), "Element with negative position has been added");
        assertThrows(IllegalArgumentException.class, () -> Collection2DTest.collection2D.add(null));
        newElement.setPosition(new Point(100, 10));
        newElement.setCollection(new Collection2D<>());
        assertThrows(IllegalArgumentException.class, () -> Collection2DTest.collection2D.add(newElement), "Element already in a collection has been added");
    }

    @Test
    void remove() {
        for (Collection2DElementTest elementTest : Collection2DTest.elements) {
            List<Collection2DElementTest> actualElementsAtPosition = collection2D.get(elementTest.getPosition());
            final int expectedSize = actualElementsAtPosition.size() - 1;

            collection2D.remove(elementTest);

            assertEquals(expectedSize, actualElementsAtPosition.size(), "Element list at position " + elementTest.position + " has not been removed");
            assertFalse(actualElementsAtPosition.contains(elementTest), "Element list at position " + elementTest.position + " still contains the element");
            assertThrows(IllegalArgumentException.class, () -> Collection2DTest.collection2D.remove(null), "Element with negative position has been added");
        }
    }

    @Test
    void contains() {
        for (Collection2DElementTest elementTest : Collection2DTest.elements) {
            assertTrue(collection2D.contains(elementTest), "Element " + elementTest + " not found in collection");
        }
        assertThrows(IllegalArgumentException.class, () -> Collection2DTest.collection2D.contains(null), "Element with negative position has been added");
    }

    @Test
    void toList() {
        assertNotNull(Collection2DTest.collection2D.toList(), "Collection2D toList is null");
        assertEquals(Collection2DTest.elements.size(), Collection2DTest.collection2D.toList().size(), "Collection2D toList size is not the same as the elements size");
        assertTrue(Collection2DTest.elements.containsAll(Collection2DTest.collection2D.toList()), "Collection2D toList does not contain all elements");
        assertTrue(Collection2DTest.collection2D.toList().containsAll(Collection2DTest.elements), "Collection2D toList does not contain all elements");
    }

    @Test
    void dimension() {
        assertEquals(new Dimension(NB_COLUMNS, NB_ROWS), collection2D.getDimension(), "Collection2D dimension is not the same as the elements size");
        Collection2DElementTest newElement = new Collection2DElementTest();
        newElement.setPosition(new Point(1000, 2000));
        Collection2DTest.collection2D.add(newElement);

        assertEquals(new Dimension(1000 + 1, 2000 + 1), Collection2DTest.collection2D.getDimension(), "Collection2D dimension is not the same as the elements size");
    }

    @Test
    void elementHasMoved() {
        Collection2DElementTest elementTest = Collection2DTest.elements.get(0);

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> elementTest.setPosition(new Point(-100, -10)), "Element with negative position has been added");
        elementTest.setPosition(new Point(100, 10));
        Collection2DTest.collection2D.add(elementTest);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> elementTest.setPosition(new Point(100, -10)), "Element with negative position has been added");
        elementTest.setPosition(new Point(100, 10));
        Collection2DTest.collection2D.add(elementTest);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> elementTest.setPosition(new Point(-100, 10)), "Element with negative position has been added");
    }

    @Test
    void isEmpty() {
        assertFalse(Collection2DTest.collection2D.isEmpty(), "Collection2D is empty");
        assertTrue(new Collection2D<Collection2DElementTest>().isEmpty(), "Collection2D is not empty");
    }

    @Getter
    private static class Collection2DElementTest implements ICollection2DElement<Collection2DElementTest> {
        private Point position;
        @Setter
        private Collection2D<Collection2DElementTest> collection;

        @Override
        public void setPosition(final Point newPosition) {
            final Point oldPosition = this.position;
            this.position = newPosition;

            if (this.collection != null) {
                this.collection.notifyElementHasMoved(this, oldPosition);
            }
        }
    }
}