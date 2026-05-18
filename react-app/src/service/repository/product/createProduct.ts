import defaultFetch from "../../defaultFetch";
import type { ProductDTO } from "../../dto/ProductDTO";
import PRODUCT_PATH from "./productPath";

export default async function createProduct(product: ProductDTO): Promise<ProductDTO> {
    return defaultFetch<ProductDTO>(PRODUCT_PATH, 'POST', { contentType: 'application/json', body: product });
}