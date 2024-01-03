import {parseProduct} from "./parsing";
import {useLoaderData} from "react-router-dom";
import {useState} from "react";
import {Alert, Button, CardContent, CardHeader, TextField, Typography} from "@mui/material";
import Card from "@mui/material/Card";
import {SingleReadableProduct} from "./SingleReadableProduct";
import {SERVICE_PREFIX} from "./index";

export async function productWithMaxUnitOfMeasureLoader() {
    const response = await fetch(`${SERVICE_PREFIX}/api/products/max-measure`)

    if (response.status === 404) {
        return {isSuccess: false, errorMsg: "Couldn't find product"}
    }

    const parser = new DOMParser()
    const doc = parser.parseFromString(await response.text(), "application/xml")
    const product = doc.querySelector("Product")


    return {isSuccess: true, product: parseProduct(product)}
}

export function ProductWithMaxUnitOfMeasure() {

    const {isSuccess, errorMsg, product} = useLoaderData()

    if (!isSuccess) {
        return (
            <div>{errorMsg}</div>
        )
    }

    return (
        SingleReadableProduct(product)
    )
}