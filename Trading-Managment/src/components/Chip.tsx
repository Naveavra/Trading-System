import { Chip } from "@mui/material"
interface chipProps {
    text: string;
}
const chip: React.FC<chipProps> = ({ text }) => {
    return (
        <Chip label={text} sx={{ ml: 1, mr: 1 }} />
    )
}
export default chip;