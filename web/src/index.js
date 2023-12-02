import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs'
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import {SingleProduct, productLoader} from "./SingleProduct";
import {ProductList, productListLoader} from "./ProductList";
import {CreateProduct} from "./CreateProduct";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App/>,
        children: [
            {
                path: "products",
                loader: productListLoader,
                element: <ProductList/>
            },
            {
                path: "/products/new",
                element: <CreateProduct />
            },
            {
                path: "/products/:productId",
                loader: productLoader,
                element: <SingleProduct />
            }
        ]
    },
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
