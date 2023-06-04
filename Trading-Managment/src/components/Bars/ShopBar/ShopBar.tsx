import './ShopBar.css'; // Import the CSS file
import { useAppSelector } from '../../../redux/store';
import { Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { setWhatchedStoreInfo } from '../../../reducers/storesSlice';


const ShopsBar: React.FC = () => {
    const store_response = useAppSelector((state) => state.store.storeInfoResponseData);
    const stores_names = store_response ?? [];
    const navigate = useNavigate();
    return (
        <>
            <div className='shop-list'
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                    overflowX: 'auto',
                }}
                key={'shop-list'}
            >
                {stores_names.map((shop) => (
                    <a
                        key={shop.id}
                        href={`dashboard/shops/${shop.id}/visitor`}
                        style={{ margin: '0 10px', marginLeft: '20px' }}
                        className='shop-list__link'
                        onClick={(e) => {
                            e.preventDefault();
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




