import jwt from 'jsonwebtoken';
import { UserRole } from '../models/UserRole';

const generateToken = (userId: number, role: UserRole): string => {
    const userRole = role.toString();
    const payload = { userId, userRole};
    const secretKey = process.env.JWT_SECRET_KEY || 'default_secret_key';
    const options = { expiresIn: '1h' };

    return jwt.sign(payload, secretKey, options);
}

const verifyToken = (token: string): {userId: number, userRole: UserRole} | null => {
    const secretKey = process.env.JWT_SECRET_KEY || 'default_secret_key'; // Секретний ключ, який використовувався для підпису токена

    try {
        const decoded = jwt.verify(token, secretKey) as { userId: number, userRole: UserRole };
        console.log(decoded);
        return decoded;
    } catch (error) {
        return null;
    }
}

export default {generateToken, verifyToken};