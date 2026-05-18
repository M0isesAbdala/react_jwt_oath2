import { createContext, useState, type ReactNode } from "react";
import { RegisterAnauthorizedCallback } from "../../service/defaultFetch";
import { handleCackDrop } from "../../components/BackDrop";
import handleLogoutPopup from "../../service/auth/handleLogoutPopup";
import { handleLoginPopup, type ParameterHandleCallbackPopup } from "../../service/auth/handleLoginPopup";
import handleLogin from "../../service/auth/handleLogin";
import handleLogout from "../../service/auth/handleLogout";
import handleGetRoles from "../../service/auth/handleGetRoles";

export type LoginRequest = { email: string, password: string };

export type AuthType = "PROVIDER" | "NORMAL" | 'NONE';

export type LoginParameters<K extends AuthType> = K extends "PROVIDER" ? { type: 'PROVIDER'; provider: string } : { type: 'NORMAL'; body: LoginRequest };

export type LoginParametersUnion = LoginParameters<'PROVIDER'> | LoginParameters<'NORMAL'>;

export type LoginFunction = (param: LoginParametersUnion) => Promise<void>;

type AuthContextValue = {
    type: AuthType;
    login: LoginFunction;
    logout: () => Promise<void>;
    hasRole: (role: string) => boolean;
};

export const AuthContext = createContext<AuthContextValue>({
    type: 'NONE',
    login: () => Promise.resolve(),
    logout: () => Promise.resolve(),
    hasRole: (role: string) => false,
});

type AuthContextProps = {
    children: ReactNode;
};

export default function AuthProvider({ children }: AuthContextProps) {
    const [type, setType] = useState<AuthType>("NONE");
    const [roles, setRoles] = useState<string[]>([]);

    const logoutFallback = (): void => {
        handleCackDrop(false);
        setType("NONE");
    }

    const logout = async (): Promise<void> => {
        if (type === 'PROVIDER') {
            await handleLogoutPopup().then(() => {
                logoutFallback();
            });
        } else if (type === 'NORMAL') {
            await handleLogout().then(() => {
                logoutFallback();
            });
        }
    };

    const getRoles = (): void => {
        handleGetRoles().then((response: string[]) => {
            setRoles(response);
        });
    }

    const handlePop = (param: ParameterHandleCallbackPopup) => {
        if (param.type === 'SUCCESS') {
            setType('PROVIDER');
            getRoles();
        } else if (param.type === 'ERROR') {
            console.error(param.message);
        }
    }

    const login: LoginFunction = async (param: LoginParametersUnion): Promise<void> => {
        if (param.type === 'PROVIDER') {
            handleLoginPopup(param.provider, handlePop);
        } else if (param.type === 'NORMAL') {
            console.log('chamou aqui');
            await handleLogin(param.body).then(() => {
                setType(param.type);
                getRoles();
            });
        }
        return Promise.resolve();
    }

    const hasRole = (role: string): boolean => {
        return roles.includes(role);
    }

    const AUTH_CONTEXT_VALUE: AuthContextValue = {
        type,
        login,
        logout,
        hasRole
    };

    RegisterAnauthorizedCallback(logoutFallback);
    console.log('ALTEROU AQUI');
    return (
        <AuthContext.Provider value={AUTH_CONTEXT_VALUE}>
            {children}
        </AuthContext.Provider>
    )
}