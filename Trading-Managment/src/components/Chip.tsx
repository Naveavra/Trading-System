import { Chip } from "@mui/material"
interface chipProps {
    text: string;
}
const chip: React.FC<chipProps> = ({ text }) => {
    return (
        <Chip label={text} />
    )
}
export default chip;