import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import { Alert, AlertTitle, Typography } from '@mui/material';
import { Password } from '@mui/icons-material';

interface AlertDialogProps {
    sevirity: "error" | "warning" | "info" | "success";
    text: string;
    open: boolean;
    onClose: () => void;
}
export default function AlertDialog({ sevirity, open, onClose, text }: AlertDialogProps) {

    const handleClose = () => {
        onClose();
    };
    return (
        <Dialog
            open={open}
            onClose={handleClose}
        >
            <Alert severity={sevirity}>
                <AlertTitle>{sevirity}</AlertTitle>
                <strong>{text}</strong>
            </Alert>
            <Button sx={{ width: '100%' }} onClick={handleClose}>okay</Button>
        </Dialog >
    );
}