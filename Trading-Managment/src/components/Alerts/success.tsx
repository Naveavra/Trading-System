import { Alert, AlertTitle } from "@mui/material"
interface SuccessAlertProps {
    message: string
}
const SuccessAlert: React.FC<SuccessAlertProps> = ({ message }) => {
    return (
        <Alert severity="success">
            <AlertTitle>Success</AlertTitle>
            <strong>{message}</strong>
        </Alert>
    );
}
export default SuccessAlert;