export function parseProduct(product) {
    const organization = product.querySelector("organization")

    return {
        id: parseInt(product.querySelector("id").textContent),
        name: product.querySelector("name").textContent,
        coordinates: parseCoordinates(product.querySelector("coordinates")),
        creationDate: product.querySelector("creationDate").textContent,
        price: parseFloat(product.querySelector("price").textContent),
        unitOfMeasure: product.querySelector("unitOfMeasure").textContent,
        manufactureCost: parseInt(product.querySelector("manufactureCost").textContent),
        organization: parseOrganisation(organization),
    }
}

function parseCoordinates(coordinates) {
    return {
        x: parseFloat(coordinates.querySelector("x").textContent),
        y: parseInt(coordinates.querySelector("y").textContent)
    }
}

function parseOrganisation(organization) {
    return {
        orgId: parseInt(organization.querySelector("orgId").textContent),
        name: organization.querySelector("name").textContent,
        fullName: organization.querySelector("fullName").textContent,
        annualTurnover: parseFloat(organization.querySelector("annualTurnover").textContent),
        orgType: organization.querySelector("orgType").textContent,
        postalAddress: {
            zipcode: parseInt(organization.querySelector("postalAddress > zipcode").textContent)
        }
    }
}
