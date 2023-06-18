import { Box } from "@mui/material";
import { useAppSelector } from "../../redux/store"
import { Product } from "../../types/systemTypes/Product";
import ProductCard from "../ProductInStore/Card";
import { useEffect } from "react";
import { emptyStore } from "../../types/systemTypes/Store";

interface props {
    text: string;
    price: number[];
    productRating: number[];
    storeRating: number[];
}
const Products: React.FC<props> = ({ text, price, productRating, storeRating }) => {
    const products = useAppSelector((state) => state.product.responseData) ?? [];
    const storesInfo = useAppSelector((state) => state.store.storeInfoResponseData) ?? [];

    //we want to filter the products that match the storesInfo by storeId and the stores rating will be between the storeRating
    const filter = products
        .filter((product) => product.categories.reduce((acc, curr) => acc || curr.includes(text), false) || product.description.includes(text) || product.name.includes(text))
        .filter((product) => product.price >= price[0] && product.price <= price[1])
        .filter((product) => product.rating >= productRating[0] && product.rating <= productRating[1])
        .filter((product) => {
            const store = storesInfo.find((store) => store.storeId === product.storeId) ?? emptyStore;
            return store?.rating >= storeRating[0] && store?.rating <= storeRating[1];
        })



    useEffect(() => {
    }, [text, productRating, storeRating])
    return (
        <>
            <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px', marginTop: 5 }} >
                {filter.map((product: Product) => {
                    return (
                        <ProductCard item={product} key={product.productId} canEdit={false} canDelete={false} />
                    )
                })}
            </Box>
        </>
    )
}
export default Products;