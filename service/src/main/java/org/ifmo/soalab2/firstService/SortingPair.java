package org.ifmo.soalab2.firstService;

public class SortingPair {
    private final SortingDirection direction;
    private final SortingParams param;

    public SortingPair(SortingDirection direction, SortingParams param) {
        this.direction = direction;
        this.param = param;
    }

    public SortingDirection getDirection() {
        return direction;
    }

    public SortingParams getParam() {
        return param;
    }
}
