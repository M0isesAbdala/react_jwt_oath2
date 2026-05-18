import { TextField, Button, Box, Typography, Grid } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';
import { useCustomRouteHook } from '../../hook/useCustomRouteHook';
import type { ProductDTO } from '../../service/dto/ProductDTO';
import { useEffect, useState } from 'react';
import services from '../../service/Services';
import { showSnackBarMessage } from '../../components/Snackbar';
import type { TFunction } from 'i18next';
import convertGenericError, { type ConvertGenericError } from '../../service/ConvertGenericError';
import { handleCackDrop } from '../../components/BackDrop';

const numberFieldStyles = {
    "& input[type=number]": {
        MozAppearance: "textfield",
    },
    "& input[type=number]::-webkit-outer-spin-button": {
        WebkitAppearance: "none",
        margin: 0,
    },
    "& input[type=number]::-webkit-inner-spin-button": {
        WebkitAppearance: "none",
        margin: 0,
    },
};

function createDefaultProduct(): ProductDTO {
    return {
        barcode: '',
        name: '',
        price: 0
    }
}

function getErrorMessage(t: TFunction<"product", undefined>, code: number): string {
    return t(`errors.${code}`, t("errors.0"));
}

export default function Product() {
    const { goTo } = useCustomRouteHook();
    const { t } = useTranslation("product");
    const location = useLocation();

    const [product, setProduct] = useState<ProductDTO>(createDefaultProduct());

    useEffect(() => {
        if (location.state) {
            const P: ProductDTO = location.state;
            setProduct(P);
        }
    }, []);

    const handleValueChange = <K extends keyof ProductDTO>(key: K, value: ProductDTO[K]): void => {
        const COPY: ProductDTO = Object.assign({}, product);
        COPY[key] = value;
        setProduct(COPY);
    }

    const handleBackNavigation = (): void => {
        goTo('/products');
    }

    const handleSaveProduct = async (): Promise<void> => {
        handleCackDrop(true);

        await services(!product.id ? 'createProduct' : 'editProduct', product).then(() => {
            showSnackBarMessage(t("sucessSaveMessage"), 'info');
            goTo('/products');
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
        return;
    }

    return (
        <Box component="form">
            <Typography variant="h4" gutterBottom>{t("registerProduct")}</Typography>
            <Grid container spacing={2}>
                <Grid size={12}>
                    <Grid size={4} m={1}>
                        <Button
                            fullWidth
                            variant="contained"
                            onClick={handleBackNavigation}>{t("backRegisterProduct")}</Button>
                    </Grid>
                </Grid>
                <Grid size={{ xs: 12 }}>
                    <TextField fullWidth value={product.name} label={t("productNameField")} onChange={(event: React.ChangeEvent<HTMLInputElement>) => handleValueChange('name', event.target.value)} />
                </Grid>
                <Grid size={{ xs: 6 }}>
                    <TextField fullWidth value={product.price} label={t("productPriceField")} type="number" sx={numberFieldStyles} onChange={(event: React.ChangeEvent<HTMLInputElement>) => handleValueChange('price', parseInt(event.target.value, 10))} />
                </Grid>
                <Grid size={{ xs: 6 }}>
                    <TextField fullWidth value={product.barcode} label={t("productBarcodeField")} type="number" sx={numberFieldStyles} onChange={(event: React.ChangeEvent<HTMLInputElement>) => handleValueChange('barcode', event.target.value)} />
                </Grid>
                <Grid size={{ xs: 12 }}>
                    <Button variant="contained" onClick={handleSaveProduct}>{t("saveProduto")}</Button>
                </Grid>
            </Grid>
        </Box>
    );
}