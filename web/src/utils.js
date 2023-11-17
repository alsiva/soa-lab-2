import {Box, CardContent, CardHeader, Typography} from "@mui/material";
import Card from "@mui/material/Card";

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
    return (
        <div>
            <Box sx={{
                display: 'inline-flex',
                flexDirection: 'column',
            }}>
                {product != null &&
                    <Card key={product.id} sx={{margin: 1, border: 5}}>
                        <CardHeader title={product.name}/>
                        <CardContent>
                            <Typography component={'span'} variant={'body2'} align='left'>
                                ID -- {product.id} <br/>
                                Coordinates -- ({product.coordinates.x}, {product.coordinates.y}) <br/>
                                CreationDate -- {product.creationDate} <br/>
                                Price -- {product.price} <br/>
                                ManufactureCost -- {product.manufactureCost} <br/>
                                UnitOfMeasure -- {product.unitOfMeasure} <br/>
                                <Typography component={'span'} variant={'body2'} align="center">
                                    Organization
                                </Typography >
                                ORG_ID -- {product.organization.orgId} <br/>
                                Name -- {product.organization.name} <br/>
                                AnnualTurnover -- {product.organization.annualTurnover} <br/>
                                OrgType -- {product.organization.orgType} <br/>
                                PostalAddress -- {product.organization.postalAddress.zipcode} <br/>
                            </Typography>
                        </CardContent>
                    </Card>
                }
            </Box>
        </div>
    )
}