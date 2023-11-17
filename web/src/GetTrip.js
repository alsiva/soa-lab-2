import {useEffect, useState} from "react";
import {CardView, parseProduct} from "./utils";

export function GetTrip({productId}) {
    const [product, setProduct] = useState(null)

    useEffect(() => {
        window.fetch(`/products/${productId}`)
            .then(response => response.text())
            .then(text => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(text, "application/xml");
                const product = doc.querySelector("Product")
                setProduct(parseProduct(product))
                console.log(productId)
            })

    }, []);

    return (
        <CardView product={product}></CardView>
    )

}