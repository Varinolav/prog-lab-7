package ru.varino.common.models.modelUtility.builders;

import ru.varino.common.models.Coordinates;

import java.math.BigDecimal;

/**
 * Билдер координат
 */
public class CoordinatesBuilder implements Builder<Coordinates> {
    private BigDecimal x;
    private BigDecimal y;


    public BigDecimal getX() {
        return x;
    }

    public CoordinatesBuilder buildX(BigDecimal x) {
        if (x == null) throw new IllegalArgumentException("X не может быть null");
        this.x = x;
        return this;
    }

    public BigDecimal getY() {
        return y;
    }

    public CoordinatesBuilder buildY(BigDecimal y) {
        if (y == null) throw new IllegalArgumentException("Y не может быть null");
        if (y.compareTo(new BigDecimal(522)) > 0)
            throw new IllegalArgumentException("Максимальное значение поля Y: 522");

        this.y = y;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinates build() {
        return new Coordinates(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.x = new BigDecimal(0);
        this.y = new BigDecimal(0);

    }
}
