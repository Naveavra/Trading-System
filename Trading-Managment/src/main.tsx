import React, { useEffect, useRef } from 'react'
import ReactDOM from 'react-dom/client'

import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment'
import { Provider } from 'react-redux';
import { RouterProvider } from 'react-router-dom';
import { store, useAppDispatch, useAppSelector } from './redux/store';
import { router } from './router';
import axios from 'axios';
import { persistor } from './redux/store';
import { PersistGate } from 'redux-persist/integration/react';
//import { hubConnection } from 'signalr-no-jquery';

import './main.css';

const App = () => {
  const userId = useAppSelector((state) => state.auth.userId);
  const dispatch = useAppDispatch();


  return (
    <LocalizationProvider dateAdapter={AdapterMoment}>
      <RouterProvider router={router} />
    </LocalizationProvider>
  )
};

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <App />
    </PersistGate>
  </Provider>
);
