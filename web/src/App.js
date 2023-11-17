import './App.css';
import {Box, Button, TextField} from "@mui/material";
import {useState} from "react";
import {ProductList} from "./ProductList";
import {GetTrip} from "./GetTrip";

function App() {

    const [content, setContent] = useState("")
    const [productID, setProductID] = useState(null)

    return (
        <div>
            <Box sx={{
                minHeight: 100,
                border: 1,
                paddingTop: 2
            }}>
                <Button variant="outlined" onClick={() => setContent("getTrips")}>Get products</Button>
                <Button variant="outlined" onClick={() => setContent("getTrip")}>GetTrip</Button>
                <TextField id="outlined-basic" label="tripId" variant="outlined" onChange={(field) => setProductID(field.target.value)}/>
                <Button variant="outlined" onClick={() => setContent("")}>Erase</Button>
            </Box>
            <Box>
                {content === "getTrips" &&
                    <ProductList/>
                }
                {content === "getTrip" &&
                    <GetTrip productId={productID}/>
                }
                {content === "" &&
                    <h1>You didn't get trips bro</h1>
                }
            </Box>
        </div>
    );
}

export default App;
