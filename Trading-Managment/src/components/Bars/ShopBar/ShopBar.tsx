import './ShopBar.css'; // Import the CSS file
import { useAppSelector } from '../../../redux/store';
import { Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { setWhatchedStoreInfo } from '../../../reducers/storesSlice';

const ShopsBar: React.FC = () => {
    const store_response = useAppSelector((state) => state.store.storeInfoResponseData);
    const stores_names = [
        { name: "Walmart", id: 1 },
        { name: "Amazon", id: 2 },
        { name: "Costco", id: 3 },
        { name: "Walgreens ", id: 4 },
        { name: "Kroger", id: 5 },
        { name: "CVS ", id: 6 },
        { name: "Aldi", id: 7 },
        { name: "Tesco", id: 8 },
        { name: "Target ", id: 9 },
        { name: "Carrefour", id: 10 },
        { name: "Ahold ", id: 11 },
        { name: "Lowe's ", id: 12 },
        { name: "Home ", id: 13 },
        { name: "JD", id: 14 },
        { name: "Woolworths ", id: 15 },
        { name: "Seven", id: 16 },
        { name: "Wal", id: 17 },
        { name: "Wal", id: 18 },
        { name: "Sainsbury's", id: 19 },
        { name: "Albertsons ", id: 20 },
        { name: "Metro", id: 21 },
        { name: "Loblaw ", id: 22 },
        { name: "Coles ", id: 23 },
        { name: "Auchan ", id: 24 },
        { name: "Casino ", id: 25 },
        { name: "H", id: 26 },
        { name: "Best ", id: 27 },
        { name: "Lidl", id: 28 },
        { name: "Fast ", id: 29 },
        { name: "Rewe ", id: 30 },];//store_response.data.results ?? ;
    const navigate = useNavigate();
    return (
        <>
            <div className='shop-list'
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                    overflowX: 'auto',
                }}
            >
                {stores_names.map((shop) => (
                    <a
                        key={shop.id}
                        href={`dashboard/shops/${shop.id}/visitor`}
                        style={{ margin: '0 10px', marginLeft: '20px' }}
                        className='shop-list__link'
                        onClick={(e) => {
                            e.preventDefault();
                            console.log('shop', shop);
                            setWhatchedStoreInfo(shop.id);
                            navigate(`shops/${shop.id}/visitor`);
                        }}
                    >
                        {shop.name}
                    </a>
                ))}
            </div>
            <div className='shop-list-arrow'>
                <i className="fas fa-angle-double-right"></i>
            </div>
        </>
    );
}
export default ShopsBar;




