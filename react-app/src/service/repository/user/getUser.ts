import defaultFetch from "../../defaultFetch";
import type { UserDTO } from "../../dto/UserDTO";
import USER_PATH from "./userPath";

export default async function getUser(id: number): Promise<UserDTO> {
    const URL: string = `${USER_PATH}/${id}`;
    return defaultFetch<UserDTO>(URL, 'GET');
}