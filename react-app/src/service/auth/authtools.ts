const CHARACTER_LOWERCASE: string = 'abcdefghijklmnopqrstuvwxyz';
const CHARACTER_UPPERCASE: string = Array.of(...CHARACTER_LOWERCASE).map(c => c.toUpperCase()).join('');
const CHARACTER_NUMERIC = '123456789'

const ALPHA_NUMERIC_CHARACTER: string = `${CHARACTER_LOWERCASE}${CHARACTER_UPPERCASE}${CHARACTER_NUMERIC}`;

function generateRadomState(): string {
    let state: string = '';

    const INDEX: number = ALPHA_NUMERIC_CHARACTER.length;

    for (let index = 0; index < 30; index++) {
        const element: string = ALPHA_NUMERIC_CHARACTER.charAt(Math.floor(Math.random() * INDEX));
        state += element;
    }

    return state;
}

function base64UrlEncode(sourceValue: number[]): string {
    const STRING_VALUE: string = String.fromCharCode.apply(null, sourceValue);
    const BASE64_ENCODE: string = window.btoa(STRING_VALUE);
    const BASE64_URL_ENCODED: string = BASE64_ENCODE.replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
    return BASE64_URL_ENCODED;
}

function generateCodeVerifier(): string {
    let code: string = '';
    const RANDOM_BYTE_ARRAY = new Uint8Array(32);
    window.crypto.getRandomValues(RANDOM_BYTE_ARRAY);

    code = base64UrlEncode(Array.from(RANDOM_BYTE_ARRAY));

    return code;
}

async function generateCodeChallenge(code: string): Promise<string> {
    let codeChallengeValue: string = '';

    const TEXT_ENCODER = new TextEncoder();
    const ENCODE_VALUE = TEXT_ENCODER.encode(code);
    const DIGEST = await window.crypto.subtle.digest('SHA-256', ENCODE_VALUE);

    codeChallengeValue = base64UrlEncode(Array.from(new Uint8Array(DIGEST)));
    return codeChallengeValue;
}

export { generateRadomState, generateCodeVerifier, generateCodeChallenge }