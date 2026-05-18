import type { JSX } from "@emotion/react/jsx-runtime";
import { Alert, Stack, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useState } from "react";

export type SnackbarType = "success" | "error" | "info" | "warning";

type SnackbarItem = {
    id: number;
    message: string;
    type: SnackbarType;
    duration: number;
};

type CallbackShowItem = (item: SnackbarItem) => void

let callbackShowItem: CallbackShowItem | null = null;

let count: number = 0;

export function showSnackBarMessage(message: string, type: SnackbarType = "info", duration: number = 5000) {
    if (callbackShowItem) {
        const id: number = count++;
        const SNACK_ITEM: SnackbarItem = { id, type, duration, message };
        callbackShowItem(SNACK_ITEM);
    }
}

export default function Snackbar(): JSX.Element {

    const [snacks, setSnacks] = useState<SnackbarItem[]>([]);

    callbackShowItem = (item: SnackbarItem) => {

        let e: number = setTimeout(() => {
            removeSnack(item.id);
            clearTimeout(e);
        }, item.duration);

        setSnacks((prev) => [...prev, item]);
    };

    const removeSnack = (id: number) => {
        setSnacks((prev) => prev.filter((s) => s.id !== id));
    };
    console.log('asudhausdh', snacks);
    return (
        <Stack
            spacing={1}
            sx={{
                position: "fixed",
                bottom: 16,
                left: 16,
                zIndex: 9999,
                display: "flex",
                flexDirection: "column",
                gap: 1,
            }}
        >
            {
                snacks.map((snack: SnackbarItem) => (
                    <Alert
                        key={snack.id}
                        severity={snack.type}
                        variant="filled"
                        onClick={() => removeSnack(snack.id)}
                        sx={{
                            minWidth: 300,
                            alignItems: "center",
                            "& .MuiAlert-action": {
                                paddingTop: 0,
                                alignItems: "center",
                            },
                        }}
                        action={
                            <IconButton
                                size="small"
                                color="inherit"
                                onClick={() => removeSnack(snack.id)}
                            >
                                <CloseIcon fontSize="small" />
                            </IconButton>
                        }
                    >
                        {snack.message}
                    </Alert>
                ))
            }
        </Stack>
    )
}