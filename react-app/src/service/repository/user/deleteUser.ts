import defaultFetch from "../../defaultFetch";
import USER_PATH from "./userPath";

export default async function deleteUser(id: number): Promise<void> {
    const URL: string = `${USER_PATH}/${id}`;
    return defaultFetch<void>(URL, 'DELETE');
}