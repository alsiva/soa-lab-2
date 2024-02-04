import './App.css';
import {Box, LinearProgress, Stack} from "@mui/material";
import {NavLink, Outlet, useNavigation} from "react-router-dom";
import {useState} from "react";


export function App() {
    const navigation = useNavigation();

    return (
        <div>
            <Box sx={{width: '100%'}}>
                <LinearProgress sx={{
                    opacity: (navigation.state === 'loading') ? 1 : 0,
                    transition: 'opacity 100ms',
                }}/>
                <Stack direction="row" spacing={2} sx={{margin: '16px 16px'}}>
                    <NavLink
                        to="service"
                        className={({isActive, isPending}) =>
                            isPending ? "pending" : isActive ? "active" : ""
                        }
                    >
                        Service
                    </NavLink>
                    <NavLink
                        to="ebay"
                        className={({isActive, isPending}) =>
                            isPending ? "pending" : isActive ? "active" : ""
                        }
                    >
                        Ebay
                    </NavLink>
                </Stack>
            </Box>
            <Box sx={{width: '100%'}}>
                <Outlet/>
            </Box>
        </div>
    )

}

export function Service() {
    const [maxAnnualTurnover, setMaxAnnualTurnover] = useState(0)

    return (
        <div>
            <Box sx={{width: '100%'}}>
                <Stack direction="row" spacing={2} sx={{margin: '16px 16px'}}>
                    <Stack direction="column">
                        <NavLink
                            to="products"
                            className={({isActive, isPending}) =>
                                isPending ? "pending" : isActive ? "active" : ""
                            }
                        >
                            Products
                        </NavLink>
                    </Stack>

                    <NavLink
                        to="products/new"
                        className={({isActive, isPending}) =>
                            isPending ? "pending" : isActive ? "active" : ""
                        }
                    >
                        New product
                    </NavLink>
                    <NavLink
                        to="products/max-measure"
                        className={({isActive, isPending}) =>
                            isPending ? "pending" : isActive ? "active" : ""
                        }
                    >
                        Product with max unitOfMeasure
                    </NavLink>
                    <div>
                        <NavLink
                            to={`products/max-annual-turnover/${maxAnnualTurnover}`}
                            className={({isActive, isPending}) =>
                                isPending ? "pending" : isActive ? "active" : ""
                            }
                        >
                            Product with maxAnnualTurnover
                        </NavLink>
                        <input type="number" onChange={field => setMaxAnnualTurnover(+field.target.value)}></input>
                    </div>
                    <NavLink
                        to="products/max-measure"
                        className={({isActive, isPending}) =>
                            isPending ? "pending" : isActive ? "active" : ""
                        }
                    >
                        Product with max unitOfMeasure
                    </NavLink>
                </Stack>
            </Box>
            <Box sx={{width: '100%'}}>
                <Outlet/>
            </Box>
        </div>
    );
}

export function Ebay() {
    return (
        <div>
            <Box sx={{width: '100%', margin: '0 16px'}}>
                <Stack direction="row" spacing={2} sx={{margin: '16px 0'}}>
                    <div>
                        <NavLink
                            to={`unit-of-measure`}
                            className={({isActive, isPending}) =>
                                isPending ? "pending" : isActive ? "active" : ""
                            }
                        >
                            Product with unit of measure
                        </NavLink>
                    </div>
                    <div>
                        <NavLink
                            to={`price`}
                            className={({isActive, isPending}) =>
                                isPending ? "pending" : isActive ? "active" : ""
                            }
                        >
                            <div>Products with range</div>
                        </NavLink>
                    </div>

                </Stack>
            </Box>
            <Box sx={{width: '100%'}}>
                <Outlet/>
            </Box>
        </div>
    )
}



