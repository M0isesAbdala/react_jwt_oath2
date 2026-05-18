import deleteProduct from "./repository/product/deleteProduct";
import listProduct from "./repository/product/listProduct";
import createProduct from "./repository/product/createProduct";
import listRoles from "./repository/resources/listRoles";
import createUser from "./repository/user/createUser";
import editUser from "./repository/user/editUser";
import listUser from "./repository/user/listUser";
import editProduct from "./repository/product/editProduct";
import deleteUser from "./repository/user/deleteUser";
import getProduct from "./repository/product/getProduct";
import getUser from "./repository/user/getUser";

const ACTIONS_SERVICE = {
    listProduct: listProduct,
    getProduct: getProduct,
    createProduct: createProduct,
    editProduct: editProduct,
    deleteProduct: deleteProduct,
    listUser: listUser,
    getUser: getUser,
    createUser: createUser,
    editUser: editUser,
    deleteUser: deleteUser,
    listRoles: listRoles
};

export default function services<K extends keyof typeof ACTIONS_SERVICE>(action: K, ...args: Parameters<typeof ACTIONS_SERVICE[K]>): ReturnType<typeof ACTIONS_SERVICE[K]> {
    return (ACTIONS_SERVICE[action] as any)(...args);
}
