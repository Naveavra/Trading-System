import { Alert, AlertTitle, Button } from "@mui/material"
interface SuccessAlertProps {
    message: string
    onClose: () => void;
}
const SuccessAlert: React.FC<SuccessAlertProps> = ({ message, onClose }) => {
    return (
        <Alert sx={{ margin: 'auto', width: '50%' }}
            action={
                <Button color="inherit" size="small" onClick={onClose}>
                    CLOSE
                </Button>
            }
        >
            {message}
        </Alert>
    );
}
export default SuccessAlert;