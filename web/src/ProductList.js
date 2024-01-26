import {Link, useLoaderData, useSearchParams} from "react-router-dom";
import {parseProducts} from "./parsing";
import {
    Alert,
    Button,
    Chip,
    FormControl,
    InputLabel,
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

function parseSortingMap(currentOperators) {
    const sortingMap = new Map()
    for (const filter of currentOperators) {
        const [parameter, direction] = filter.split('-', 3)

        // const isAsc = direction == null || direction.trim().toLowerCase() === 'asc'

        sortingMap.set(parameter, direction)
    }
    return sortingMap;
}

const PAGE_SIZE_URL_PARAM = 'pageSize'
const PAGE_INDEX_URL_PARAM = 'pageIndex'

export function ProductList() {
    const {isSuccess, products} = useLoaderData()
    const [showFilter, setShowFilter] = useState(false)
    const [searchParams, setSearchParams] = useSearchParams();
    const [loading, setIsLoading] = useState(false)
    const [deleteStatus, setDeleteStatus] = useState(-1)

    const pageIndex = searchParams.get(PAGE_INDEX_URL_PARAM)
    const pageSize = searchParams.get(PAGE_SIZE_URL_PARAM);

    const currentFilterMap = parseFilterMap(searchParams.getAll('filter'));
    const unselectedFilters = filterFields.filter(field => !currentFilterMap.has(field))

    const currentSortingMap = parseSortingMap(searchParams.getAll('sort'))
    const unselectedSortingParams = sortFields.filter(field => !currentSortingMap.has(field))

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
            window.location.reload()
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

    function updateSortingInUrl(sortingMap) {
        setSearchParams(prev => {
            prev.delete('sort')

            for (const entry of sortingMap.entries()) {
                const [param, direction] = entry;

                prev.append('sort', `${param}-${direction}`)
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
                            }}/>
                        ))}
                    </div>
                    <div>
                        {Array.from(currentFilterMap).map(([filter, {operator, filterValue}]) => (
                            <SingleFilterControlled
                                key={filter}
                                field={filter}
                                operator={operator}
                                value={filterValue}
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
                    <div className="unselected-filters">
                        <h3>Add sorting:</h3>
                        {unselectedSortingParams.map(param => (
                            <Chip key={param} label={param} onClick={() => {
                                const nextSortingMap = new Map(currentSortingMap)
                                nextSortingMap.set(param, 'asc')
                                updateSortingInUrl(nextSortingMap)
                            }}/>
                        ))}
                    </div>
                    <div>
                        {Array.from(currentSortingMap).map(([param, direction]) => {
                            return (
                                <SingleSort
                                    param={param}
                                    direction={direction}
                                    changeFunc={(nextDirection) => {
                                        const nextSortingMap = new Map(currentSortingMap)
                                        nextSortingMap.set(param, nextDirection)
                                        updateSortingInUrl(nextSortingMap)
                                    }}
                                    handleDelete={() => {
                                        const nextSortingMap = new Map(currentSortingMap)
                                        nextSortingMap.delete(param)
                                        updateSortingInUrl(nextSortingMap)
                                    }}
                                />
                            )
                        })}
                    </div>
                    <div>
                        <label>Page index</label>
                        <input type="number" value={pageIndex} onChange={event => {
                            const nextPageIndex = Number(event.target.value.trim());

                            if (Number.isNaN(nextPageIndex) || nextPageIndex <= 0) {
                                setSearchParams(prev => {
                                    prev.delete(PAGE_INDEX_URL_PARAM)
                                    return prev
                                })
                            } else {
                                setSearchParams(prev => {
                                    prev.set(PAGE_INDEX_URL_PARAM, nextPageIndex.toString())
                                    return prev
                                })
                            }
                        }}/>
                        <label>Page size</label>
                        <input type="number" value={pageSize} onChange={event => {
                            const nextPageSize = Number(event.target.value.trim());

                            if (Number.isNaN(nextPageSize) || nextPageSize <= 0) {
                                setSearchParams(prev => {
                                    prev.delete(PAGE_SIZE_URL_PARAM)
                                    return prev
                                })
                            } else {
                                setSearchParams(prev => {
                                    prev.set(PAGE_SIZE_URL_PARAM, nextPageSize.toString())
                                    return prev
                                })
                            }
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
                                <Link to={`/web/service/products/${product.id}`}>{product.name}</Link>
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

function SingleFilterControlled({field, operator, handleOperatorChange, value, handleValueChange, handleDelete}) {
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
                value={value}
                variant="outlined"
                onChange={e => {
                    handleValueChange(e.target.value)
                }}
            />
            <Button variant="outlined" color="error" onClick={handleDelete}>Delete</Button>
        </div>
    );
}

function SingleSort({param, direction, changeFunc, handleDelete}) {
    const id = `operator-label-${param}`

    return (
        <div className="singleFilter">
            <div>{param}</div>

            <FormControl fullWidth>
                <InputLabel id={id}>Operator</InputLabel>
                <Select
                    labelId={id}
                    id={id}
                    value={direction}
                    label={param}
                    size="small"
                    onChange={e => {
                        changeFunc(e.target.value);
                    }}
                >
                    <MenuItem value={'asc'}>Ascending</MenuItem>
                    <MenuItem value={'desc'}>Descending</MenuItem>
                </Select>
            </FormControl>
            <Button variant="outlined" color="error" onClick={handleDelete}>Delete</Button>

        </div>
    )

}

