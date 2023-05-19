import { Alert, AlertTitle, Button } from "@mui/material"

interface ErrorAlertProps {
    message: string
    onClose: () => void;
}
const ErrorAlert: React.FC<ErrorAlertProps> = ({ message, onClose }) => {
    return (
        <Alert sx={{ margin: 'auto', width: '50%' }} severity="error" action={
            <Button color="inherit" size="small" onClick={onClose}>
                CLOSE
            </Button>
        }>
            <AlertTitle>Error</AlertTitle>
            <strong>{message}</strong>

        </Alert>
    );
}
export default ErrorAlert;