import './App.css';
import {Box, Button, Table, TableCell, TableContainer, TableHead, TableRow, TextField} from "@mui/material";
import {useState} from "react";
import {ProductList} from "./ProductList";
import {GetTrip} from "./GetTrip";
import {UpdateForm} from "./utils";

function App() {

    const [content, setContent] = useState("")
    const [productID, setProductID] = useState(null)

    return (
        <div>
            <Box className="Header">
                <Box className="Header-item">
                    <Button variant="outlined" onClick={() => setContent("getTrips")}>Get products</Button>
                </Box>
                <Box className="Header-item">
                    <Box>
                        <Button variant="outlined" onClick={() => setContent("getTrip")}>
                            GetTrip
                        </Button>
                    </Box>
                    <Box>
                        <TextField id="outlined-basic" label="tripId" variant="outlined"
                                   onChange={(field) => setProductID(field.target.value)}
                        />
                    </Box>
                </Box>

                <Box className="Header-item" sx={{display: 'flex', direction: 'column'}} >
                    <Box sx={{height: 50, width: 50, border: 'solid', margin: 2}}>
                        First item
                    </Box>
                    <Box sx={{height: 50, width: 50, border: 'solid', margin: 2}}>
                        Second item
                    </Box>
                </Box>

                <Box className="Header-item">
                    <Button variant="outlined" onClick={() => setContent("updateTrip")}>Update the trip</Button>
                </Box>
                <Box className="Header-item">
                    <Button variant="outlined" onClick={() => setContent("")}>Erase</Button>
                </Box>

            </Box>

            <Box>
                {content === "getTrips" &&
                    <ProductList/>
                }
                {content === "getTrip" &&
                    <GetTrip productId={productID}/>
                }
                {content === "updateTrip" &&
                    <UpdateForm/>
                }
                {content === "" &&
                    <div>
                        <h1>Empty page</h1>
                    </div>
                }
            </Box>
        </div>
    );
}

export default App;
