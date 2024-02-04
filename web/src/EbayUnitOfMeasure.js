import React from "react";
import {ProductTableView} from "./ProductList";
import {unitOfMeasureEnum} from "./SingleEditableProduct";
import {EBAY_PREFIX} from "./index";
import {parseProducts} from "./parsing";
import {useLoaderData, useSearchParams} from "react-router-dom";

const UNIT_OF_MEASURE_URL_PARAM = 'unit'
export async function productListWithUnitOfMeasureLoader({request}) {
    const browserUrl = new URL(request.url);
    let unitOfMeasure = browserUrl.searchParams.get(UNIT_OF_MEASURE_URL_PARAM)
    if (unitOfMeasure == null) {
       unitOfMeasure = unitOfMeasureEnum.METERS
    }

    const response = await fetch(`${EBAY_PREFIX}/api/filter/unit-of-measure/${unitOfMeasure}`)
    if (response.status !== 200) {
        return {isSuccess: false}
    }

    const text = await response.text()
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, "application/xml");
    const productArray = parseProducts(doc);

    return {isSuccess: true, products: productArray, unitOfMeasure }
}

export function EbayUnitOfMeasure() {
    const {isSuccess, products, unitOfMeasure} = useLoaderData()

    const [_, setSearchParams] = useSearchParams();

    function setUnitOfMeasure(nextUnit) {
        setSearchParams(prev => {
            prev.set(UNIT_OF_MEASURE_URL_PARAM, nextUnit)
            return prev;
        })
    }

    return (
        <div>
            <h2>Unit of Measure</h2>
            <select
                value={unitOfMeasure}
                onChange={field => setUnitOfMeasure(field.target.value)}
            >
                <option value={unitOfMeasureEnum.METERS}>METERS</option>
                <option value={unitOfMeasureEnum.PCS}>PCS</option>
                <option value={unitOfMeasureEnum.MILLIGRAMS}>MILLIGRAMS</option>
                <option value={unitOfMeasureEnum.MILLILITERS}>MILLILITERS</option>
                <option value={unitOfMeasureEnum.SQUARE_METERS}>SQUARE_METERS</option>
            </select>
            <ProductTableView products={products} />
        </div>
    )
}