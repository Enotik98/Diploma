import { UserRole } from "./UserRole"

export interface User {
    id: number,
    email: string,
    f_name: string,
    l_name: string,
    password: string,
    p_salt: string,
    role: UserRole
}