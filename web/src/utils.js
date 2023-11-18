import {
    Box, Button,
    CardContent,
    CardHeader,
    Table, TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow, TextField,
    Typography
} from "@mui/material";
import Card from "@mui/material/Card";
import {Input} from '@mui/base/Input';
import React, {useState} from "react";
import dayjs from "dayjs";


export function parseProduct(product) {
    return {
        id: parseInt(product.querySelector("id").textContent),
        name: product.querySelector("name").textContent,
        coordinates: {
            x: parseFloat(product.querySelector("coordinates > x").textContent),
            y: parseInt(product.querySelector("coordinates > y").textContent)
        },
        creationDate: product.querySelector("creationDate").textContent,
        price: parseFloat(product.querySelector("price").textContent),
        unitOfMeasure: product.querySelector("unitOfMeasure").textContent,
        manufactureCost: parseInt(product.querySelector("manufactureCost").textContent),
        organization: {
            orgId: parseInt(product.querySelector("organization > orgId").textContent),
            name: product.querySelector("organization > name").textContent,
            fullName: product.querySelector("organization > fullName").textContent,
            annualTurnover: parseFloat(product.querySelector("organization > annualTurnover").textContent),
            orgType: product.querySelector("organization > orgType").textContent,
            postalAddress: {
                zipcode: parseInt(product.querySelector("organization > postalAddress > zipcode").textContent)
            }
        }
    }
}

export function CardView({product}) {

    const unitOfMeasureEnum = {
        METERS: "METERS",
        SQUARE_METERS: "SQUARE_METERS",
        PCS: "PCS",
        MILLILITERS: "MILLILITERS",
        MILLIGRAMS: "MILLIGRAMS"
    }

    const orgType = {
        COMMERCIAL: "COMMERCIAL",
        GOVERNMENT: "GOVERNMENT",
        TRUST: "TRUST",
        PRIVATE_LIMITED_COMPANY: "PRIVATE_LIMITED_COMPANY",
        OPEN_JOINT_STOCK_COMPANY: "OPEN_JOINT_STOCK_COMPANY"
    }

    async function updateProduct(nextProduct) {
        const xmlBody = `
            <Product>
                <name>
                    ${nextProduct.name}
                </name>
                <coordinates>
                    <x>${nextProduct.coordinates.x}</x>
                    <y>${nextProduct.coordinates.y}</y>
                </coordinates>
                <price>${nextProduct.price}</price>
                <manufactureCost>${nextProduct.manufactureCost}</manufactureCost>
                <unitOfMeasure>${nextProduct.unitOfMeasure}</unitOfMeasure>
                <organization>
                    <orgId>${nextProduct.organization.orgId}</orgId>
                    <name>${nextProduct.organization.name}</name>
                    <annualTurnover>${nextProduct.organization.annualTurnover}</annualTurnover>
                    <orgType>${nextProduct.organization.orgType}</orgType>
                    <postalAddress>
                        <zipcode>${nextProduct.organization.postalAddress.zipcode}</zipcode>
                    </postalAddress>
                </organization>
            </Product>
        `

        const response = await fetch(`/products/${nextProduct.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/xml'
            },
            body: xmlBody
        })

        console.log(xmlBody)
        console.log(response)
    }



    const [productState, setProductState] = useState(product)

    return (
        <div>
            <Box sx={{
                display: 'inline-flex',
                flexDirection: 'column',
            }}>
                {<Card key={product.id} sx={{margin: 1, border: 5}}>
                    <CardHeader title={product.name}/>
                    <CardContent>
                        <Typography component={'span'} variant={'body2'} align='left'>
                            ID -- {product.id} <br/>
                            Coordinates -- (<input className="coordinate" value={productState.coordinates.x}
                                                   onChange={field => setProductState(prev => ({
                                                       ...prev,
                                                       coordinates: {
                                                           x: field.target.value,
                                                           y: prev.coordinates.y
                                                       }
                                                   }))}/>, <input className="coordinate"
                                                                  value={productState.coordinates.y}
                                                                  onChange={field => setProductState(prev => ({
                                                                      ...prev,
                                                                      coordinates: {
                                                                          x: prev.coordinates.x,
                                                                          y: field.target.value
                                                                      }
                                                                  }))}/>) <br/>
                            CreationDate -- {dayjs(product.creationDate).format("DD.MM.YYYY HH.mm")} <br/>
                            Price -- <input value={productState.price} onChange={field => setProductState(prev => ({
                            ...productState,
                            price: field.target.value
                        }))}/> <br/>
                            ManufactureCost -- <input value={productState.manufactureCost}
                                                      onChange={field => setProductState(prev => ({
                                                          ...productState,
                                                          manufactureCost: field.target.value
                                                      }))}/> <br/>
                            UnitOfMeasure -- <select value={productState.unitOfMeasure}
                                                     onChange={field => setProductState(prev => ({
                                                         ...productState,
                                                         unitOfMeasure: field.target.value
                                                     }))}>
                            <option value={unitOfMeasureEnum.METERS}>METERS</option>
                            <option value={unitOfMeasureEnum.PCS}>PCS</option>
                            <option value={unitOfMeasureEnum.MILLIGRAMS}>MILLIGRAMS</option>
                            <option value={unitOfMeasureEnum.MILLILITERS}>MILLILITERS</option>
                            <option value={unitOfMeasureEnum.SQUARE_METERS}>SQUARE_METERS</option>
                        </select> <br/>
                            <Typography component={'span'} variant={'h5'} align="center">
                                Organization <br/>
                            </Typography>
                            ORG_ID -- {product.organization.orgId} <br/>
                            Name -- <input value={productState.organization.name}
                                           onChange={field => setProductState(prev => ({
                                               ...prev, organization: {
                                                   ...prev.organization,
                                                   name: field.target.value
                                               }
                                           }))}/> <br/>
                            AnnualTurnover -- <input value={productState.organization.annualTurnover}
                                                     onChange={field => setProductState(prev => ({
                                                         ...prev, organization: {
                                                             ...prev.organization,
                                                             annualTurnover: field.target.value
                                                         }
                                                     }))}/> <br/>
                            OrgType -- <input value={productState.organization.orgType}
                                              onChange={field => setProductState(prev => ({
                                                  ...prev, organization: {
                                                      ...prev.organization,
                                                      orgType: field.target.value
                                                  }
                                              }))}/> <br/>
                            PostalAddress -- <input value={productState.organization.postalAddress.zipcode}
                                                    onChange={field => setProductState(prev => ({
                                                        ...prev, organization: {
                                                            ...prev.organization,
                                                            postalAddress: {
                                                                zipcode: field.target.value
                                                            }
                                                        }
                                                    }))}/> <br/>
                        </Typography>
                    </CardContent>
                    <Button onClick={() => updateProduct(productState)}>Update product</Button>
                </Card>
                }

            </Box>
        </div>
    )
}

export function TableView({products}) {
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
                        <TableRow key={product.id} sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                            <TableCell>{product.id}</TableCell>
                            <TableCell>{product.name}</TableCell>
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

export function UpdateForm() {
    return (
        <div>
            <Box sx={{
                display: 'inline-flex',
                flexDirection: 'column',
            }}>
                <Card sx={{margin: 1, border: 5}}>
                    <CardHeader title="Ввод полей"/>
                    <CardContent>
                        <Typography component={'span'} variant={'body2'} align='left'>
                            <Input/>
                        </Typography>
                    </CardContent>
                </Card>
            </Box>
        </div>
    )
}