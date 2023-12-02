export function serializeProduct(product) {
    let xmlDoc = document.implementation.createDocument(null,"Document")
    let xmlProduct = xmlDoc.createElement("Product")

    let name = xmlDoc.createElement("name")
    name.textContent = product.name

    let xmlCoordinates = xmlDoc.createElement("coordinates")
    let xmlCoordinateX = xmlDoc.createElement("x")
    let xmlCoordinateY = xmlDoc.createElement("y")
    xmlCoordinateX.textContent = product.coordinates.x
    xmlCoordinateY.textContent = product.coordinates.y
    xmlCoordinates.appendChild(xmlCoordinateX)
    xmlCoordinates.appendChild(xmlCoordinateY)


    let price = xmlDoc.createElement("price")
    price.textContent = product.price

    let manufactureCost = xmlDoc.createElement("manufactureCost")
    manufactureCost.textContent = product.manufactureCost

    let unitOfMeasure = xmlDoc.createElement("unitOfMeasure")
    unitOfMeasure.textContent = product.unitOfMeasure


    let organization = serializeOrganization(xmlProduct, xmlDoc, product.organization)



    xmlProduct.appendChild(name)
    xmlProduct.appendChild(xmlCoordinates)
    xmlProduct.appendChild(price)
    xmlProduct.appendChild(manufactureCost)
    xmlProduct.appendChild(unitOfMeasure)
    xmlProduct.appendChild(organization)


    console.log(xmlProduct)

    return new XMLSerializer().serializeToString(xmlProduct)
}

function serializeOrganization(xmlProduct, xmlDoc, organization) {
    let xmlOrganization = xmlDoc.createElement("organization")
    let orgId = xmlDoc.createElement("orgId")
    let orgName = xmlDoc.createElement("name")
    let fullName = xmlDoc.createElement("fullName")
    let annualTurnover = xmlDoc.createElement("annualTurnover")
    let orgType = xmlDoc.createElement("orgType")

    orgId.textContent = organization.orgId
    orgName.textContent = organization.name
    fullName.textContent = organization.fullName
    annualTurnover.textContent = organization.annualTurnover
    orgType.textContent = organization.orgType

    let postalAddress = xmlDoc.createElement("postalAddress")
    let zipCode = xmlDoc.createElement("zipcode")
    zipCode.textContent = organization.postalAddress.zipcode
    postalAddress.appendChild(zipCode)

    xmlProduct.appendChild(xmlOrganization)
    xmlOrganization.appendChild(orgId)
    xmlOrganization.appendChild(orgName)
    xmlOrganization.appendChild(fullName)
    xmlOrganization.appendChild(annualTurnover)
    xmlOrganization.appendChild(orgType)
    xmlOrganization.appendChild(postalAddress)

    return xmlOrganization
}