import React, { useEffect, useRef } from 'react'
import ReactDOM from 'react-dom/client'

import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment'
import { Provider } from 'react-redux';
import { RouterProvider } from 'react-router-dom';
import { store, useAppDispatch, useAppSelector } from './redux/store';
import { router } from './router';
import axios from 'axios';
//import { hubConnection } from 'signalr-no-jquery';

const App = () => {
  const userId = useAppSelector((state) => state.auth.userId);
  const dispatch = useAppDispatch();
  const retryConnection = useRef(0);

  // const newConnection = hubConnection('http://localhost:4567/api/signalR', { logging: true, });
  // const hubProxy = newConnection.createHubProxy('NotificationHub');
  // hubProxy.on('firstConnection', () => { });
  // newConnection.start()
  //   .done(() => {
  //     retryConnection.current = 0;
  //   })
  //   .fail((e: any) => {
  //     console.log(e);
  //     console.log("signalR could not connect")
  //   })

  // newConnection.disconnected(() => {
  //   if (retryConnection.current <= 10) {
  //     retryConnection.current++;
  //     setTimeout(() => {
  //       newConnection.start().done(() => {
  //         retryConnection.current = 0;
  //       });
  //     }, 10000)
  //   }
  // })
  // // set up event listeners i.e. for incoming "message" event
  // hubProxy.on('SendNotification', function (idToSend, message) {

  //   if (idToSend == userId) {
  //     //dispatch(setNotification(message));
  //     console.log(message, "came from server to u: " + idToSend);
  //   }
  // });

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
