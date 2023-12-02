import './App.css';
import {Box,  LinearProgress, Stack} from "@mui/material";
import {NavLink, Outlet, useNavigation} from "react-router-dom";


function App() {
    const navigation = useNavigation();


    return (
        <div>
            <Box sx={{ width: '100%', margin: '0 16px' }}>
                <LinearProgress sx={{
                    opacity: (navigation.state === 'loading') ? 1 : 0 ,
                    transition: 'opacity 100ms',
                }} />

                <Stack direction="row" spacing={2} sx={{ margin: '16px 0'}}>
                    <Stack direction="column">
                        <NavLink
                            to="products"
                            className={({ isActive, isPending }) =>
                                isPending ? "pending" : isActive ? "active" : ""
                            }
                        >
                            Products
                        </NavLink>
                    </Stack>

                    <NavLink
                        to="products/new"
                        className={({ isActive, isPending }) =>
                            isPending ? "pending" : isActive ? "active" : ""
                        }
                    >
                        New product
                    </NavLink>
                </Stack>

            </Box>
            <Box sx={{width: '100%'}}>
                <Outlet/>
            </Box>
        </div>
    );
}

export default App;
