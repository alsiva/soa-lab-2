export function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

export function getEmptyProduct() {
    return {
        name: undefined,
        coordinates: {
            x: undefined,
            y: undefined
        },
        price: undefined,
        unitOfMeasure: undefined,
        manufactureCost: undefined,
        organization: {
            orgId: undefined,
            name: undefined,
            fullName: undefined,
            annualTurnover: undefined,
            orgType: undefined,
            postalAddress: {
                zipcode: undefined
            }
        }
    }
}