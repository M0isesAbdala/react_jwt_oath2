import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import auth_pt from "./locales/pt-BR/auth.json";
import menu_pt from "./locales/pt-BR/menu.json";
import product_pt from "./locales/pt-BR/product.json";
import user_pt from "./locales/pt-BR/user.json";
import auth_en from "./locales/en-US/auth.json";
import menu_en from "./locales/en-US/menu.json";
import product_en from "./locales/en-US/product.json";
import user_en from './locales/en-US/user.json'

const LANGS = ['pt-BR', 'en-US'] as const;

export type Langs = typeof LANGS[number];

i18n.use(initReactI18next)
    .init({
        resources: {
            'pt-BR': {
                auth: auth_pt,
                menu: menu_pt,
                product: product_pt,
                user: user_pt
            },
            'en-US': {
                auth: auth_en,
                menu: menu_en,
                product: product_en,
                user: user_en
            },
        },
        lng: getLang(),
        fallbackLng: "en-US",
        interpolation: {
            escapeValue: false,
        },
    });

export default i18n;

export function getLang(): string {
    let lang: string = 'pt-BR';

    const LANG_STORED: string | null = localStorage.getItem("lang");

    if (LANG_STORED) {
        lang = LANG_STORED;
    } else {
        const LAG_NAVIGATOR: string = navigator.language;
        if (LANGS.find((l: string) => l === LAG_NAVIGATOR)) {
            lang = LAG_NAVIGATOR
        }
    }


    document.documentElement.lang = lang;
    return lang;
}

export function changeLanguage(lang: Langs) {
    localStorage.setItem("lang", lang);
    i18n.changeLanguage(lang);
}

i18n.on("languageChanged", (lng: string) => {
    document.documentElement.lang = lng;
});