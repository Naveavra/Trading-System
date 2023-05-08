import React, { } from 'react'
import ReactDOM from 'react-dom/client'

import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment'
import { Provider } from 'react-redux';
import { RouterProvider } from 'react-router-dom';
import { store, useAppDispatch, useAppSelector } from './redux/store';
import { router } from './router';
import axios from 'axios';


const App = () => {
  const userId = useAppSelector((state) => state.auth.userId);
  const dispatch = useAppDispatch();
  const [number, setNumber] = React.useState(0)
  const [open, setOpen] = React.useState(false)
  const PING_INTERVAL = 10000; // 10 seconds in milliseconds

  // Send a ping to the server
  const sendPing = () => {
    if (userId != -1) {
      axios.post('http://localhost:4567/api/auth/ping', { userId: userId })
        .then(response => {
          // Do something with the response if necessary
        })
        .catch(error => {
          // Handle the error if necessary
        });
      // dispatch(ping(userId));
    }
  }

  // Call the sendPing function every 2 seconds
  const pingInterval = setInterval(sendPing, PING_INTERVAL);

  // Stop the ping interval when the user leaves the app
  const stopPing = () => {
    clearInterval(pingInterval);
  }

  return (
    <LocalizationProvider dateAdapter={AdapterMoment}>
      <RouterProvider router={router} />
    </LocalizationProvider>
  )
};

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <Provider store={store}>
    <App />
  </Provider>
);
