import {useEffect, useState} from "react";
import {CardView, parseProduct} from "./utils";

export function GetTrip({productId}) {
    const [product, setProduct] = useState(null)

    useEffect(() => {
        (async () => {

            if (productId === null || isNaN(productId)) {
                return
            }

            const response = await fetch(`/products/${productId}`)
            if (response.status === 404) {
                return
            }

            const parser = new DOMParser()
            const doc = parser.parseFromString(await response.text(), "application/xml")
            const product = doc.querySelector("Product")

            console.log(product)


            setProduct(parseProduct(product))

        })()
    }, []);

    if (isNaN(productId)) {
        return (
            <div>User eblan exception</div>
        )
    } else if (product === null) {
        return (
            <div>No product</div>
        )
    } else {
        return (
            <CardView product={product}/>

        )
    }


}