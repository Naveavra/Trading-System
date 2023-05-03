import { Alert, AlertTitle } from "@mui/material"

const SuccessAlert = (message: string) => {
    return (
        <Alert severity="success">
            <AlertTitle>Success</AlertTitle>
            <strong>{message}</strong>
        </Alert>
    );
}
export default SuccessAlert;