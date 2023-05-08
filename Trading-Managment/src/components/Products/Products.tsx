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
import ReadOnlyRating from "../Ratings/readRating";

const ProductDisplay: React.FC = () => {
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
        category: ['nike', 'shoes', 'sport'],
        name: "Air force",
        description: "great shoes",
        price: 100,
        quantity: 0,
        img: "https://images.pexels.com/photos/12628400/pexels-photo-12628400.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
        rating: [{ value: 5, content: 'great product' }],
        reviewNumber: 10,
    };//useAppSelector((dtate) => state.products.selectedProduct); 
    const avrage = selectedProduct.rating.reduce((acc, curr) => acc + curr.value, 0) / selectedProduct.rating.length;
    const handleOnAddToBasket = () => {
        dispatch(addProductToCart({ userId: userId, StoreId: selectedProduct.storeId, productId: selectedProduct.id, amount: quantity }));
    }

    return (
        <>
            <Box sx={{ display: 'flex' }}>
                <Box sx={{ display: 'flex' }} >
                    <img
                        src={selectedProduct.img}
                        alt=""
                        style={{ marginTop: 50, height: '80%', width: '50%', marginLeft: 100, }}
                    />
                </Box>
                <Box sx={{ width: '40%' }}>
                    <Typography variant="h5" component="h5" className="name" fontFamily={"Gill Sans"} sx={{ marginTop: '10px' }} color={"black"} fontWeight={'bold'} textAlign={'center'} alignContent={'center'}>
                        {selectedProduct.name}
                    </Typography>
                    <Typography variant="h6" component="h6" className="description" sx={{ paddingTop: '10px' }} fontFamily={"Gill Sans"} color={"black"}>
                        {selectedProduct.description}
                    </Typography>
                    <Grid sx={{ paddingTop: '10px', display: 'flex' }}>
                        {selectedProduct.category.map((category: string) => {
                            return (
                                <Chip label={category} sx={{ marginRight: '2px', marginLeft: '5px' }} />
                            )
                        })}
                    </Grid>
                    <Grid sx={{ paddingTop: '10px', display: 'inline-block' }}>
                        <ReadOnlyRating rating={avrage} style={{ paddingTop: '10px', display: 'inline-block' }} />
                        <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"}>
                            {selectedProduct.reviewNumber} reviews
                        </Typography>
                    </Grid>
                    <Divider />
                    <Grid sx={{ paddingTop: '10px', marginButton: '10px' }}>
                        <Typography variant="h6" component="h5" className="description" sx={{ paddingTop: '10px', marginBottom: '10px' }} fontSize={30} fontFamily={"Gill Sans"} color={"black"}>
                            {quantity * selectedProduct.price} ₪
                        </Typography>
                    </Grid>
                    <Divider />
                    <Grid sx={{ paddingTop: '10px', marginButton: '10px' }}>
                        <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"}>
                            Quantity:
                        </Typography>
                    </Grid>
                    <Grid sx={{ paddingTop: '10px', marginButton: '10px', display: 'flex' }}>
                        <Box>
                            <RemoveIcon onClick={() => { set(quantity - 1) }} sx={{ paddingRight: '10px', paddingBottom: '3px', marginBottom: 5 }} />
                        </Box>
                        <Box>
                            <Typography variant="h6" component="h6" className="description" color={"black"} sx={{ marginBottom: 10, fontSize: 25 }}>
                                {quantity}
                            </Typography>
                        </Box>
                        <Box>
                            <AddIcon onClick={() => { set(quantity + 1) }} sx={{ paddingLeft: '10px' }} />
                        </Box>
                    </Grid>
                    <Grid item xs={12} key={1}>
                        <LoadingButton
                            type="submit"
                            fullWidth
                            variant="contained"
                            onClick={handleOnAddToBasket}
                            sx={{ color: 'white', backgroundColor: 'blue', '&:hover': { backgroundColor: 'green' }, width: '50%', left: 205, bottom: 53, borderRadius: 10 }}
                        >
                            {'add to basket'}
                        </LoadingButton >
                    </Grid>
                </Box>
            </Box>
            {/* customers review */}
            <Divider />
            <Box sx={{ display: 'flex' }}>
                <Box sx={{ width: '50%' }}>
                    <Typography variant="h5" component="h5" className="name" fontSize={50} fontFamily={"Gill Sans"} sx={{ marginTop: '10px', marginRight: 20 }} color={"black"} fontWeight={'bold'} textAlign={'center'} alignContent={'center'}>
                        {'Customers review'}
                    </Typography>
                    <Box sx={{ display: 'flex', marginLeft: 20, paddingTop: '10px', }} >
                        <ReadOnlyRating rating={avrage} style={{ marginLeft: 100, paddingTop: '10px', display: 'inline-block', size: 'large' }} />
                        <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"} sx={{ marginRight: '2px', marginLeft: '15px' }}>
                            {selectedProduct.reviewNumber} reviews
                        </Typography>
                    </Box>
                </Box>
                <Box sx={{ width: '50%' }}>
                    <Divider />
                    <Typography variant="h5" component="h5" className="name" fontSize={50} fontFamily={"Gill Sans"} sx={{ marginTop: '10px', marginRight: 20 }} color={"black"} fontWeight={'bold'} textAlign={'center'} alignContent={'center'}>
                        Top Customer Reviews
                    </Typography>
                    <Box>
                        {/* best*/}
                        <Grid sx={{ paddingTop: '10px', display: 'flex' }}>
                            <Box sx={{ width: '50%' }}>
                                <Typography variant="h6" component="h6" className="description" fontFamily={"Gill Sans"} color={"black"} sx={{ marginRight: '2px', marginLeft: '15px' }}>
                                    {selectedProduct.rating[0].content}
                                </Typography>
                            </Box>
                        </Grid>

                    </Box>
                </Box>

            </Box>


            <AlertDialog sevirity={"error"} text={"quantity cannot be negative"} open={quantityError} onClose={() => {
                setQuantity(0);
                setQuantiryError(false)
            }} />
        </>
    );
};

export default ProductDisplay;