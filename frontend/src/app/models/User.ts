export type User = {
    userId: number;
    name: string;
    email: string;
    password: string;
}

export type RegistrationUser = {
    name: string;
    email: string;
    password: string;
}

export type LoginUser = {
    email: string;
    password: string;
}