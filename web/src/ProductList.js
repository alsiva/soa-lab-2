import {Link, useLoaderData, useSearchParams} from "react-router-dom";
import {parseProducts} from "./parsing";
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField} from "@mui/material";


export async function productListLoader({request}) {
    const browserUrl = new URL(request.url);

    const response = await fetch(`/products?${browserUrl.searchParams.toString()}`)
    if (response.status !== 200) {
        return {isSuccess: false}
    }

    const text = await response.text()
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, "application/xml");
    const productArray = parseProducts(doc);

    return {isSuccess: true, products: productArray}

}

export function ProductList() {
    const {isSuccess, products} = useLoaderData()
    let [_, setSearchParams] = useSearchParams();

    function changeFilter(nextFilterName, nextOperator, event) {
        const nextFilterValue = event.target.value.trim()

        setSearchParams(prev => {
            const currentFilters = prev.getAll('filter')

            const filterMap = new Map()
            for (const filter of currentFilters) {
                const [currentFilterName, operator, filterValue] = filter.split('-', 3)
                filterMap.set(currentFilterName, {
                    operator,
                    filterValue,
                })
            }

            if (nextFilterValue === '') {
                filterMap.delete(nextFilterName)
            } else {
                filterMap.set(nextFilterName, {
                    operator: nextOperator,
                    filterValue: nextFilterValue
                })
            }

            prev.delete('filter')
            for (const entry of filterMap.entries()) {
                const [filterName, filterOpAndValue] = entry;
                const { operator, filterValue } = filterOpAndValue;

                prev.append('filter', `${filterName}-${operator}-${filterValue}`)
            }

            return prev
        })
    }

    return (
        <>
            <TextField id="name" label="name" size="small" variant="outlined" onChange={event => {
                changeFilter('name', 'eq', event)
            }}/>
            <label>price
                <input type="text" onChange={event => {
                    changeFilter('price', 'lt', event)
                }}/>
            </label>
            {isSuccess === true
                ? <ProductTableView products={products}></ProductTableView>
                : <h1>Failed to fetch list of products</h1>
            }
        </>
    )
}

function ProductTableView({products}) {
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

