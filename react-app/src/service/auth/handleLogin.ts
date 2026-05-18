import type { LoginRequest } from "../../context/auth/AuthContext";
import defaultFetch from "../defaultFetch";

export default function handleLogin(body: LoginRequest): Promise<void> {
    const URL: string = `/login`;
    return defaultFetch<void>(URL, 'POST', { body: body, contentType: 'application/json' });
}