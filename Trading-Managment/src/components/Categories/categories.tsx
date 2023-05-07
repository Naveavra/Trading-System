import React from "react";
import "./categories.css";
import { Link } from "react-router-dom";
import { Typography } from "@mui/material";
import { useAppSelector } from "../../redux/store";
import { Category } from "../../types/systemTypes/Categorie";

const Categories = () => {
    const categories: Category[] = [{ id: 1, name: 'nike', img: 'https://images.pexels.com/photos/1598505/pexels-photo-1598505.jpeg' }, { id: 2, name: 'adidas', img: 'https://images.pexels.com/photos/39362/the-ball-stadion-football-the-pitch-39362.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1' }]//useAppSelector((state) => state.market.categories);
    return (
        <>
            <div className="categories">
                <>
                    {categories.map((category: Category) => {
                        return (
                            <div className="col col-l">
                                <div className="row">
                                    <div className="col">
                                        <div className="row">
                                            {" "}
                                            <img
                                                src={category.img}
                                                alt=""
                                            />
                                            <button>
                                                <Link className="link" to="/products/1">
                                                    <Typography variant="h5" component="h5" className="logo" fontFamily={"Gill Sans"} color={"white"}>
                                                        {category.name}
                                                    </Typography>
                                                </Link>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )
                    })
                    }
                </>
            </div>

            < div className="categories" >
                <div className="col">
                    <div className="row">
                        <img
                            src="https://images.pexels.com/photos/818992/pexels-photo-818992.jpeg?auto=compress&cs=tinysrgb&w=1600"
                            alt=""
                        />
                        <button>
                            <Link className="link" to="/products/1">
                                <Typography variant="h5" component="h5" className="logo" fontFamily={"Gill Sans"} color={"white"}>
                                    Sale
                                </Typography>
                            </Link>
                        </button>
                    </div>
                    <div className="row">
                        <img
                            src="https://images.pexels.com/photos/2036646/pexels-photo-2036646.jpeg?auto=compress&cs=tinysrgb&w=1600"
                            alt=""
                        />
                        <button>
                            <Link to="/products/1" className="link">
                                <Typography variant="h5" component="h5" className="logo" fontFamily={"Gill Sans"} color={"white"}>
                                    Women
                                </Typography>
                            </Link>
                        </button>
                    </div>
                </div>
                <div className="col">
                    <div className="row">
                        {" "}
                        <img
                            src="https://images.pexels.com/photos/1813947/pexels-photo-1813947.jpeg?auto=compress&cs=tinysrgb&w=1600"
                            alt=""
                        />
                        <button>
                            <Link to="/products/1" className="link">
                                <Typography variant="h5" component="h5" className="logo" fontFamily={"Gill Sans"} color={"white"}>
                                    New Seson
                                </Typography>
                            </Link>
                        </button>
                    </div>
                </div>
                <div className="col col-l">
                    <div className="row">
                        <div className="col">
                            <div className="row">
                                <img
                                    src="https://images.pexels.com/photos/1192609/pexels-photo-1192609.jpeg?auto=compress&cs=tinysrgb&w=1600"
                                    alt=""
                                />
                                <button>
                                    <Link to="/products/1" className="link">
                                        <Typography variant="h5" component="h5" className="logo" fontFamily={"Gill Sans"} color={"white"}>
                                            Men
                                        </Typography>
                                    </Link>
                                </button>
                            </div>
                        </div>
                        <div className="col">
                            <div className="row">
                                {" "}
                                <img
                                    src="https://images.pexels.com/photos/2703202/pexels-photo-2703202.jpeg?auto=compress&cs=tinysrgb&w=1600"
                                    alt=""
                                />
                                <button>
                                    <Link to="/products/1" className="link">
                                        <Typography variant="h5" component="h5" className="logo" fontFamily={"Gill Sans"} color={"white"}>
                                            Accessories
                                        </Typography>
                                    </Link>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        <img
                            src="https://images.pexels.com/photos/1159670/pexels-photo-1159670.jpeg?auto=compress&cs=tinysrgb&w=1600"
                            alt=""
                        />
                        <button>
                            <Link to="/products/1" className="link">
                                <Typography variant="h5" component="h5" className="logo" fontFamily={"Gill Sans"} color={"white"}>
                                    Shoes
                                </Typography>
                            </Link>
                        </button>
                    </div>
                </div>
            </div >
        </>
    );
};

export default Categories;