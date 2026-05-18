import { Box, Button, TextField, Typography, Paper } from '@mui/material';
import { useContext, useState } from 'react';
import PERMISSION_SERVERS, { type PermissionServer } from '../../service/auth/permissionServers';
import { AuthContext, type LoginRequest } from '../../context/auth/AuthContext';
import { useTranslation } from 'react-i18next';

export default function Login() {
    const { login } = useContext(AuthContext);
    const { t } = useTranslation("auth");
    const [body, setBody] = useState<LoginRequest>({ email: '', password: '' });

    const handleClickBtn = (provider: string): (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void => (event: React.MouseEvent<HTMLButtonElement, MouseEvent>): void => {
        event.preventDefault();
        login({ type: 'PROVIDER', provider: provider });
    }

    const handleLogin = () => {
        login({ type: 'NORMAL', body: body });
    }

    const handleValueChange = <K extends keyof LoginRequest>(key: K, value: LoginRequest[K]): void => {
        const COPY: LoginRequest = Object.assign({}, body);
        COPY[key] = value;
        setBody(COPY);
    }

    return (
        <Box sx={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Paper sx={{ p: 4, width: 300, textAlign: 'center' }}>
                <Typography variant="h5" gutterBottom>{t("welcome")}</Typography>
                <TextField value={body.email} fullWidth label={t("emailField")} margin="normal" onChange={(event: React.ChangeEvent<HTMLInputElement>) => handleValueChange('email', event.target.value)} />
                <TextField value={body.password} fullWidth label={t("passwordField")} type="password" margin="normal" onChange={(event: React.ChangeEvent<HTMLInputElement>) => handleValueChange('password', event.target.value)} />
                <Button
                    fullWidth
                    variant="outlined"
                    sx={{ mt: 2 }}
                    onClick={handleLogin}>{t("login")}</Button>
                {
                    PERMISSION_SERVERS.map((item: PermissionServer, index: number) => (
                        <Button
                            key={index}
                            fullWidth
                            variant="contained"
                            sx={{ mt: 2 }}
                            onClick={handleClickBtn(item.provider)}>{item.name}</Button>
                    ))
                }
            </Paper>
        </Box>
    );
}