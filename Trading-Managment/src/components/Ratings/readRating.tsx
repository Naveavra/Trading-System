import { Rating } from "@mui/material";

interface ratingProps {
    rating: number;
    onChange?: () => void;
}
const readOnlyRting: React.FC<ratingProps> = ({ rating }) => {
    return (
        <Rating name="read-only" value={rating} readOnly />
    )
}
export default readOnlyRting;