import React from "react";
import { useNavigate } from "react-router-dom";
import { Product } from "../../types/systemTypes/Product";
import { Card, CardMedia, CardContent, Typography, Box, CardActions } from "@mui/material";
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import RemoveIcon from '@mui/icons-material/Remove';
import { useAppDispatch, useAppSelector } from "../../redux/store";
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import { addToCart, getCart, removeFromCart } from "../../reducers/cartSlice";
import { useCallback, useEffect, useState } from "react";
interface CardProps {
    item: Product;
}
const ProductInCart: React.FC<CardProps> = ({ item }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const [isFav, setFav] = React.useState(false);
    const [quantity, setQuantity] = React.useState(0);



    const userId = useAppSelector((state) => state.auth.userId);

    const discountPercentage = 0.2;
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);

    const handleAddToCart = () => {
        dispatch(addToCart({ userId: userId, storeId: item.storeId, productId: item.productId, quantity: quantity }));
        dispatch(getCart({ userId: userId }));
        setQuantity(0);
    }
    const addProdut = () => {
        setQuantity(quantity + 1);
    }

    const handleRemoveFromCart = () => {
        //dispatch(removeFromCart(item.id))
        setQuantity(quantity - 1);
        dispatch(removeFromCart({ userId: userId, storeId: item.storeId, productId: item.productId, quantity: 1 }));
        dispatch(getCart({ userId: userId }));
    }
    const handleDelete = () => {
        //dispatch(removeFromCart(item.id))
        dispatch(removeFromCart({ userId: userId, storeId: item.storeId, productId: item.productId, quantity: quantity }));
        setQuantity(0);
        dispatch(getCart({ userId: userId }));
    }
    useEffect(() => {
        setQuantity(item.quantity);
    }, [item.quantity])



    return (
        <Card className="h-100" sx={{ width: '50%', marginLeft: 'auto', mr: 10, mt: 2, flexBasis: '30%' }} >
            <CardMedia
                component="img"
                src={item.img}
                height="200px"
                style={{ objectFit: 'cover' }}
            />
            <CardContent className="d-flex flex-column">
                <Box width={'100%'} height={40}>
                    <Typography gutterBottom variant="h5" component="div">
                        {item.name}
                    </Typography>
                </Box>
            </CardContent>
            <CardActions >
                <Box>
                    <IconButton onClick={addProdut}>
                        <AddIcon />
                    </IconButton>
                    <IconButton onClick={handleRemoveFromCart}>
                        <RemoveIcon />
                    </IconButton>

                    <IconButton onClick={handleDelete}>
                        <DeleteIcon />
                    </IconButton>
                </Box>
                <Typography gutterBottom variant="h6" component="div" sx={{ ml: 3, mt: 1 }}>
                    {item.quantity}
                </Typography>
                <Box>

                    <Typography gutterBottom variant="h5" component="div" sx={{ marginLeft: 10 }}>
                        {(item.price * (quantity + 1)).toFixed(2)}$
                    </Typography>
                </Box>
            </CardActions>
            <CardContent sx={{ height: 55 }} >
                <CardActions>
                    <IconButton onClick={handleAddToCart} sx={{ ml: 10 }}>
                        <AddShoppingCartIcon />
                        <Typography gutterBottom variant="h5" component="div" sx={{ ml: 2, mt: 1 }}>
                            add to cart
                        </Typography>
                    </IconButton>
                </CardActions>
            </CardContent>
        </Card>

    );
};

export default ProductInCart;