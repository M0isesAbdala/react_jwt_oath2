const DEFAULT_RESOURCE_URL: string = "http://mysite.com/client";

type ContentType =
    | "application/json"
    | "application/x-www-form-urlencoded"
    | "multipart/form-data"
    | "text/plain";

type BodyMap = {
    "application/json": Record<string, unknown>;
    "application/x-www-form-urlencoded": URLSearchParams;
    "multipart/form-data": FormData;
    "text/plain": string;
};

type HttpRequest<T extends ContentType> = {
    contentType: T;
    body: BodyMap[T];
};

type HttpRequestUnion = HttpRequest<'application/json'> | HttpRequest<'application/x-www-form-urlencoded'> | HttpRequest<'multipart/form-data'> | HttpRequest<'text/plain'>;

type CallbackSerializer<T extends ContentType> = (param: BodyMap[T]) => BodyInit;

type Serializes = {
    [K in ContentType]: CallbackSerializer<K>
}

const SERIALIZERS: Serializes = {
    "application/json": (body: Record<string, unknown>) => JSON.stringify(body),
    "application/x-www-form-urlencoded": (body: URLSearchParams) => body.toString(),
    "multipart/form-data": (body: FormData) => body,
    "text/plain": (body: string) => body,
};

function serializeBody<T extends ContentType>(contentType: T, body: BodyMap[T]): BodyInit {
    return SERIALIZERS[contentType](body);
}

type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH' | 'OPTIONS' | 'HEAD';

type UnauthorizedInterceptorCallback = () => void;

let unauthorizedInterceptorCallback: UnauthorizedInterceptorCallback | null = null;

export function RegisterAnauthorizedCallback(cb: UnauthorizedInterceptorCallback) {
    unauthorizedInterceptorCallback = cb;
}

export default function defaultFetch<K>(url: string, method: HttpMethod, body?: HttpRequestUnion): Promise<K> {
    return new Promise<K>(async (resolve, reject) => {
        try {

            const HEADERS: HeadersInit = {};

            const REQUEST: RequestInit = {
                method: method,
                credentials: "include"
            };

            if (body) {
                REQUEST.body = serializeBody(body.contentType, body.body);
                HEADERS['content-type'] = body.contentType;
            }

            REQUEST.headers = HEADERS;

            const FETCH = fetch(`${DEFAULT_RESOURCE_URL}${url}`, REQUEST);

            const RESPONSE = await FETCH;

            if (!RESPONSE.ok) {
                if (unauthorizedInterceptorCallback && RESPONSE.status === 401) {
                    unauthorizedInterceptorCallback();
                }
                return reject(RESPONSE);
            }

            const PARSED_RESPONSE = await parseResponse(RESPONSE);

            return resolve(PARSED_RESPONSE);

        } catch (error) {
            return reject(error);
        }
    });
}

async function parseResponse(response: Response) {
    const contentType = response.headers.get("content-type") || "";

    if (contentType.includes("application/json")) {
        return response.json();
    }

    if (contentType.includes("text/")) {
        return response.text();
    }

    if (contentType.includes("application/octet-stream")) {
        return response.arrayBuffer();
    }

    if (contentType.includes("application/pdf")) {
        return response.blob();
    }

    if (contentType.includes("image/")) {
        return response.blob();
    }

    if (contentType.includes("audio/") || contentType.includes("video/")) {
        return response.blob();
    }

    if (contentType.includes("application/x-www-form-urlencoded")) {
        const text = await response.text();
        return new URLSearchParams(text);
    }

    if (contentType.includes("multipart/form-data")) {
        return response.formData();
    }

    return response.text();
}