import {useEffect, useState} from "react";
import Card from '@mui/material/Card'
import {Box, CardContent, CardHeader, Typography} from "@mui/material";
import {CardView, parseProduct} from "./utils";


export function ProductList() {
    const [products, setProducts] = useState([])
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        window.fetch("/products")
            .then(response => response.text())
            .then(text => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(text, "application/xml");
                const xml = doc.querySelectorAll("product")
                const productArray = []
                for (const product of xml) {
                    productArray.push(parseProduct(product))
                }
                setProducts(productArray)
            })

    }, []);

    return (
        <Box sx={{
            display: 'inline-flex',
            flexDirection: 'column',
        }}>
            {products.map(product =>
                <CardView key={product.id} product={product}/>
            )}
        </Box>
    )

}

