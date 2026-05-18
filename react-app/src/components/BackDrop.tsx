import type { JSX } from "@emotion/react/jsx-runtime";
import { Backdrop, CircularProgress } from "@mui/material";
import { useState } from "react";

type CallbackBackDrop = (param: boolean) => void;

let backDrop: CallbackBackDrop | null = null;

export function handleCackDrop(param: boolean) {
    if (backDrop) {
        backDrop(param);
    }
}

export default function BackDrop(): JSX.Element {
    const [open, setOpen] = useState<boolean>(false);

    backDrop = (param: boolean) => {
        setOpen(param);
    }

    return (
        <Backdrop
            sx={(theme) => ({ color: '#fff', zIndex: theme.zIndex.drawer + 1 })}
            open={open}
        >
            <CircularProgress color="inherit" />
        </Backdrop>
    )
}