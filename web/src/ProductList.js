import {Link, useLoaderData, useSearchParams} from "react-router-dom";
import {parseProducts} from "./parsing";
import {
    Alert,
    Box, Button, ButtonGroup, Chip,
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
import {serializeProduct} from "./serializing";
import {sleep} from "./utils";
import {EBAY_PREFIX, SERVICE_PREFIX} from "./index";


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

const sortFields = [
    "product_id",
    "name",
    "coordinate_x",
    "coordinate_y",
    "creationDate",
    "price",
    "manufactureCost",
    "unitOfMeasure",
    "org_id",
    "org_name",
    "org_fullName",
    "org_annualTurnover",
    "org_type",
    "postalAddress_zipcode",
    "desc_product_id",
    "desc_name",
    "desc_coordinate_x",
    "desc_coordinate_y",
    "desc_creationDate",
    "desc_price",
    "desc_manufactureCost",
    "desc_unitOfMeasure",
    "desc_org_id",
    "desc_org_name",
    "desc_org_fullName",
    "desc_org_annualTurnover",
    "desc_org_type",
    "desc_postalAddress_zipcode"
]

export async function productListLoader({request}) {
    const browserUrl = new URL(request.url);

    const response = await fetch(`${SERVICE_PREFIX}/api/products?${browserUrl.searchParams.toString()}`)
    if (response.status !== 200) {
        return {isSuccess: false}
    }

    const text = await response.text()
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, "application/xml");
    const productArray = parseProducts(doc);

    return {isSuccess: true, products: productArray}

}

export async function productListWithPriceRangeLoader({request}) {
    const browserUrl = new URL(request.url);
    const min = browserUrl.toString().split('/').slice(-2)[0]
    const max = browserUrl.toString().split('/').slice(-2)[1]

    const response = await fetch(`${EBAY_PREFIX}/api/filter/price/${min}/${max}`)
    if (response.status !== 200) {
        return {isSuccess: false}
    }

    const text = await response.text()
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, "application/xml");
    const productArray = parseProducts(doc);

    return {isSuccess: true, products: productArray}
}

export async function productListWithUnitOfMeasureLoader({request}) {
    const response = await fetch(`${EBAY_PREFIX}/api/filter/unit-of-measure/METERS`)
    if (response.status !== 200) {
        return {isSuccess: false}
    }

    const text = await response.text()
    const parser = new DOMParser();
    const doc = parser.parseFromString(text, "application/xml");
    const productArray = parseProducts(doc);

    return {isSuccess: true, products: productArray}
}


function parseFilterMap(currentFilters) {
    const filterMap = new Map()
    for (const filter of currentFilters) {
        const [currentFilterName, operator, filterValue] = filter.split('-', 3)

        filterMap.set(currentFilterName, {
            operator,
            filterValue,
        })
    }
    return filterMap;
}

export function ProductList() {
    const {isSuccess, products} = useLoaderData()
    const [showFilter, setShowFilter] = useState(false)
    const [searchParams, setSearchParams] = useSearchParams();
    const [loading, setIsLoading] = useState(false)
    const [deleteStatus, setDeleteStatus] = useState(-1)

    const [page, setPage] = useState({
        pageIndex: null,
        pageSize: null
    })

    const currentFilterMap = parseFilterMap(searchParams.getAll('filter'));
    const unselectedFilters = filterFields.filter(field => !currentFilterMap.has(field))

    async function deleteProduct(productId) {
        setIsLoading(true)
        const response = await fetch(`${SERVICE_PREFIX}/api/products/${productId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/xml'
            },
        })
        setIsLoading(false)
        setDeleteStatus(response.status)

        if (response.status === 200) {

        }
    }

    function updateFilterInUrl(filterMap) {
        setSearchParams(prev => {
            prev.delete('filter')

            for (const entry of filterMap.entries()) {
                const [filterName, filterOpAndValue] = entry;
                const {operator, filterValue} = filterOpAndValue;

                prev.append('filter', `${filterName}-${operator}-${filterValue.trim()}`)
            }

            return prev
        })
    }

    function changeSort(nextSortOperator, desc) {
        setSearchParams(prev => {
            const currentSort = prev.getAll('sort')
            //Вернёт массив
            console.log(`Current sort is\n${currentSort}`)

            //Первое что нужно сделать это найти повторения и удалить их
            let next_sort = []
            for (const sort of currentSort) {
                const usual_sort = sort.replace("desc_", "")
                const usual_next_sort = nextSortOperator.replace("desc_", "")
                if (usual_sort !== usual_next_sort) {
                    next_sort.push(sort)
                }
            }

            if (desc !== null) {
                if (desc) {
                    next_sort.push(`desc_${nextSortOperator}`)
                } else if (!desc) {
                    next_sort.push(`${nextSortOperator}`)
                }
            }
            console.log(next_sort)

            prev.delete('sort')
            for (const sort of next_sort) {
                prev.append('sort' ,sort)
            }

            return prev
        })
    }

    function changePagination(nextPageSize, nextPageIndex) {
        setSearchParams(prev => {

            if (nextPageSize >= 1 && nextPageIndex >= 0) {
                prev.set('pageSize', nextPageSize)
                prev.set('pageIndex', nextPageIndex)
            } else {
                prev.delete('pageSize')
                prev.delete('pageIndex')
            }

            return prev
        })
    }


    return (
        <>
            {showFilter && (
                <div className="filterAndSortContainer">
                    <div className="unselected-filters">
                        <h3>Add filters:</h3>
                        {unselectedFilters.map(filter => (
                            <Chip key={filter} label={filter} onClick={() => {
                                const nextFilterMap = new Map(currentFilterMap)
                                nextFilterMap.set(filter, {
                                    operator: 'eq',
                                    filterValue: '',
                                })
                                updateFilterInUrl(nextFilterMap)
                            }} />
                        ))}
                    </div>
                    <div>
                        {Array.from(currentFilterMap).map(([filter, { operator, filterValue }]) => (
                            <SingleFilterControlled
                                key={filter}
                                field={filter}
                                operator={operator}
                                handleOperatorChange={nextOp => {
                                    const nextFilterMap = new Map(currentFilterMap);
                                    nextFilterMap.set(filter, {
                                        operator: nextOp,
                                        filterValue: filterValue,
                                    });
                                    updateFilterInUrl(nextFilterMap);
                                }}
                                handleValueChange={nextVal => {
                                    const nextFilterMap = new Map(currentFilterMap);
                                    nextFilterMap.set(filter, {
                                        operator: operator,
                                        filterValue: nextVal,
                                    });
                                    updateFilterInUrl(nextFilterMap);
                                }}
                                handleDelete={() => {
                                    const nextFilterMap = new Map(currentFilterMap)
                                    nextFilterMap.delete(filter)
                                    updateFilterInUrl(nextFilterMap)
                                }}
                            />
                        ))}
                    </div>
                    <div>
                        {sortFields.map((sortElement, index) => {
                            if (index < sortFields.length / 2) {
                                return (
                                    <SingleSort sortElement={sortElement} changeFunc={changeSort}></SingleSort>
                                )
                            }
                        })}
                    </div>
                    <div>
                        <label>Page index</label>
                        <input type="number" onChange={event => {
                            setPage(oldPage => ({
                                ...oldPage,
                                pageIndex: event.target.value
                            }));

                            changePagination(page.pageSize, event.target.value)
                            // Call your function here
                            // For example: yourFunctionAfterSetPage();
                        }}/>
                        <label>Page size</label>
                        <input type="number" onChange={event => {
                            setPage(oldPage => ({
                                ...oldPage,
                                pageSize: event.target.value
                            }));
                            changePagination(event.target.value, page.pageIndex)
                        }}/>
                    </div>
                </div>
            )}

            <Button onClick={() => setShowFilter(!showFilter)}>Toggle filter</Button>

            {isSuccess === true
                ? <>
                    {(() => {
                        if (deleteStatus === -1) {

                        } else if (deleteStatus === 200) {
                            return (
                                <Alert severity="success">Продукт был удалён успешно</Alert>
                            );
                        } else if (deleteStatus === 404) {
                            return (
                                <Alert severity="warning">Не получилось удалить продукт</Alert>
                            );
                        } else {
                            return (
                                <Alert severity="error">Следующий статус {deleteStatus}</Alert>
                            );
                        }
                    })()}
                    <ProductTableView products={products} deleteProduct={deleteProduct}></ProductTableView>
                </>
                : <h1>Failed to fetch list of products</h1>
            }
        </>
    )
}

function ProductTableView({products, deleteProduct}) {
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
                        <TableCell>DeleteRow</TableCell>
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
                            <TableCell>
                                <Button variant="outlined" color="error" onClick={() => deleteProduct(product.id)}>
                                    Удалить продукт
                                </Button>
                            </TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </TableContainer>

    )
}

function SingleFilterControlled({field, operator, handleOperatorChange, handleValueChange, handleDelete}) {
    const id = `operator-label-${field}`

    return (
        <div className="singleFilter">
            <div>{field}</div>

            <FormControl fullWidth>
                <InputLabel id={id}>Operator</InputLabel>
                <Select
                    labelId={id}
                    id={id}
                    value={operator}
                    label={field}
                    size="small"
                    onChange={e => {
                        handleOperatorChange(e.target.value);
                    }}
                >
                    <MenuItem value={'eq'}>Equals</MenuItem>
                    <MenuItem value={'nq'}>Not equals</MenuItem>
                    <MenuItem value={'gt'}>Greater than</MenuItem>
                    <MenuItem value={'lt'}>Lower than</MenuItem>
                    <MenuItem value={'gte'}>Greater than or equals</MenuItem>
                    <MenuItem value={'lte'}>Lower than or equals</MenuItem>
                </Select>
            </FormControl>
            <TextField
                id={field}
                label={field}
                size="small"
                variant="outlined"
                onChange={e => {
                    handleValueChange(e.target.value)
                }}
            />
            <Button variant="outlined" color="error" onClick={handleDelete}>Delete</Button>
        </div>
    );
}

function SingleSort({sortElement, changeFunc}) {

    const [desc, setDesc] = useState(null);


    const handleValueChange = (event) => {
        setDesc(event.target.value);
        changeFunc(sortElement, event.target.value)
    };


    const id = `operator-label-${sortElement}`

    return (
        <div>
            <FormControl fullWidth>
                <InputLabel id={id}>Operator</InputLabel>
                <Select
                    labelId={id}
                    id={id}
                    value={desc}
                    label={sortElement}
                    size="small"
                    onChange={handleValueChange}
                >
                    <MenuItem value={null}>None</MenuItem>
                    <MenuItem value={false}>Normal</MenuItem>
                    <MenuItem value={true}>Desc</MenuItem>
                </Select>
            </FormControl>
            <div>{sortElement}</div>

        </div>
    )

}

