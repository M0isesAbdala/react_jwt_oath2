import { Button, Grid, type ButtonPropsColorOverrides } from "@mui/material";
import type { OverridableStringUnion } from "@mui/types";

export type CallbackCustomButtomNavigation = () => void;

export type ParameterCustomButtomNavigation = {
    label: string;
    cb: CallbackCustomButtomNavigation;
};

export type TypesCustomButtomNavigation = 'refresh' | 'new' | 'edit' | 'delete';

export type CustomButtomNavigatioProps = {
    [K in TypesCustomButtomNavigation]?: ParameterCustomButtomNavigation
};

const COLORS: Record<TypesCustomButtomNavigation, OverridableStringUnion<'inherit' | 'primary' | 'secondary' | 'success' | 'error' | 'info' | 'warning', ButtonPropsColorOverrides>> = {
    delete: 'error',
    edit: 'warning',
    new: 'primary',
    refresh: 'info'
};

export default function CustomButtomNavigation({ ...buttons }: CustomButtomNavigatioProps) {

    return (
        <Grid container justifyContent="left" spacing={1}>
            {
                Object.entries(buttons).map(([key, value], index: number) => (
                    <Grid key={`${key}-${index}`} size={3}>
                        <Button
                            fullWidth
                            variant="contained"
                            color={COLORS[key as TypesCustomButtomNavigation]}
                            onClick={value.cb}>{value.label}</Button>
                    </Grid>
                ))
            }
        </Grid>
    )
}