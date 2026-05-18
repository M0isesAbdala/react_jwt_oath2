import defaultFetch from "../../defaultFetch";
import type { UserDTO } from "../../dto/UserDTO";
import USER_PATH from "./userPath";

export default async function createUser(body: UserDTO): Promise<UserDTO> {
    const URL: string = `${USER_PATH}`;
    return defaultFetch<UserDTO>(URL, 'POST', { body: body, contentType: 'application/json' });
}