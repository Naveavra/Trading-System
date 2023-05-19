import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Button } from '@mui/material';
import { useAppSelector } from '../../redux/store';
const Tests: React.FC = () => {
    const [notification, setNotification] = useState('');
    const userId = 1; // Replace with the actual user ID
    useEffect(() => {
        const fetchNotification = async () => {
            try {
                const response = await axios.get(`http://localhost:4567/notifications/${userId}`);
                debugger;
                if (response.status === 200) {
                    setNotification(response.data);
                    fetchNotification();
                } else {
                    setNotification("");
                }
            } catch (error) {
                console.error('Error fetching notification:', error);
            }
        };

        fetchNotification();
    }, [userId]);
    const sendNotifiy = () => {
        axios.post(`http://localhost:4567/notifications`, { userToSend: 1, message: `hiiii from ${userId}` })
            .then(response => {
                // Do something with the response if necessary
            })
            .catch(error => {
                // Handle the error if necessary
            });
        // dispatch(ping(userId));
    }
    return (
        <>
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={sendNotifiy}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'notify'}
            </Button >

        </>
    )
}
export default Tests;