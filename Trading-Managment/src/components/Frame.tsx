import React, { useEffect, useState } from 'react';

import { Outlet, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../redux/store';
//import { mockManagers } from '../data/mockData';
//import  Navebar  from '../components/NavBar/Navbar';
import Bar from './Bars/Navbar/Navbar';
import { SearchBar } from './Bars/SearchBar/SearchBar';
import Categories2 from './Categories/category2';
import ShopsBar from './Bars/ShopBar/ShopBar';
import { Divider } from '@mui/material';
import axios from 'axios';
import ProductCard from './ProductInStore/Card';
import { Product } from '../types/systemTypes/Product';
import { getStoresInfo } from '../reducers/storesSlice';
import { getProducts } from '../reducers/productsSlice';
import { getNotifications, ping } from '../reducers/authSlice';

const DashboardFrame: React.FC = () => {

    //const themeColorMode = useAppSelector((state) => state.global.clientSettings.theme.colorMode);
    const [text, setText] = useState('');
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const token = useAppSelector((state) => state.auth.token);
    const products = useAppSelector((state) => state.product.responseData) ?? [];

    const handleSet = (text: string) => {
        setText(text);
    }
    return (<>
        <Bar headLine={"Trading System"} />
        <SearchBar text={text} set={handleSet} />
        <Divider sx={{ marginTop: 1 }} />
        <ShopsBar />
        <Divider />
        {/* <Categories /> */}
        <Categories2 />
        {text == '' ?
            <>
                {
                    products.map((product: Product) => {
                        <ProductCard item={product} key={product.productId} canEdit={false} canDelete={false} />
                    })
                }
            </>
            :
            <>
                {
                    products.filter((product) => product.description.includes(text) || product.name.includes(text) || product.categories.reduce((acc, curr) => curr.includes(text), false)).map((product: Product) => {
                        <ProductCard item={product} key={product.productId} canEdit={false} canDelete={false} />
                    })
                }
            </>
        }
        <Outlet />

    </>);
};

export default DashboardFrame;
