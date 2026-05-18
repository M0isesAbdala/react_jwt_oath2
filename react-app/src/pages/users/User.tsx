import { TextField, Button, Box, Typography, Grid, FormGroup, FormControlLabel, Checkbox } from '@mui/material';
import { useEffect, useState, type SyntheticEvent } from 'react';
import { useTranslation } from 'react-i18next';
import type { UserDTO } from '../../service/dto/UserDTO';
import { useCustomRouteHook } from '../../hook/useCustomRouteHook';
import { useLocation } from 'react-router-dom';
import services from '../../service/Services';
import { showSnackBarMessage } from '../../components/Snackbar';
import convertGenericError, { type ConvertGenericError } from '../../service/ConvertGenericError';
import type { TFunction } from 'i18next';
import { handleCackDrop } from '../../components/BackDrop';

function createDefaultUser(): UserDTO {
    return {
        email: '',
        username: '',
        roles: []
    }
}

function addRole(roles: string[], key: string): string[] {
    if (!roles.includes(key)) {
        roles = [...roles, key];
    }
    return roles;
}

function getErrorMessage(t: TFunction<"user", undefined>, code: number): string {
    return t(`errors.${code}`, t("errors.0"));
}

type GetRoles = () => void;

let getRoles: GetRoles;

export default function User() {
    const { goTo } = useCustomRouteHook();
    const location = useLocation();
    const { t } = useTranslation("user");
    const [user, setUser] = useState<UserDTO>(createDefaultUser());
    const [password, setPassword] = useState<string>("");
    const [roles, setRoles] = useState<string[]>([]);

    getRoles = () => {
        services('listRoles').then((response: string[]) => {
            setRoles(response);
        })
    }

    useEffect(() => {
        if (location.state) {
            const U: UserDTO = location.state;
            setUser(U);
        }
        getRoles();
    }, []);

    const handleValueChange = <K extends keyof UserDTO>(key: K, value: UserDTO[K]): void => {
        const COPY: UserDTO = Object.assign({}, user);
        COPY[key] = value;
        setUser(COPY);
    }

    const handleBackNavigation = (): void => {
        goTo('/users');
    }

    const handleValueRoleChange = (event: SyntheticEvent, key: string, flag: boolean): void => {
        event.preventDefault();
        const COPY: UserDTO = Object.assign({}, user);
        COPY.roles = flag ? addRole(COPY.roles, key) : COPY.roles.filter(item => item !== key);
        setUser(COPY);
    }

    const handleSaveUser = async (): Promise<void> => {
        handleCackDrop(true);
        
        if (!user.id) {

            if (user.password !== password) {
                showSnackBarMessage(t('differentPasswordsErrorMessage'), 'error');
                handleCackDrop(false);
                return;
            }

        }

        await services(!user.id? 'createUser' : 'editUser', user).then(() => {
            showSnackBarMessage(t("sucessSaveMessage"), 'info');
            goTo('/users');
        }).catch(async (e: Response) => {

            const MESSAGE_ERROR: string = await convertGenericError(e).then((error: ConvertGenericError) => {
                const ERROR: string = getErrorMessage(t, error.code);
                return ERROR;
            }).catch((error) => {
                return error;
            });

            showSnackBarMessage(MESSAGE_ERROR, 'error');

        });

        handleCackDrop(false);
    }

    return (
        <Box component="form">
            <Typography variant="h4" gutterBottom>{t("registerUser")}</Typography>
            <Grid container spacing={2}>
                <Grid size={12}>
                    <Grid size={4} m={1}>
                        <Button
                            fullWidth
                            variant="contained"
                            onClick={handleBackNavigation}>{t("backRegisterUser")}</Button>
                    </Grid>
                </Grid>
                <Grid size={{ xs: 6 }}>
                    <Grid size={{ xs: 12 }}>
                        <TextField fullWidth label={t('userNameField')} margin="normal" value={user.username} onChange={(event: React.ChangeEvent<HTMLInputElement>) => handleValueChange('username', event.target.value)} />
                    </Grid>
                    <Grid size={{ xs: 12 }}>
                        <TextField fullWidth label={t('userEmailField')} type="email" margin="normal" value={user.email} onChange={(event: React.ChangeEvent<HTMLInputElement>) => handleValueChange('email', event.target.value)} />
                    </Grid>
                    {
                        !user.id &&
                        <>
                            <Grid size={{ xs: 12 }}>
                                <TextField fullWidth label={t('passwordField')} type="password" margin="normal" value={user.password} onChange={(event: React.ChangeEvent<HTMLInputElement>) => handleValueChange('password', event.target.value)} />
                            </Grid>
                            <Grid size={{ xs: 12 }}>
                                <TextField fullWidth label={t('repeatPasswordField')} type="password" margin="normal" value={password} onChange={(event: React.ChangeEvent<HTMLInputElement>) => setPassword(event.target.value)} />
                            </Grid>
                        </>
                    }
                </Grid>
                <Grid size={{ xs: 6 }}>
                    <Typography variant="h4" gutterBottom>{t("registerUserRoles")}</Typography>
                    <FormGroup>
                        {
                            roles.map((r: string, index: number) => (
                                <FormControlLabel
                                    key={`roles-${index}`}
                                    control={<Checkbox checked={user.roles.includes(r)} />}
                                    label={r}
                                    value={r}
                                    onChange={(event: React.SyntheticEvent, checked: boolean) => handleValueRoleChange(event, r, checked)}
                                />
                            ))
                        }
                    </FormGroup>
                </Grid>
            </Grid>
            <Button variant="contained" sx={{ mt: 2 }} onClick={handleSaveUser}>{t('saveUser')}</Button>
        </Box>
    );
}