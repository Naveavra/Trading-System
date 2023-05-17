import React from "react";
import "./Card.css";
import { Link, useNavigate } from "react-router-dom";
import { Product } from "../../types/systemTypes/Product";
import { Card, Button, CardMedia, CardContent, Typography, Box, CardActions, Grid } from "@mui/material";
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import RemoveIcon from '@mui/icons-material/Remove';
import { useAppSelector } from "../../redux/store";
import DeleteIcon from '@mui/icons-material/Delete';
import { Action } from "../../types/systemTypes/Action";
import { LoadingButton } from "@mui/lab";
import AddIcon from '@mui/icons-material/Add';
interface CardProps {
    item: Product;
    canEdit: boolean;
    canDelete: boolean;
}
const ProductCard: React.FC<CardProps> = ({ item, canDelete, canEdit }) => {
    const navigate = useNavigate();
    const [isFav, setFav] = React.useState(false);
    const [quantity, setQuantity] = React.useState(0);
    const discountPercentage = 0.2;
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.id);

    const handleAddToCart = () => {
        //dispatch(addProductToCart({ userId: userId, StoreId: selectedProduct.storeId, productId: selectedProduct.id, amount: quantity }));
        setQuantity(quantity + 1);
    }
    const handleClickProduct = () => {
    }
    const handleRemoveFromCart = () => {
        //dispatch(removeFromCart(item.id))
        setQuantity(quantity - 1);
    }
    return (
        <Card className="h-100" sx={{ width: '50%', marginLeft: 'auto', mr: 10, mt: 2, flexBasis: '30%' }} onClick={handleClickProduct}>
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
                    {quantity === 0 ? (
                        <IconButton onClick={handleAddToCart}>
                            <AddIcon />
                        </IconButton>
                    ) : (
                        <>
                            <IconButton onClick={handleAddToCart}>
                                <AddIcon />
                            </IconButton>
                            <IconButton onClick={handleRemoveFromCart}>
                                <RemoveIcon />
                            </IconButton>
                        </>
                    )}
                    {canEdit &&
                        <IconButton onClick={() => navigate("editProduct")} >
                            <EditIcon />
                        </IconButton>
                    }
                    {canDelete &&
                        <IconButton >
                            <DeleteIcon />
                        </IconButton>
                    }
                </Box>
                <Typography gutterBottom variant="h6" component="div" sx={{ ml: 3, mt: 1 }}>
                    {quantity}
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

export default ProductCard;