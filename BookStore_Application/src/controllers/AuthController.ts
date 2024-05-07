import {Request, Response} from 'express'
import pool from '../db';
import {User} from '../models/User';
import { UserRepositorie } from '../repositories/UserRepositorie';
import Token from "../helpers/TokenHelper";
import Password from  "../helpers/PasswordHelper";

const userRpositorie = new UserRepositorie(pool);

export const authorization = async (req: Request, res:Response) => {
    try {
        const user: User = req.body;

        const dbUser: User | null = await userRpositorie.getUserByEmail(user.email);
        if(!dbUser){
            return res.status(404).send({message: "User not Found!"});
        }

        const isValid: boolean = await Password.comparePassword(user.password, dbUser.password);
        const token = Token.generateToken(dbUser.id, dbUser.role);

        return res.status(200).json({token: token, userRole: dbUser.role});
    } catch (err) {
        return res.status(500).send(`Server error\n ${err}`);
    }
}

