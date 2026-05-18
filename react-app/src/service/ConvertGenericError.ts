type GenericError = {
    code: number;
    message: string;
};

type ErrorFields = {
    field: string;
    Error: string;
};

type GenericErrorFields = {
    code: number;
    message: string;
    fields: ErrorFields[];
};

export type ConvertGenericError = GenericError | GenericErrorFields;

export default function convertGenericError(response: Response): Promise<ConvertGenericError> {

    return new Promise<ConvertGenericError>(async (resolve, reject) => {
        try {
            const ARRAY_BUFFER = await response.arrayBuffer();

            if (ARRAY_BUFFER.byteLength === 0) {
                return reject(null);
            }

            const DECODE = new TextDecoder("utf-8");
            const JSON_STRING: string = DECODE.decode(ARRAY_BUFFER);
            const JSON_OBJECT: ConvertGenericError = JSON.parse(JSON_STRING);

            return resolve(JSON_OBJECT);

        } catch (error) {
            return reject(error);
        }
    });


}