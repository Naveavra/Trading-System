import React from "react";
import Card from "../Card/Card";
import "./Products.css";

import { Product } from "../../types/systemTypes/Product";
import { useAppSelector } from "../../redux/store";
interface ProductsDisplayProps {
    type: string;
    products: Product[];
    canMakeChanges: boolean;
}
const ProductsDisplay: React.FC<ProductsDisplayProps> = ({ type, products, canMakeChanges }) => {
    const error = false;//useAppSelector((state) => state.products.error);
    const loading = false;//useAppSelector((state) => state.products.loading);


    return (
        <div className="featuredProducts">
            <div className="top">
                <h1>{type} products</h1>

            </div>
            <div className="bottom">
                {error
                    ? "Something went wrong!"
                    : loading
                        ? "loading"
                        : products.map((item) => <Card item={item} key={item.id} canMakeChanges={canMakeChanges} />)}
            </div>
        </div>
    );
};

export default ProductsDisplay;