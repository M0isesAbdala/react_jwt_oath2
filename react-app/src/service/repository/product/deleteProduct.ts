import defaultFetch from "../../defaultFetch";
import PRODUCT_PATH from "./productPath";

export default async function deleteProduct(id: number): Promise<void> {
    const URL: string = `${PRODUCT_PATH}/${id}`;
    return defaultFetch<void>(URL, 'DELETE');
}