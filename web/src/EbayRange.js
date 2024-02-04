import React, {useState} from "react";
import {ProductTableView} from "./ProductList";
import {EBAY_PREFIX} from "./index";
import {parseProducts} from "./parsing";
import {useLoaderData, useSearchParams} from "react-router-dom";

const MIN_URL_PARAM = 'min'
const MAX_URL_PARAM = 'max'


export async function productListWithPriceRangeLoader({request}) {
    const browserUrl = new URL(request.url);
    let min = browserUrl.searchParams.get(MIN_URL_PARAM)
    let max = browserUrl.searchParams.get(MAX_URL_PARAM)

    if (min == null) {
        min = 0
    }
    if (max == null) {
        max = 2000
    }

    const response = await fetch(`${EBAY_PREFIX}/api/filter/price/${min}/${max}`)
    if (response.status !== 200) {
        return {isSuccess: false}
    }

    const text = await response.text()
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, "application/xml");
    const productArray = parseProducts(doc);

    return {isSuccess: true, products: productArray, min, max}
}

function isNumberValid(numberAsString) {
    const number = Number.parseInt(numberAsString)
    return !(Number.isNaN(number) || number < 0);

}

export function EbayRange() {
    const {isSuccess, products, min: urlMin, max: urlMax} = useLoaderData()

    const [min, setMin] = useState(urlMin);
    const [max, setMax] = useState(urlMax);

    const [_, setSearchParams] = useSearchParams();

    function updateMin(nextMin) {
        setMin(nextMin)
        if (!isNumberValid(nextMin)) {
            return
        }

        setSearchParams(prev => {
            prev.set(MIN_URL_PARAM, nextMin)
            return prev;
        })
    }

    function updateMax(nextMax) {
        setMax(nextMax)
        if (!isNumberValid(nextMax)) {
            return
        }

        setSearchParams(prev => {
            prev.set(MAX_URL_PARAM, nextMax)
            return prev;
        })
    }

    return (
        <div>
            <div>MinRange</div>
            <input value={min} type="number" onChange={event => updateMin(event.target.value)}/>
            <div>MaxRange</div>
            <input value={max} type="number" onChange={event => updateMax(event.target.value)}/>
            <ProductTableView products={products}/>
        </div>
    )
}