let closeEvent: null | number = null;

export type TypeParameterHandleCallbackPopup = "SUCESS" | 'ERROR';

export type SucessParameterHandleCallbackPopup = {
    type: "SUCCESS"
};

export type ErrorParameterHandleCallbackPopup = {
    type: "ERROR",
    message: any
};

export type ParameterHandleCallbackPopup = SucessParameterHandleCallbackPopup | ErrorParameterHandleCallbackPopup;

export type HandleCallbackPopup = (param: ParameterHandleCallbackPopup) => void;

export function handleLoginPopup(provider: string, cb: HandleCallbackPopup) {

    const URL: string = `${window.location.origin}/client/oauth2/authorization/${provider}`

    const popup: Window | null = window.open(
        URL,
        "login",
        "width=500,height=600"
    );

    if (!popup) {
        console.error("Popup bloqueado pelo navegador");
    } else {

        function cleanEvent(): void {
            if (closeEvent) {
                clearInterval(closeEvent);
            }
        }

        function handleWindowEvent(event: MessageEvent<any>): void {
            if (event.origin !== window.location.origin) {
                return;
            } else if (event.data.type === "LOGIN_SUCCESS") {
                cb({ type: 'SUCCESS' });
            } else if (event.data.type === "LOGIN_ERROR") {
                cb({ type: 'ERROR', message: event.data.message });
            }
            window.removeEventListener("message", handleWindowEvent);
            cleanEvent();
        }

        closeEvent = setInterval(() => {
            if (popup.closed) {
                cleanEvent();
                window.removeEventListener("message", handleWindowEvent);
            }
        }, 100);

        window.addEventListener("message", handleWindowEvent);
    }

}
