import React, { useState } from "react";
import Card from "../Card/Card";
import "./Products.css";

import { Product } from "../../types/systemTypes/Product";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import { Box, Chip, Divider, Grid, Typography } from "@mui/material";
import { LoadingButton } from "@mui/lab";
import AlertDialog from "../Dialog/AlertDialog";
interface ProductsDisplayProps {
    type: string;
    products: Product[];
    canMakeChanges: boolean;
}
const ProductsDisplay: React.FC<ProductsDisplayProps> = ({ type, products, canMakeChanges }) => {
    const error = false;//useAppSelector((state) => state.products.error);
    const loading = false;//useAppSelector((state) => state.products.loading);
    const [quantityError, setQuantiryError] = useState(false);
    const [quantity, setQuantity] = useState(0);
    const userId = -1;//useAppSelector((state) => state.auth.userId);
    const dispatch = useAppDispatch();
    const set = (newVal: number) => {
        if (newVal >= 0) {
            setQuantity(newVal);
        }
        else {
            setQuantiryError(!quantityError);
        }
    }
    const selectedProduct: Product = {
        id: 0,
        storeId: 0,
        category: [],
        name: "show",
        description: "great shoes",
        price: 110,
        quantity: 0,
        img: "https://images.pexels.com/photos/1598505/pexels-photo-1598505.jpeg",
        rating: 2,
        reviewNumber: 10,
    };//useAppSelector((dtate) => state.products.selectedProduct); 
    const handleOnAddToBasket = () => {
        dispatch(addProductToCart({ userId: userId, StoreId: selectedProduct.storeId, productId: selectedProduct.id, amount: quantity }));
    }

    return (
        <>
            <Box>
                <img
                    src={selectedProduct.img}
                    alt=""
                />
            </Box>
            <Box>
                <Typography variant="h5" component="h5" className="name" fontFamily={"Gill Sans"} color={"black"}>
                    {selectedProduct.name}
                </Typography>
                <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"}>
                    {selectedProduct.description}
                </Typography>
                {selectedProduct.category.map((category: string) => {
                    return (
                        <Chip label={category} />
                    )
                })}
                <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"}>
                    {selectedProduct.reviewNumber} reviews
                </Typography>
                <Divider />
                <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"}>
                    {quantity * selectedProduct.price} ₪
                </Typography>
                <Divider />
                <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"}>
                    Quantity:
                </Typography>
                <RemoveIcon onClick={() => { set(quantity - 1) }} />
                <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"}>
                    {quantity}
                </Typography>
                <AddIcon onClick={() => { set(quantity + 1) }} />
                <Grid item xs={12} key={1}>
                    <LoadingButton
                        type="submit"
                        fullWidth
                        variant="contained"
                        onClick={handleOnAddToBasket}
                        sx={{ color: 'white', backgroundColor: 'blue', '&:hover': { backgroundColor: 'green' }, width: '50%', left: 205, bottom: 53 }}
                    >
                        {'add to basket'}
                    </LoadingButton >
                </Grid>
            </Box>
            <AlertDialog sevirity={"error"} text={"quantity cannot be negative"} open={quantityError} onClose={() => {
                setQuantity(0);
                setQuantiryError(false)
            }} />
        </>
    );
};

export default ProductsDisplay;