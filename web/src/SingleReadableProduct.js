import {CardContent, CardHeader, Typography} from "@mui/material";
import Card from "@mui/material/Card";

export function SingleReadableProduct(readableProduct) {
    return (
        <Card key={readableProduct.id} sx={{margin: 1, border: 5}}>
            <CardHeader title={readableProduct.name}/>
            <CardContent>
                <Typography component={'span'} variant={'body2'} align='left'>
                    Coordinates -- ({readableProduct.coordinates.x} {readableProduct.coordinates.y})<br/>
                    Price -- {readableProduct.price} <br/>
                    ManufactureCost -- {readableProduct.manufactureCost}<br/>
                    UnitOfMeasure -- {readableProduct.unitOfMeasure}<br/>
                    <Typography component={'span'} variant={'h5'} align="center">
                        Organization <br/>
                    </Typography>
                    Name -- {readableProduct.organization.name}<br/>
                    AnnualTurnover -- {readableProduct.organization.annualTurnover}<br/>
                    OrgType -- {readableProduct.organization.orgType}<br/>
                    PostalAddress -- {readableProduct.organization.postalAddress.zipcode}<br/>
                </Typography>
            </CardContent>
        </Card>
    )
}