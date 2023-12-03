import {Link, useLoaderData, useSearchParams} from "react-router-dom";
import {parseProduct} from "./parsing";
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {wait} from "@testing-library/user-event/dist/utils";


export async function productListLoader({request}) {

    const browserUrl = new URL(request.url);
    const filterString = browserUrl.searchParams.get('filter')
    const filterList = filterString != null
        ? filterString.split(',')
        : null

    const sortString = browserUrl.searchParams.get('sort')
    const sortList = sortString != null
        ? sortString.split(',')
        : null

    let backendUrl = "products"
    if (filterList != null || sortList != null) {
        backendUrl += '?'
    }

    if (filterList != null) {
        for (let i = 0; i < filterList.length; i++) {
            backendUrl += `filter=${filterList[i]}`
            if (i !== filterList.length - 1) backendUrl += "&"
        }
    }

    if (sortList != null) {
        backendUrl += '?'
        for (let i = 0; i < sortList.length; i++) {
            backendUrl += `sort=${sortList[i]}`
            if (i !== sortList.length - 1) backendUrl += "&"
        }
    }

    const response = await fetch(backendUrl)
    if (response.status !== 200) {
        return {isSuccess: false}
    }

    const text = await response.text()
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, "application/xml");
    const xml = doc.querySelectorAll("product")
    const productArray = []
    for (const product of xml) {
        productArray.push(parseProduct(product))
    }
    return {isSuccess: true, products: productArray}

}

export function ProductList() {
    const {isSuccess, products} = useLoaderData()
    let [searchParams, setSearchParams] = useSearchParams();


    return (
        <>
            <input type="text" onChange={event => {
                setSearchParams({"filter": event.target.value})
            }}/>
            <input type="text" onChange={event => {
                setSearchParams({"sort": event.target.value})
            }}/>
            {isSuccess === true
                ? <ProductTableView products={products}></ProductTableView>
                : <h1>Failed to fetch list of products</h1>
            }
        </>
    )
}

const filterRegex = "^(id|name|coordinates\\\\.x|coordinates\\\\.y|creationDate|price|manufactureCost|unitOfMeasure|org_id|org_name|org_fullName|org_annualTurnover|org_type|postalAddress_zipcode)-(eq|nq|gt|lt|gte|lte)-(.+)"

function ProductTableView({products}) {


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

