import { Alert, AlertTitle } from "@mui/material"

interface ErrorAlertProps {
    message: string
}
const ErrorAlert: React.FC<ErrorAlertProps> = ({ message }) => {
    return (
        <Alert severity="error">
            <AlertTitle>Error</AlertTitle>
            <strong>{message}</strong>
        </Alert>
    );
}
export default ErrorAlert;