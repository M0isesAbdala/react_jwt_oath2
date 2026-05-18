import React, { useContext, useMemo, type ReactElement } from 'react';
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from '../pages/auth/Login';
import MainLayout from "../pages/main/MainLayout";
import Product from "../pages/products/Product";
import ProductList from "../pages/products/ProductList";
import UserList from '../pages/users/UserList';
import User from '../pages/users/User';
import { ColorModeProvider } from './theme/ThemeContext';
import { AuthContext, type AuthType } from './auth/AuthContext';
import BackDrop from '../components/BackDrop';
import Snackbar from '../components/Snackbar';

export type CustomRoutesPath = '/' | '/product' | '/products' | '/users' | '/user';

type CustomRoutes = {
  index: boolean;
  path: CustomRoutesPath;
  el: ReactElement
};

const ROUTES: CustomRoutes[] = [
  {
    index: true,
    path: '/',
    el: <MainLayout />
  },
  {
    index: false,
    path: '/product',
    el: <Product />
  },
  {
    index: false,
    path: '/products',
    el: <ProductList />
  },
  {
    index: false,
    path: '/user',
    el: <User />
  },
  {
    index: false,
    path: '/users',
    el: <UserList />
  },

];

export type RouteContextProps = {
  navigate: (path: CustomRoutesPath) => void;
};

const ProtectedRoute = ({ children, type }: { children: React.ReactNode, type: AuthType }) => {
  return type !== 'NONE' ? children : <Navigate to="/login" />;
};

export default function AppContent() {
  const { type } = useContext(AuthContext);

  const { mode } = useContext(ColorModeProvider);

  const theme = useMemo(() => createTheme({ palette: { mode } }), [mode]);

  return (
    <ThemeProvider theme={theme}>
      <BackDrop />
      <Snackbar />
      <CssBaseline />
      <BrowserRouter>
        <Routes>
          <Route
            path="/login"
            element={
              type === 'NONE' ? <Login /> : <Navigate to="/" />
            } />
          <Route
            path="/"
            element={
              <ProtectedRoute type={type}>
                <MainLayout />
              </ProtectedRoute>
            }>
            {
              ROUTES.map((r: CustomRoutes, index: number) => (
                <Route
                  key={index}
                  path={r.path}
                  index={r.index}
                  element={r.el}
                />
              ))
            }
            <Route path="*" element={<Navigate to="/" replace />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  );
}