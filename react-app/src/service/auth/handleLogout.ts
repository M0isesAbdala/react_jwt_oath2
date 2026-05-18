import defaultFetch from "../defaultFetch";

export default function handleLogout(): Promise<void> {
    const URL: string = `/logout`;
    return defaultFetch<void>(URL, 'POST');
}