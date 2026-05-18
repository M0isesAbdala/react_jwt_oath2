import defaultFetch from "../defaultFetch";

export default async function handleGetRoles(): Promise<string[]> {
    const URL: string = `/user/roles`;
    return defaultFetch<string[]>(URL, 'GET');
}