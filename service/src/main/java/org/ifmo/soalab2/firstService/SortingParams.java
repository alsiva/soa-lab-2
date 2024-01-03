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
    postalAddress_zipcode,
    desc_product_id,
    desc_name,
    desc_coordinate_x,
    desc_coordinate_y,
    desc_creationDate,
    desc_price,
    desc_manufactureCost,
    desc_unitOfMeasure,
    desc_org_id,
    desc_org_name,
    desc_org_fullName,
    desc_org_annualTurnover,
    desc_org_type,
    desc_postalAddress_zipcode;

    private static final Map<String, SortingParams> enumMap = Arrays.stream(SortingParams.values())
            .collect(Collectors.toMap(param -> param.name().toLowerCase(), Function.identity()));


    public static List<SortingParams> parseSortingParams(List<String> sortingParams) {
        return sortingParams.stream().map(paramAsString -> {
            SortingParams sortingParam = enumMap.get(paramAsString.toLowerCase());
            if (sortingParam == null) {
                throw new IllegalArgumentException("No such sorting param: " + paramAsString);
            }
            return sortingParam;
        }).collect(Collectors.toList());
    }
}
