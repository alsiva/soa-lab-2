import {serializeProduct} from "./serializing";
import {getEmptyProduct, sleep} from "./utils";
import {useState} from "react";
import {Alert} from "@mui/material";
import {SingleEditableProduct} from "./SingleEditableProduct";
import {SERVICE_PREFIX} from "./index";


export function CreateProduct() {

    const [status, setStatus] = useState(-1)
    const [isLoading, setIsLoading] = useState(false)

    async function addProduct(nextProduct) {
        const serializedProduct = serializeProduct(nextProduct)

        console.log("SERIALIZED PRODUCT")
        console.log(serializedProduct)


        setIsLoading(true)
        await sleep(2000)
        const response = await fetch(`${SERVICE_PREFIX}/api/products`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/xml'
            },
            body: serializedProduct
        })


        setIsLoading(false)
        setStatus(response.status)
    }

    return (
        <>
            <div>
                {(() => {
                    if (status === -1) {

                    } else if (status === 200) {
                        return (
                            <Alert severity="success">Продукт был обновлён успешно</Alert>
                        );
                    } else if (status === 404) {
                        return (
                            <Alert severity="warning">Не получилось обновить продукт</Alert>
                        );
                    } else if (status === 405) {
                        return (
                            <div>
                                <Alert severity="error">Егор Дмитриевич натыкал, ввёл продукт с неверными данными</Alert>
                                <Alert severity="warning">А теперь стишок</Alert>
                                <Alert severity="success">
                                    <div>Маленький ёжик бежит и хохочет</div>
                                    <div>Ёжику травка пипиську щекочет</div>
                                    <div>Травка закончилась гравий пошёл</div>
                                    <div>Ёжик домой без пиписьки пришёл</div>
                                </Alert>
                            </div>

                        );
                    } else {
                        return (
                            <Alert severity="error">Следующий статус {status}</Alert>
                        );
                    }
                })()}
            </div>
            <SingleEditableProduct buttonText={"Создать продукт"} product={getEmptyProduct} callback={addProduct}
                                   isLoading={isLoading}></SingleEditableProduct>
        </>
    )
}