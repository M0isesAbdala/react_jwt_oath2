import { createContext, useState, type ReactNode } from "react";

type TypeTheme = 'dark' | 'light';

type ThemeModeContext = {
    toggleColorMode: () => void;
    mode: TypeTheme;
}

function getTheme(): TypeTheme {
    let theme: string | null = localStorage.getItem("theme");
    if (!theme) {
        theme = 'light';
    }
    return theme as TypeTheme;
}

const CURRENT_THEME: TypeTheme = getTheme();

export const ColorModeProvider = createContext<ThemeModeContext>({ toggleColorMode: () => { }, mode: CURRENT_THEME });

type ThemeContextProps = {
    children: ReactNode;
};

export default function ThemeContext({ children }: ThemeContextProps) {

    const [mode, setMode] = useState<TypeTheme>(CURRENT_THEME);

    const colorMode: ThemeModeContext = {
        toggleColorMode: () => {
            const THEME: TypeTheme = (mode === 'light' ? 'dark' : 'light')
            localStorage.setItem("theme", THEME);
            setMode(THEME)
        },
        mode: mode
    };

    return (
        <ColorModeProvider.Provider value={colorMode}>
            {children}
        </ColorModeProvider.Provider>
    )
}