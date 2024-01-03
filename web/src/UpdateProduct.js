import {useLoaderData} from "react-router-dom";
import {parseProduct} from "./parsing";
import {Alert, Box, Button, CardContent, CardHeader, CircularProgress, Typography} from "@mui/material";
import Card from "@mui/material/Card";
import {useState} from "react";
import dayjs from "dayjs";
import {serializeProduct} from "./serializing";
import {SingleEditableProduct} from "./SingleEditableProduct";
import {sleep} from "./utils";

export async function productLoader({params}) {
    const {productId} = params;
    if (productId === null || isNaN(productId)) {
        return {isSuccess: false, errorMsg: `invalid product id: ${productId}`}
    }

    const response = await fetch(`/api/products/${productId}`)

    if (response.status === 404) {
        return {isSuccess: false, errorMsg: `No product with id ${productId}`}
    }

    const parser = new DOMParser()
    const doc = parser.parseFromString(await response.text(), "application/xml")
    const product = doc.querySelector("Product")

    return {isSuccess: true, product: parseProduct(product)}
}

export function UpdateProduct() {

    async function updateProduct(nextProduct) {
        const serializedProduct = serializeProduct(nextProduct)

        setIsLoading(true)
        await sleep(2000)
        const response = await fetch(`/api/products/${nextProduct.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/xml'
            },
            body: serializedProduct
        })

        setIsLoading(false)
        setStatus(response.status)
    }

    const {isSuccess, errorMsg, product} = useLoaderData()
    const [status, setStatus] = useState(-1)
    const [isLoading, setIsLoading] = useState(false)

    if (!isSuccess) {
        return (
            <div>{errorMsg}</div>
        )
    }

    return (
        <>
            <div>
                {(() => {
                    if (status === -1) {

                    } else if (status === 200) {
                        return (
                            <Alert severity="success">Продукт был обновлён успешно</Alert>
                        );
                    } else if (status === 404) {
                        return (
                            <Alert severity="warning">Не получилось обновить продукт</Alert>
                        );
                    } else {
                        return (
                            <Alert severity="error">Следующий статус {status}</Alert>
                        );
                    }
                })()}
            </div>
            <SingleEditableProduct buttonText={"Изменить продукт"} product={product} callback={updateProduct} isLoading={isLoading}></SingleEditableProduct>
        </>
    )
}
