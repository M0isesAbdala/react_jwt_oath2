import { useNavigate } from "react-router-dom";
import type { CustomRoutesPath } from "../context/AppContent";

type CustomRouteHookProps = {
    goTo: (path: CustomRoutesPath, params?: any) => void
};

export function useCustomRouteHook(): CustomRouteHookProps {
    const navigate = useNavigate();

    const goTo = (path: CustomRoutesPath, params?: any): void => {
        navigate(path, params !== undefined ? { state: params } : undefined);
    }

    return { goTo };
}