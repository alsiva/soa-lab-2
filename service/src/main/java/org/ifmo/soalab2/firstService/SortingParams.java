package org.ifmo.soalab2.firstService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SortingParams {
    product_id,
    name,
    coordinate_x,
    coordinate_y,
    creationDate,
    price,
    manufactureCost,
    unitOfMeasure,
    org_id,
    org_name,
    org_fullName,
    org_annualTurnover,
    org_type,
    postalAddress_zipcode;

    private static final Map<String, SortingParams> enumMap = Arrays.stream(SortingParams.values())
            .collect(Collectors.toMap(param -> param.name().toLowerCase(), Function.identity()));


    public static List<SortingPair> parseSortingParams(List<String> sortingParams) {
        return sortingParams.stream().map(paramAsString -> {
            String[] paramAndDirection = paramAsString.toLowerCase().split("-", 2);
            String paramName = paramAndDirection[0];
            if (paramName == null) {
                throw new IllegalArgumentException("No sorting param : " + paramAsString);
            }
            SortingParams sortingParam = enumMap.get(paramName);
            if (sortingParam == null) {
                throw new IllegalArgumentException("No such sorting param: " + paramAsString);
            }

            String directionAsString = paramAndDirection[1];

            SortingDirection direction = ("desc".equalsIgnoreCase(directionAsString))
                    ? SortingDirection.DESC
                    : SortingDirection.ASC;

            return new SortingPair(direction, sortingParam);

        }).collect(Collectors.toList());
    }
}
