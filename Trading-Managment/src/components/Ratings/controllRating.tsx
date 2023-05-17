import { Rating } from "@mui/material";
interface controlledRatingProps {
    rating: number;
    setValue: (newVal: number | null) => void;
}
const controlledRating: React.FC<controlledRatingProps> = ({ rating, setValue }) => {
    return (
        <Rating
            name="simple-controlled"
            value={rating}
            onChange={(event, newValue) => {
                setValue(newValue);
            }}
        />
    );
}
export default controlledRating;