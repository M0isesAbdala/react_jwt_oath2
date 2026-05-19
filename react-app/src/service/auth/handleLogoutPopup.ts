const URL: string = `${window.location.origin}/client/logout`;
let closeEvent: null | number = null;

export default function handleLogoutPopup(): Promise<void> {
    return new Promise<void>((resolve, reject) => {
        const POPUP: Window | null = window.open(
            URL,
            "login",
            "width=500,height=600"
        );

        if (!POPUP) {
            console.error("Popup bloqueado pelo navegador");
        } else {

            function cleanEvent(): void {
                if (closeEvent) {
                    clearInterval(closeEvent);
                }
            }

            function handleWindowEvent(event: MessageEvent<any>): void {

                function removeEvet(): void {
                    cleanEvent();
                    window.removeEventListener("message", handleWindowEvent);
                }

                if (event.origin !== window.location.origin) {
                    removeEvet()
                    return reject();
                } else if (event.data.type === "LOGOUT_SUCCESS") {
                    removeEvet()
                    return resolve()
                }

                removeEvet()
                return reject();
            }

            closeEvent = setInterval(() => {
                if (POPUP.closed) {
                    cleanEvent();
                    window.removeEventListener("message", handleWindowEvent);
                    return reject();
                }
            }, 100);

            window.addEventListener("message", handleWindowEvent);
        }
    });
}