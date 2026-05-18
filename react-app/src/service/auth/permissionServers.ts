export type PermissionServer = {
    url: string;
    name: string;
    provider: string
}

const PERMISSION_SERVERS: PermissionServer[] = [
    {
        name: "Authentication server",
        url: "http://mysite.com/client",
        provider: "web-client-oidc"
    }
];

export default PERMISSION_SERVERS;