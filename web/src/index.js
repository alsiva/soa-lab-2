import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {App, Service, Ebay} from './App';
import reportWebVitals from './reportWebVitals';
import {LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs'
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import {UpdateProduct, productLoader} from "./UpdateProduct";
import {
    ProductList,
    productListLoader,
    productListWithPriceRangeLoader,
    productListWithUnitOfMeasureLoader
} from "./ProductList";
import {CreateProduct} from "./CreateProduct";
import {ProductWithMaxUnitOfMeasure, productWithMaxUnitOfMeasureLoader} from "./ProductWithMaxUnitOfMeasure";
import {ProductListWithLessAnnualTurnover, productListWithLessAnnualTurnoverLoader} from "./ProductListWithLessAnnualTurnover";

export const SERVICE_PREFIX = `/service`
export const EBAY_PREFIX = `/ebay`

export const FRONTEND_PREFIX = `/web`

const router = createBrowserRouter([
    {
        path: FRONTEND_PREFIX,
        element: <App/>,
        children: [
            {
                path: "service",
                element: <Service/>,
                children: [
                    {
                        path: "products",
                        loader: productListLoader,
                        element: <ProductList/>
                    },
                    {
                        path: "products/new",
                        element: <CreateProduct />
                    },
                    {
                        path: "products/:productId",
                        loader: productLoader,
                        element: <UpdateProduct />
                    },
                    {
                        path: "products/max-measure",
                        loader: productWithMaxUnitOfMeasureLoader,
                        element: <ProductWithMaxUnitOfMeasure/>
                    },
                    {
                        path: "products/max-annual-turnover/:maxAnnualTurnover",
                        loader: productListWithLessAnnualTurnoverLoader,
                        element: <ProductListWithLessAnnualTurnover />
                    }
                ]
            },
            {
                path: "ebay",
                element: <Ebay/>,
                children: [
                    {
                        path: "filter/price/:minPrice/:maxPrice",
                        loader: productListWithPriceRangeLoader,
                        element: <ProductList/>
                    },
                    {
                        path: "filter/unit-of-measure/:unitOfMeasure",
                        loader: productListWithUnitOfMeasureLoader,
                        element: <ProductList />
                    }
                ]
            },


        ]
    }
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <RouterProvider router={router}/>
        </LocalizationProvider>
    </React.StrictMode>
)
;

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
