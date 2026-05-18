import { Paper, Typography, Grid } from '@mui/material';
import { useCustomRouteHook } from '../../hook/useCustomRouteHook';
import { DataGrid } from '@mui/x-data-grid';
import type { GridColDef, GridPaginationModel, GridRowSelectionModel } from '@mui/x-data-grid';
import { useContext, useEffect, useState } from 'react';
import CustomButtomNavigation, { type CustomButtomNavigatioProps } from '../../components/navigation/CustomButtomNavigation';
import { useTranslation } from 'react-i18next';
import services from '../../service/Services';
import type { PaginationDTO } from '../../service/dto/PaginationDTO';
import { handleCackDrop } from '../../components/BackDrop';
import { showSnackBarMessage, type SnackbarType } from '../../components/Snackbar';
import type { UserDTO } from '../../service/dto/UserDTO';
import type { TFunction } from 'i18next';
import { AuthContext } from '../../context/auth/AuthContext';

function columns(t: TFunction<"user", undefined>): GridColDef<UserDTO>[] {
    const HEADERS: GridColDef<UserDTO>[] = [
        {
            field: 'id',
            headerName: 'ID',
            width: 100,
        },
        {
            field: 'username',
            headerName: 'Nome',
            width: 150,
        },
        {
            field: 'email',
            headerName: 'E-mail',
            flex: 2,
        },
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

export default function UserList() {
    const { t } = useTranslation("user");
    const { hasRole } = useContext(AuthContext);
    const { goTo } = useCustomRouteHook();
    const [model, setModel] = useState<GridRowSelectionModel | undefined>(undefined);
    const [pagination, setPagination] = useState<Pagination>({ pagination: { page: 0, pageSize: LIMIT }, total: 0 });
    const [rows, setRows] = useState<UserDTO[]>([]);

    const search = async (page: number): Promise<void> => {
        handleCackDrop(true);
        showSnackBarMessage(t("loadingMessage"), 'info');
        const PAGINATION: PaginationDTO<UserDTO> | null = await services('listUser', page, LIMIT).then((response: PaginationDTO<UserDTO>) => response).catch(() => null);

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
        goTo('/user');
    }

    const handleEditUser = async (): Promise<void> => {
        if (model) {
            handleCackDrop(true);
            const [FIRST_ITEM] = model.ids;
            const ITEM: UserDTO | undefined = rows.find((p: UserDTO) => p.id === FIRST_ITEM);
            if (ITEM && ITEM.id) {
                const RESPONSE: UserDTO | null = await services('getUser', ITEM.id).then((response: UserDTO) => response).catch(() => null);
                if (RESPONSE) {
                    goTo('/user', RESPONSE);
                } else {
                    showSnackBarMessage("errors.2", 'error');
                }
            }
            handleCackDrop(false);
        }
    }

    const handleDeleteProduct = async (): Promise<void> => {
        if (model) {
            const [FIRST_ITEM] = model.ids;
            const ITEM: UserDTO | undefined = rows.find((p: UserDTO) => p.id === FIRST_ITEM);
            if (ITEM && ITEM.id) {
                handleCackDrop(true);
                let msg: { msg: string, type: SnackbarType } = { msg: t("delete.success"), type: 'success' };
                await services('deleteUser', ITEM.id).then(() => {
                    search(pagination.pagination.page);
                }).catch(() => {
                    msg = { msg: t("delete.error"), type: "error" };
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

    const handleCustomButtomParameters = (): CustomButtomNavigatioProps => {

        const CUSTOM_BUTTOM_PARAMETERS: CustomButtomNavigatioProps = {
            refresh: {
                label: t("loadingButton"),
                cb: handleRefreshProduct,
            }
        };

        if (hasRole('ADMIN')) {
            CUSTOM_BUTTOM_PARAMETERS
            CUSTOM_BUTTOM_PARAMETERS['new'] = {
                label: t("newUserButton"),
                cb: handleNewProduct,
            };

            CUSTOM_BUTTOM_PARAMETERS['edit'] = {
                label: t("editUserButton"),
                cb: handleEditUser,
            };

            CUSTOM_BUTTOM_PARAMETERS['delete'] = {
                label: t("deleteUserButton"),
                cb: handleDeleteProduct,
            };
        }
        return CUSTOM_BUTTOM_PARAMETERS;
    }


    return (
        <>
            <Typography variant="h4" gutterBottom>{t("title")}</Typography>
            <Paper>
                <Grid container>
                    <Grid size={12}>
                        <CustomButtomNavigation {...handleCustomButtomParameters()} />
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