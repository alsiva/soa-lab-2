import {Link, useLoaderData, useSearchParams} from "react-router-dom";
import {parseProducts} from "./parsing";
import {
    Box, Button,
    FormControl,
    InputLabel, Menu,
    MenuItem,
    Select,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField
} from "@mui/material";
import React, {useState} from "react";


const filterFields = [
    "id",
    "name",
    "coordinates.x",
    "coordinates.y",
    "creationDate",
    "price",
    "manufactureCost",
    "unitOfMeasure",
    "org_id",
    "org_name",
    "org_annualTurnover",
    "org_type",
    "postalAddress_zipcode"
]

export async function productListLoader({request}) {
    const browserUrl = new URL(request.url);

    const response = await fetch(`/api/products?${browserUrl.searchParams.toString()}`)
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
    const [showFilter, setShowFilter] = useState(false)
    const [_, setSearchParams] = useSearchParams();

    function changeFilter(nextFilterName, nextOperator, nextValue) {
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

            if (nextValue === '' || nextOperator === null || nextOperator === '') {
                filterMap.delete(nextFilterName)
            } else {
                filterMap.set(nextFilterName, {
                    operator: nextOperator,
                    filterValue: nextValue
                })
            }

            prev.delete('filter')
            for (const entry of filterMap.entries()) {
                const [filterName, filterOpAndValue] = entry;
                const {operator, filterValue} = filterOpAndValue;

                prev.append('filter', `${filterName}-${operator}-${filterValue}`)
            }

            return prev
        })
    }

    return (
        <>
            {showFilter && (
                <div className="filterAndSortContainer">
                    <div className="filterContainer">
                        {filterFields.map(filterField => (
                            <SingleFilter
                                key={filterField}
                                changeFunc={changeFilter}
                                field={filterField}
                            />
                        ))}
                    </div>
                    <div className="sortContainer">
                        <h1>Здесь будет будущая сортировка</h1>
                    </div>
                </div>
            )}

            <Button onClick={() => setShowFilter(!showFilter)}>Toggle filter</Button>
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

function SingleFilter({changeFunc, field}) {
    const [operator, setOperator] = useState(null);
    const [value, setValue] = useState(null)

    const handleOperatorChange = (event) => {
        setOperator(event.target.value);
        changeFunc(field, event.target.value, value)
    };

    const handleValueChange = (event) => {
        setValue(event.target.value);
        changeFunc(field, operator, event.target.value)
    };

    const id = `operator-label-${field}`

    return (
        <div className="singleFilter">
            <div>
                {field}
            </div>

            <FormControl fullWidth>
                <InputLabel id={id}>Operator</InputLabel>
                <Select
                    labelId={id}
                    id={id}
                    value={operator}
                    label={field}
                    size="small"
                    onChange={handleOperatorChange}
                >
                    <MenuItem value={null}>None</MenuItem>
                    <MenuItem value={'eq'}>Equals</MenuItem>
                    <MenuItem value={'nq'}>Not equals</MenuItem>
                    <MenuItem value={'gt'}>Greater than</MenuItem>
                    <MenuItem value={'lt'}>Lower than</MenuItem>
                    <MenuItem value={'gte'}>Greater than or equals</MenuItem>
                    <MenuItem value={'lte'}>Lower than or equals</MenuItem>
                </Select>
            </FormControl>
            <TextField id={field} label={field} size="small" variant="outlined" onChange={handleValueChange}/>
        </div>
    );
}

