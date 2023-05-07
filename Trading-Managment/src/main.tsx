import React, { useEffect, useState } from 'react'
import ReactDOM from 'react-dom/client'

import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment'
import { Provider } from 'react-redux';
import { RouterProvider } from 'react-router-dom';
import { store, useAppDispatch, useAppSelector } from './redux/store';
import { router } from './router';
import LeavePageBlocker from './components/Dialog/leavePage';
import axios from 'axios';
import { ping } from './reducers/authSlice';


const App = () => {
  const userId = useAppSelector((state) => state.auth.userId);
  const dispatch = useAppDispatch();
  const [number, setNumber] = React.useState(0)
  const [open, setOpen] = React.useState(false)
  // useEffect(() => {
  //   window.addEventListener('beforeunload', handleBeforeUnload);
  //   window.addEventListener('unload', handleBeforeUnload);
  //   return () => {
  //     // window.removeEventListener('beforeunload', handleBeforeUnload);
  //   };
  // }, []);
  // const checkclose = async () => {
  //   return open;
  // }

  // const handleBeforeUnload = (event: BeforeUnloadEvent) => {
  //   // Do something here, such as show a confirmation dialog
  //   event.preventDefault();
  //   console.log("before unload");
  //   console.log(open);
  //   setOpen(true);
  //   (async () => {
  //     const result = await busywait(checkclose, {
  //       sleepTime: 5500,
  //       maxChecks: 20,
  //     })
  //     console.log(`Finished after ${result.backoff.time}ms (${result.backoff.iterations} iterations) with result ${result.result}`);
  //   })();
  //   event.returnValue = '';
  // };
  // Create a const named status and a function called setStatus


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
