import { Request, Response, NextFunction } from "express";
import Token from "../helpers/TokenHelper";
import { UserRole } from "../models/UserRole";

export const authentificateUser = (req: Request, res: Response, next: NextFunction) => {
    const token = req.headers.authorization?.split("Bearer ")[1];
    
    if (!token) return res.status(403).json({ error: 'No token provided.' });

    const decoded = Token.verifyToken(token);
    if (!decoded) {
        return res.status(401).json({ error: 'Invalid or expired token.' });
    }

    req.user = decoded;
    
    next();
};

export const checkRole = (allowedRoles: UserRole[]) => {
    return (req: Request, res: Response, next: NextFunction) => {
        console.log(req.user);
        console.log(allowedRoles);
        
        if (req.user && allowedRoles.includes(req.user?.userRole)) {
            next();
        } else {
            res.status(403).json({ message: "Access denied" });
        }
    };
};

