import { Paper, Typography, Grid } from '@mui/material';
import { useCustomRouteHook } from '../../hook/useCustomRouteHook';
import { DataGrid } from '@mui/x-data-grid';
import type { GridColDef, GridPaginationModel, GridRowSelectionModel } from '@mui/x-data-grid';
import type { ProductDTO } from '../../service/dto/ProductDTO';
import { useEffect, useState } from 'react';
import CustomButtomNavigation from '../../components/navigation/CustomButtomNavigation';
import { useTranslation } from 'react-i18next';
import services from '../../service/Services';
import type { PaginationDTO } from '../../service/dto/PaginationDTO';
import { handleCackDrop } from '../../components/BackDrop';
import { showSnackBarMessage, type SnackbarType } from '../../components/Snackbar';
import type { TFunction } from 'i18next';

function columns(t: TFunction<"product", undefined>): GridColDef<ProductDTO>[] {
    const HEADERS: GridColDef<ProductDTO>[] = [
        {
            field: 'id',
            headerName: 'ID',
            width: 100,
        },
        {
            field: 'name',
            headerName: 'Nome',
            width: 150,
        },
        {
            field: 'price',
            headerName: 'Preço',
            flex: 2,
        },
        {
            field: 'barcode',
            headerName: 'Código de barras',
            type: 'number',
            flex: 1,
        }
    ];

    return HEADERS.map((h) => {
        h.headerName = t(`headers.${h.field}`);
        return h;
    });
};

type PaginationLabel = {
    from: number;
    to: number;
    count: number;
    estimated: number | undefined;
}

function handleLabelPagination({ from, to, count, estimated }: PaginationLabel): string {
    const unknownRowCount = count == null || count === -1;
    if (!estimated) {
        return `${(from)} – ${(to)} / ${!unknownRowCount ? (count) : `${(to)}`}`;
    }
    const estimatedLabel = estimated && estimated > to ? `${(estimated)}` : `${(to)}`;
    return `${(from)} – ${(to)} / ${!unknownRowCount ? (count) : estimatedLabel}`;
}

const LIMIT = 100;

type Pagination = {
    pagination: GridPaginationModel;
    total: number
}

export default function ProductList() {
    const { t } = useTranslation("product");
    const { goTo } = useCustomRouteHook();
    const [model, setModel] = useState<GridRowSelectionModel | undefined>(undefined);
    const [pagination, setPagination] = useState<Pagination>({ pagination: { page: 0, pageSize: LIMIT }, total: 0 });
    const [rows, setRows] = useState<ProductDTO[]>([]);

    const search = async (page: number): Promise<void> => {
        handleCackDrop(true);
        showSnackBarMessage(t("loadingMessage"), 'info');
        const PAGINATION: PaginationDTO<ProductDTO> | null = await services('listProduct', page, LIMIT).then((response: PaginationDTO<ProductDTO>) => response).catch(() => null);

        if (!PAGINATION) {
            handleCackDrop(false);
            return;
        }

        setRows(PAGINATION.content);

        setPagination({
            pagination: {
                page: PAGINATION.number,
                pageSize: PAGINATION.numberOfElements
            },
            total: PAGINATION.totalElements
        });

        handleCackDrop(false);
    }


    useEffect(() => {
        search(0);
    }, []);

    const handleRowClick = (row: GridRowSelectionModel) => {
        setModel(row);
    }

    const handleNewProduct = (): void => {
        goTo('/product');
    }

    const handleEditProduct = async (): Promise<void> => {
        if (model) {
            handleCackDrop(true);
            const [FIRST_ITEM] = model.ids;
            const ITEM: ProductDTO | undefined = rows.find((p: ProductDTO) => p.id === FIRST_ITEM);
            if (ITEM && ITEM.id) {
                const RESPONSE: ProductDTO | null = await services('getProduct', ITEM.id).then((response: ProductDTO) => response).catch(() => null);
                if (RESPONSE) {
                    goTo('/product', RESPONSE);
                } else {
                    showSnackBarMessage(t("errors.2"), 'error');
                }
            }
            handleCackDrop(false);
        }
    }

    const handleDeleteProduct = async (): Promise<void> => {
        if (model) {
            const [FIRST_ITEM] = model.ids;
            const ITEM: ProductDTO | undefined = rows.find((p: ProductDTO) => p.id === FIRST_ITEM);
            if (ITEM && ITEM.id) {
                handleCackDrop(true);
                let msg: { msg: string, type: SnackbarType } = { msg: t("delete.success"), type: 'success' };
                await services('deleteProduct', ITEM.id).then(() => {
                    search(pagination.pagination.page);
                }).catch(() => {
                    t("delete.error");
                });
                showSnackBarMessage(msg.msg, msg.type);
                handleCackDrop(false);
            }
        }
    }

    const handleRefreshProduct = (): void => {
        search(pagination.pagination.page);
    }

    const handlePagination = (model: GridPaginationModel): void => {
        search(model.page);
    }

    return (
        <>
            <Typography variant="h4" gutterBottom>{t("title")}</Typography>
            <Paper>
                <Grid container>
                    <Grid size={12}>
                        <CustomButtomNavigation
                            refresh={{
                                label: t("loadingButton"),
                                cb: handleRefreshProduct,
                            }}
                            new={{
                                label: t("newProductButton"),
                                cb: handleNewProduct,
                            }}
                            edit={{
                                label: t("editProductButton"),
                                cb: handleEditProduct,
                            }}
                            delete={{
                                label: t("deleteProductButton"),
                                cb: handleDeleteProduct,
                            }}
                        />
                    </Grid>
                    <Grid size={12} p={1}>
                        <Grid container sx={{ height: "70vh" }}>
                            <DataGrid
                                onRowSelectionModelChange={handleRowClick}
                                rows={rows}
                                columns={columns(t)}
                                pagination
                                pageSizeOptions={[]}
                                rowSelectionModel={model}
                                paginationMode="server"
                                keepNonExistentRowsSelected
                                paginationModel={pagination.pagination}
                                rowCount={pagination.total}
                                onPaginationModelChange={handlePagination}
                                localeText={{
                                    paginationDisplayedRows: handleLabelPagination
                                }}
                            />
                        </Grid>
                    </Grid>
                </Grid>
            </Paper >
        </>
    );
}