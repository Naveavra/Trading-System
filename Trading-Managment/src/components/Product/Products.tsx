import { Box } from "@mui/material";
import { useAppSelector } from "../../redux/store"
import { Product } from "../../types/systemTypes/Product";
import ProductCard from "../ProductInStore/Card";
import { useEffect } from "react";

interface props {
    text: string;
}
const Products: React.FC<props> = ({ text }) => {
    const products = useAppSelector((state) => state.product.responseData) ?? [];
    const filter = products.filter((product) => product.categories.reduce((acc, curr) => acc || curr.includes(text), false) || product.description.includes(text) || product.name.includes(text));

    useEffect(() => {
    }, [text])
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