import React from "react";
import "./Card.scss";
import { Link } from "react-router-dom";
import { Product } from "../../types/systemTypes/Product";

interface CardProps {
    item: Product;
}
const Card: React.FC<CardProps> = ({ item }) => {
    console.log(item);
    return (
        <Link className="link" to={`/product/${item.id}`}>
            <div className="card">
                <div className="image">
                    <img
                        src={item.img}
                        alt=""
                        className="mainImg"
                    />

                </div>
                <h2>{item.name}</h2>
                <p>{item.description.substring(0, 100)}...</p>
                <div className="prices">
                    <h3>${item.price}</h3>
                </div>
            </div>
        </Link>
    );
};

export default Card;