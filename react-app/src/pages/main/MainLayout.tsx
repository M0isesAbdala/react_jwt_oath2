import { Outlet } from 'react-router-dom';
import { Box, Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText, AppBar, Toolbar, Typography, IconButton, Tooltip } from '@mui/material';
import { Inventory, People, Home, DarkMode, Brightness7, Logout } from '@mui/icons-material';
import { useContext, type ReactElement } from 'react';
import { ColorModeProvider } from '../../context/theme/ThemeContext';
import { AuthContext } from '../../context/auth/AuthContext';
import { type CustomRoutesPath } from '../../context/AppContent';
import { useCustomRouteHook } from '../../hook/useCustomRouteHook';
import LanguageSelector from '../../components/LanguageSelector';
import { useTranslation } from 'react-i18next';
import { handleCackDrop } from '../../components/BackDrop';

type MenuItens = {
    path: CustomRoutesPath,
    key: string
    icon: ReactElement;
}

const MENU_ITENS: MenuItens[] = [
    { key: 'home', icon: <Home />, path: '/' },
    { key: 'products', icon: <Inventory />, path: '/products' },
    { key: 'users', icon: <People />, path: '/users' },
];

const drawerWidth = 240;

export default function MainLayout() {
    const { t } = useTranslation("menu");
    const { logout } = useContext(AuthContext);
    const { toggleColorMode, mode } = useContext(ColorModeProvider);
    const { goTo } = useCustomRouteHook();

    const handleLogout = (): void => {
        handleCackDrop(true);
        logout();
    }

    return (
        <Box sx={{ display: 'flex' }}>
            <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
                <Toolbar>
                    <Typography variant="h6" noWrap sx={{ flexGrow: 1 }}>Material App</Typography>
                    <Tooltip title={t("themeButton")}>
                        <IconButton onClick={toggleColorMode} color="inherit"> {mode === 'light' ? <DarkMode /> : <Brightness7 />} </IconButton>
                    </Tooltip>
                    <LanguageSelector tooltip={t("langButton")} />
                    <Tooltip title={t("logoutButton")}>
                        <IconButton onClick={handleLogout} color="inherit"><Logout /></IconButton>
                    </Tooltip>
                </Toolbar>
            </AppBar>
            <Drawer variant="permanent" sx={{ width: drawerWidth, [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' } }}>
                <Toolbar />
                <List>
                    {MENU_ITENS.map((item) => (
                        <ListItem key={item.key} disablePadding>
                            <ListItemButton onClick={() => goTo(item.path)}>
                                <ListItemIcon>{item.icon}</ListItemIcon>
                                <ListItemText primary={t(item.key)} />
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
            </Drawer>
            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <Toolbar />
                <Outlet />
            </Box>
        </Box>
    );
}