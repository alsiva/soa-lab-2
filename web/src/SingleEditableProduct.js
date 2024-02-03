import {useState} from "react";
import {Box, Button, CardContent, CardHeader, CircularProgress, TextField, Typography} from "@mui/material";
import Card from "@mui/material/Card";
import dayjs from "dayjs";


export const unitOfMeasureEnum = {
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

export function SingleEditableProduct({product, callback, isLoading, buttonText}) {


    const [productState, setProductState] = useState(product)

    return (

        <div>
            <Box sx={{
                display: 'inline-flex',
                flexDirection: 'column'
            }}>
                {isLoading
                    ? <CircularProgress/>
                    : <Card key={product.id} sx={{margin: 1, border: 5}}>
                        <CardHeader title={productState.name}/>
                        <TextField label="Product name" variant="outlined" value={productState.name}
                            onChange={field => setProductState(prev => ({
                                ...prev,
                                name: field.target.value
                            }))}
                        />
                        <CardContent>
                            <Typography component={'span'} variant={'body2'} align='left'>
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
                                org_id -- <input value={productState.organization.orgId}
                                               onChange={field => setProductState(prev => ({
                                                   ...prev, organization: {
                                                       ...prev.organization,
                                                       orgId: field.target.value
                                                   }
                                               }))}/> <br/>
                                Name -- <input value={productState.organization.name}
                                               onChange={field => setProductState(prev => ({
                                                   ...prev, organization: {
                                                       ...prev.organization,
                                                       name: field.target.value
                                                   }
                                               }))}/> <br/>
                                FullName -- <input value={productState.organization.fullName}
                                               onChange={field => setProductState(prev => ({
                                                   ...prev, organization: {
                                                       ...prev.organization,
                                                       fullName: field.target.value
                                                   }
                                               }))}/> <br/>
                                AnnualTurnover -- <input value={productState.organization.annualTurnover}
                                                         onChange={field => setProductState(prev => ({
                                                             ...prev, organization: {
                                                                 ...prev.organization,
                                                                 annualTurnover: field.target.value
                                                             }
                                                         }))}/> <br/>
                                OrgType -- <select value={productState.organization.orgType}
                                        onChange={field => setProductState(prev => ({
                                            ...prev, organization: {
                                                ...prev.organization,
                                                orgType: field.target.value
                                            }
                                        }))}>
                                    <option value={orgType.COMMERCIAL}>COMMERCIAL</option>
                                    <option value={orgType.TRUST}>TRUST</option>
                                    <option value={orgType.GOVERNMENT}>GOVERNMENT</option>
                                    <option value={orgType.OPEN_JOINT_STOCK_COMPANY}>OPEN_JOINT_STOCK_COMPANY</option>
                                    <option value={orgType.PRIVATE_LIMITED_COMPANY}>PRIVATE_LIMITED_COMPANY</option>
                                </select>
                                <br/>
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
                        <Button onClick={() => callback(productState)}>
                            {buttonText}
                        </Button>
                    </Card>
                }
            </Box>
        </div>
    )
}