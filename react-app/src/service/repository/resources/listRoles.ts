import defaultFetch from "../../defaultFetch";

export default async function listRoles(): Promise<string[]> {
    const URL: string = `/roles`;
    return defaultFetch<string[]>(URL, 'GET');
}