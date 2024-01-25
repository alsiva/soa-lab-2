import './App.css';
import {Box, LinearProgress, Stack} from "@mui/material";
import {NavLink, Outlet, useNavigation} from "react-router-dom";
import {useState} from "react";
import {unitOfMeasureEnum} from "./SingleEditableProduct";


export function App() {
    const navigation = useNavigation();

    return (
        <div>
            <Box sx={{width: '100%', margin: '0 16px'}}>
                <LinearProgress sx={{
                    opacity: (navigation.state === 'loading') ? 1 : 0,
                    transition: 'opacity 100ms',
                }}/>
                <Stack direction="row" spacing={2} sx={{margin: '16px 0'}}>
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
    const navigation = useNavigation();
    const [maxAnnualTurnover, setMaxAnnualTurnover] = useState(0)

    return (
        <div>
            <Box sx={{width: '100%', margin: '0 16px'}}>
                <LinearProgress sx={{
                    opacity: (navigation.state === 'loading') ? 1 : 0,
                    transition: 'opacity 100ms',
                }}/>

                <Stack direction="row" spacing={2} sx={{margin: '16px 0'}}>
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
    const navigation = useNavigation();
    const [unitOfMeasure, setUnitOfMeasure] = useState(unitOfMeasureEnum.METERS)
    const [range, setRange] = useState({
        min: 0,
        max: 0
    })

    function updateUnitOfMeasure(value) {
        setUnitOfMeasure(value)
    }


    function updateRangeMin(value) {
        setRange(prevState => ({
            min: value,
            max: prevState.max
        }))
    }

    function updateRangeMax(value) {
        setRange(prevState => ({
            min: prevState.min,
            max: value
        }))
    }


    return (
        <div>
            <Box sx={{width: '100%', margin: '0 16px'}}>
                <LinearProgress sx={{
                    opacity: (navigation.state === 'loading') ? 1 : 0,
                    transition: 'opacity 100ms',
                }}/>
                <Stack direction="row" spacing={2} sx={{margin: '16px 0'}}>
                    <div>
                        <NavLink
                            to={`filter/unit-of-measure/${unitOfMeasure}`}
                            className={({isActive, isPending}) =>
                                isPending ? "pending" : isActive ? "active" : ""
                            }
                        >
                            Product with unit of measure
                        </NavLink>
                        UnitOfMeasure -- <select value={unitOfMeasure}
                                                 onChange={field => setUnitOfMeasure(field.target.value)}>
                        <option value={unitOfMeasureEnum.METERS}>METERS</option>
                        <option value={unitOfMeasureEnum.PCS}>PCS</option>
                        <option value={unitOfMeasureEnum.MILLIGRAMS}>MILLIGRAMS</option>
                        <option value={unitOfMeasureEnum.MILLILITERS}>MILLILITERS</option>
                        <option value={unitOfMeasureEnum.SQUARE_METERS}>SQUARE_METERS</option>
                    </select>
                    </div>
                    <div>
                        <NavLink
                            to={`filter/price/${range.min}/${range.max}`}
                            className={({isActive, isPending}) =>
                                isPending ? "pending" : isActive ? "active" : ""
                            }
                        >
                            <div>Products with range</div>
                        </NavLink>
                        <div>MinRange</div>
                        <input value={range.min} type="number" onChange={event => updateRangeMin(event.target.value)}/>
                        <div>MaxRange</div>
                        <input value={range.max} type="number" onChange={event => updateRangeMax(event.target.value)}/>
                    </div>

                </Stack>
            </Box>
            <Box sx={{width: '100%'}}>
                <Outlet/>
            </Box>
        </div>
    )
}



