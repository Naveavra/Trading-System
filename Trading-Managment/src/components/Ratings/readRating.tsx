import { Rating, SxProps, Theme } from "@mui/material";

interface ratingProps {
    rating: Number;
    style: SxProps<Theme>;
    onChange?: () => void;
}
const ReadOnlyRating: React.FC<ratingProps> = ({ rating, style }) => {
    return (
        <Rating name="read-only" sx={style} value={rating} readOnly />
    )
}
export default ReadOnlyRating;