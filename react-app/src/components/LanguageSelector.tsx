import React from "react";
import { Menu, MenuItem, ListItemIcon, ListItemText, IconButton, Tooltip } from "@mui/material";
import LanguageIcon from "@mui/icons-material/Language";
import { changeLanguage, type Langs } from "../i18n";

type Language = {
    lang: Langs;
    label: string;
};

const languages: Language[] = [
    { lang: "pt-BR", label: "Português" },
    { lang: "en-US", label: "English" }
];

export type LanguageSelectorProps = {
    tooltip: string;
}

export default function LanguageSelector({ tooltip }: LanguageSelectorProps) {
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);

    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleSelect = (param: Language) => {
        changeLanguage(param.lang);
        handleClose();
    };

    return (
        <>
            <Tooltip title={tooltip}>
                <IconButton
                    color="inherit"
                    onClick={handleClick}
                >
                    <LanguageIcon />
                </IconButton>
            </Tooltip>
            <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
            >
                {languages.map((lang) => (
                    <MenuItem key={lang.lang} onClick={() => handleSelect(lang)}>
                        <ListItemIcon>
                            <LanguageIcon fontSize="small" />
                        </ListItemIcon>
                        <ListItemText>{lang.label}</ListItemText>
                    </MenuItem>
                ))}
            </Menu>
        </>
    );
}