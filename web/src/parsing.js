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
