import defaultFetch from "../../defaultFetch";
import type { PaginationDTO } from "../../dto/PaginationDTO";
import type { ProductDTO } from "../../dto/ProductDTO";
import PRODUCT_PATH from "./productPath";

export default async function listProduct(page: number, size: number): Promise<PaginationDTO<ProductDTO>> {
    const URL: string = `${PRODUCT_PATH}?${new URLSearchParams({ size: size.toString(), page: page.toString() }).toString()}`;
    return defaultFetch<PaginationDTO<ProductDTO>>(URL, 'GET');
}