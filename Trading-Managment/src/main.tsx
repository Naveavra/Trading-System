import React, { useEffect } from 'react'
import ReactDOM from 'react-dom/client'

import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment'
import { Provider } from 'react-redux';
import { RouterProvider } from 'react-router-dom';
import { store, useAppDispatch, useAppSelector } from './redux/store';
import { router } from './router';
import AlertDialog from './components/Dialog/AlertDialog';
import MyComponent from './reload';
import { login } from './reducers/authSlice';
const App = () => {
  const userId = useAppSelector((state) => state.auth.userId);
  const dispatch = useAppDispatch();
  const [number, setNumber] = React.useState(0)
  const [open, setOpen] = React.useState(false)
  // window.addEventListener("beforeunload", function (event) {
  //   //if u want ro show message opun leave uncomment this:
  //   event.returnValue = (() => {
  //     console.log("hi");
  //     setOpen(true);
  //   }); // this is the msg that will be shown
  //   //dispatch(exit({userId:userId}))


  // })
  // // on guest log - dosnt require , can be done with continu as guest botton
  // // const myWindow = window.open("http://localhost:4567/auth/login", "MsgWindow", "width=200,height=100");
  // // console.log("window open", myWindow)
  //useEffect(() => {
  //   const handleUnload = () => {
  //     console.log('refresh');
  //   };

  //   window.addEventListener('unload', handleUnload);

  //   return () => {
  //     console.log('close');
  //     window.removeEventListener('unload', handleUnload);
  //   };

  useEffect(() => {
    const handleUnload = () => {
      if (performance.navigation.type === performance.navigation.TYPE_RELOAD) {
        console.log('Page was reloaded');
      } else if (performance.navigation.type === performance.navigation.TYPE_NAVIGATE) {
        console.log('Page was navigated away from');
      } else {
        dispatch(login({ rememberMe: true, email: "close", password: "" }));
        console.log('!!!!!!!!!!!!!!!!!!!!!!!!!!Page was closed');
      }
    };

    window.addEventListener('unload', handleUnload);

    return () => {
      window.removeEventListener('unload', handleUnload);
    };
  }, []);

  // useEffect(() => {
  //   const handleBeforeUnload = (event: BeforeUnloadEvent) => {
  //     const message = 'Are you sure you want to leave?';
  //     event.returnValue = message; // This line shows a confirmation dialog to the user.
  //     return message;
  //   };

  //   window.addEventListener('beforeunload', handleBeforeUnload);

  //   return () => {
  //     window.removeEventListener('beforeunload', handleBeforeUnload);
  //   };
  // }, []);

  return (
    <LocalizationProvider dateAdapter={AdapterMoment}>

      <AlertDialog sevirity={'error'} text={'a u sure u want to leave?'} open={open} onClose={() => setOpen(false)} />
      <RouterProvider router={router} />
    </LocalizationProvider>
  )
};

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <Provider store={store}>
      <App />
    </Provider>
  </React.StrictMode>
);
