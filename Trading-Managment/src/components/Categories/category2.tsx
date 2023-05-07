

import { Link, useNavigate } from "react-router-dom";
import "./category2.css";
import { Category } from "../../types/systemTypes/Categorie";
import { Typography } from "@mui/material";
const Categories2 = () => {
    const categories: Category[] = [{ name: 'nike', img: 'https://images.pexels.com/photos/1598505/pexels-photo-1598505.jpeg', url: 'nike' }, { name: 'adidas', img: 'https://images.pexels.com/photos/39362/the-ball-stadion-football-the-pitch-39362.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1', url: 'adidas' }]//useAppSelector((state) => state.market.categories);
    const navigate = useNavigate();
    console.log('categories', categories[0].url);
    return (
        <>
            <div className="categories">

                <Typography variant="h5" component="h5" className="logo" fontFamily={"Gill Sans"} color={"black"} onClick={() => navigate('/dashboard/categories')} sx={{ marginLeft: 2, width: 320, fontStyle: 'italic', overscrollBehavior: 'contain' }}>
                    Explore Popular Categories
                </Typography>
                {categories.map(category => (
                    <button className="category-btn" key={category.name} onClick={() => navigate(`/dashboard/products/1}`)}>
                        <div className="category-img" style={{ backgroundImage: `url(${category.img})` }}>
                            <div className="category-name">{category.name}</div>
                        </div>
                    </button>
                ))}
            </div>
        </>
    );
};
export default Categories2;