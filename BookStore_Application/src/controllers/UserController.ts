import {Request, Response} from 'express'
import pool from '../db';
import {User} from '../models/User';
import { UserRepositorie } from '../repositories/UserRepositorie';
import {QueryResult } from 'pg';
import Password from '../helpers/PasswordHelper';
import { UserRole } from '../models/UserRole';
import Token from '../helpers/TokenHelper';

const userRpositorie = new UserRepositorie(pool);

export const getAllUsers = async (req: Request, res:Response) =>{
    try{
        const users: User[] = await userRpositorie.getUsers();

        if (!users){
            return res.status(404).send("No Users Found");
        }
        
        return res.status(200).json(users);
    } catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
};

export const createNewUser = async (req: Request, res:Response)=>{
    try {
        const newUser: User = req.body;
        newUser.role = UserRole.CLIENT;

        const user: User | null = await userRpositorie.getUserByEmail(newUser.email);
        if (user != null ){
            return res.status(409).send({message : "User already exists"}); 
        }

        newUser.p_salt = await Password.generateSalt();
        newUser.password = await Password.hashPassword(newUser.password, newUser.p_salt);

        const result: boolean = await userRpositorie.createUser(newUser);
        return res.status(200).send("ok");
    } catch (err) {
        return res.status(500).send(`Server error\n ${err}`);
    }

}

export const getUserInfo = async (req: Request, res: Response) => {
    try {


    } catch (err) {
        return res.status(500).send(`Server error\n ${err}`);
    }
}


//-------------------

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