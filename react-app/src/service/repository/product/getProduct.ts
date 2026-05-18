import defaultFetch from "../../defaultFetch";
import type { ProductDTO } from "../../dto/ProductDTO";
import PRODUCT_PATH from "./productPath";

export default async function getProduct(id: number): Promise<ProductDTO> {
    const URL: string = `${PRODUCT_PATH}/${id}`;
    return defaultFetch<ProductDTO>(URL, 'GET');
}