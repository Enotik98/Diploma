import {Request, Response} from 'express'
import pool from '../db';
import {User} from '../models/User';
import { UserRepositorie } from '../repositories/UserRepositorie';
import Token from "../helpers/TokenHelper";
import Password from  "../helpers/PasswordHelper";
import { UserRole } from '../models/UserRole';


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

export const getUserInfo = async (req: Request, res: Response) => {
    try {
        const userId = Number(req.user?.userId) || -1;

        const user: User | null = await userRpositorie.getUserInfo(userId);
        if (!user) {
            return res.status(404).json({message: "User Not Found"});
        }

        return res.status(200).json({user});
    } catch (err) {
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const createNewUser = async (req: Request, res:Response)=>{
    try {
        const newUser: User = req.body;
        newUser.role = UserRole.CLIENT;

        const exUser: User | null = await userRpositorie.getUserByEmail(newUser.email);
        if (exUser != null ){
            return res.status(409).send({message : "User already exists"}); 
        }

        newUser.p_salt = await Password.generateSalt();
        newUser.password = await Password.hashPassword(newUser.password, newUser.p_salt);

        const user = await userRpositorie.createUser(newUser);
        if (user) {
            const token = Token.generateToken(user.id, user.role);
            return res.status(200).json({token: token});
        }
        return res.status(403).send({message: 'Error registration!'});
    } catch (err) {
        return res.status(500).send(`Server error\n ${err}`);
    }

}