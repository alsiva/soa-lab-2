import {useLoaderData} from "react-router-dom";
import {parseProduct} from "./parsing";
import {Box, Button, CardContent, CardHeader, Typography} from "@mui/material";
import Card from "@mui/material/Card";
import {useState} from "react";
import dayjs from "dayjs";
import {serializeProduct} from "./serializing";

export async function productLoader({ params }) {
    const { productId } = params;
    if (productId === null || isNaN(productId)) {
        return { isSuccess: false, errorMsg: `invalid product id: ${productId}` }
    }

    const response = await fetch(`/products/${productId}`)

    if (response.status === 404) {
        return { isSuccess: false, errorMsg: `No product with id ${productId}` }
    }

    const parser = new DOMParser()
    const doc = parser.parseFromString(await response.text(), "application/xml")
    const product = doc.querySelector("Product")

    return { isSuccess: true, product: parseProduct(product) }
}

export function SingleProduct({productId}) {
    const { isSuccess, errorMsg, product } = useLoaderData()

    if (!isSuccess) {
        return (
            <div>{errorMsg}</div>
        )
    }

    return (
        <ProductCardView product={product}/>
    )
}

function ProductCardView({product}) {
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
        const serializedProduct = serializeProduct(nextProduct)

        const response = await fetch(`/products/${nextProduct.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/xml'
            },
            body: serializedProduct
        })

        console.log(serializedProduct)
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
                    <CardHeader title={productState.name}/>
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