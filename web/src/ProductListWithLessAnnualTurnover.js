import {parseProducts} from "./parsing";
import {Link, useLoaderData} from "react-router-dom";
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import React from "react";
import {SERVICE_PREFIX} from "./index";

export async function productListWithLessAnnualTurnoverLoader(request) {
    let maxAnnualTurnover = request.params.maxAnnualTurnover

    const response = await fetch(`${SERVICE_PREFIX}/api/products/max-annual-turnover/${maxAnnualTurnover}`)
    if (response.status !== 200) {
        return {isSuccess: false, errorMsg: response.statusText}
    }

    const text = await response.text()
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, "application/xml");
    const productArray = parseProducts(doc);

    return { isSuccess: true, products: productArray }
}

export function ProductListWithLessAnnualTurnover() {
    const {isSuccess, errorMsg, products} = useLoaderData()

    if (!isSuccess) {
        return (
            <div>{errorMsg}</div>
        )
    }

    if (products.length === 0) {
        return (
            <h2>Поиск не выдал подходящих продуктов. Попробуйте ослабить фильтрацию.</h2>
        )
    }


    return (
        <TableContainer>
            <Table sx={{minWidth: 650}} aria-label="Products table">
                <TableHead>
                    <TableRow>
                        <TableCell>Id</TableCell>
                        <TableCell>Name</TableCell>
                        <TableCell>Coordinates</TableCell>
                        <TableCell>Price</TableCell>
                        <TableCell>ManufactureCost</TableCell>
                        <TableCell>UnitOfMeasure</TableCell>
                        <TableCell>Organization</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {products.map(product =>
                        <TableRow
                            key={product.id}
                            sx={{'&:last-child td, &:last-child th': {border: 0}}}
                        >
                            <TableCell>{product.id}</TableCell>
                            <TableCell>
                                <Link to={product.id.toString()}>{product.name}</Link>
                            </TableCell>
                            <TableCell>({product.coordinates.x}, {product.coordinates.y})</TableCell>
                            <TableCell>{product.price}</TableCell>
                            <TableCell>{product.manufactureCost}</TableCell>
                            <TableCell>{product.unitOfMeasure}</TableCell>
                            <TableCell>{product.organization.name}</TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </TableContainer>

    )
}