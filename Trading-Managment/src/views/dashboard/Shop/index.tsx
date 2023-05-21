import { Card, CardContent, Typography } from "@mui/material";
import { Box } from "@mui/system";
import { Outlet } from "react-router";
import Bar2 from "../../../components/Bars/Navbar/NavBar2";
import ProductCard from "../../../components/ProductInStore/Card";
import { store, useAppSelector } from "../../../redux/store";

const Shops: React.FC = () => {
    const store = useAppSelector((state) => state.store.storeState.wahtchedStoreInfo);
    const products = useAppSelector((state) => state.product.responseData?.data.results);
    const ourProducts = products?.filter((product) => product.storeId === store.id);
    return (
        <>
            <Bar2 headLine={`wellcome to the store`} />
            <Box>
                <Card sx={{ minWidth: 275 }}>
                    <CardContent>
                        <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
                            about us
                        </Typography >
                        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>

                        </Typography>
                        <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                            <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 73, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                                {store.description}
                                decreaption about the store
                            </Typography >
                        </Box>

                    </CardContent>
                </Card>
            </Box >
            <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                {!!ourProducts && ourProducts.map((product) => {
                    return (
                        <ProductCard item={product} canDelete={false} canEdit={false} key={product.id} />
                    );
                })
                }
            </Box>
            <Outlet />
        </>
    );
}
export default Shops;
