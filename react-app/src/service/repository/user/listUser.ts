import defaultFetch from "../../defaultFetch";
import type { PaginationDTO } from "../../dto/PaginationDTO";
import type { UserDTO } from "../../dto/UserDTO";
import USER_PATH from "./userPath";

export default async function listUser(page: number, size: number): Promise<PaginationDTO<UserDTO>> {
    const URL: string = `${USER_PATH}?${new URLSearchParams({ size: size.toString(), page: page.toString() }).toString()}`;
    return defaultFetch<PaginationDTO<UserDTO>>(URL, 'GET');
}