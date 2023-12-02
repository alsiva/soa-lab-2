import {Link, useLoaderData, useSearchParams} from "react-router-dom";
import {parseProduct} from "./parsing";
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {wait} from "@testing-library/user-event/dist/utils";


export async function productListLoader({request}) {
    const browserUrl = new URL(request.url);

    console.log(request.url)
    let filterQuery = ""


    let filterString = browserUrl.searchParams.get('filter')
    filterString = "id==1"


    const name = ''
    if (name !== undefined) {
        filterQuery += `name==${name}`
    }

    let backendUrl = "/products?"
    if (filterQuery !== "") {
        backendUrl += filterQuery
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

    if (!isSuccess) {
        return (
            <h1>Failed to fetch list of products</h1>
        )
    }


    return (
        <ProductTableView products={products}></ProductTableView>
    )
}

const filterRegex = "^(id|name|coordinates\\\\.x|coordinates\\\\.y|creationDate|location\\\\.id|location\\\\.x|location\\\\.y|location\\\\.name|distance)(==|!=|>=|<=|>|<)(.+)"

function parseFilterString(value) {
    let exampleString = value
    exampleString = exampleString.replaceAll(" ", "").split(",")
    let filterArray = []
    const regex = new RegExp(filterRegex)
    exampleString.forEach((filter) => {
        if (regex.test(filter)) {
            const result = regex.exec(filter)
            filterArray.push(`${result[1] + result[2] + result[3]}`)
        }
    })

    let result = "?"
    filterArray.forEach((filter, key) => {
        result += `filter=${filter}`
        if (filterArray.length > 1 && key !== filterArray.length-1) {
            result += "&"
        }
    })

    return result
}

/*
const filter = [
    {
        field: "name",
        comparasion: "==",
        value: "Alex"
    },
    {
        field: "id",
        comparasion: "==",
        value: "1"
    }
]

 */

function ProductTableView({products}) {

    let [searchParams, setSearchParams] = useSearchParams();

    return (
        <>
            <input type="text" onChange={event => {
                let parsedFilter = parseFilterString(event.currentTarget.value)
                setSearchParams({ "filter": "Dispenser==a"})
                console.log(searchParams.get("filter"))
            }}/>
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
        </>
    )
}

