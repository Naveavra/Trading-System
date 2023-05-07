import { Card, CardContent, Typography, CardActions, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material'
import PropTypes from 'prop-types'
import React, { useEffect } from 'react'


interface LeavePageBlockerProps {
    open: boolean
    onClose: () => void
}
const LeavePageBlocker: React.FC<LeavePageBlockerProps> = ({ open, onClose }) => {

    const close = () => {
        console.log("close")
        window.close();
    };
    const refresh = () => {
        console.log("refresh")
        window.location.reload()
    }
    return (
        <div>
            <Dialog
                open={open}
                onClose={onClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    {"u may have unsaved changes"}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        are u sure u want to leave? or u want to refresh?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={refresh}>refresh</Button>
                    <Button onClick={close} autoFocus>
                        leave
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    )
}

export default LeavePageBlocker