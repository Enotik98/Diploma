import { Pool, QueryResult} from 'pg';
import {User} from '../models/User'

export class UserRepositorie {
    constructor(private pool: Pool) {}

    async getUsers(): Promise<User[]> {
        const sql = 'SELECT id, email, f_name, l_name, role::user_role FROM  users;'

        try {
            const result: QueryResult<User> = await this.pool.query(sql);
            return result.rows;
        } catch (error) {
            throw new Error('Error getting all users');
        }
    }
    async getUserById(id: number): Promise<User | null> {
        const sql = 'SELECT id, email, f_name, l_name, role::user_role FROM users WHERE id = $1;';
        const values = [id];

        try {
            const result: QueryResult<User> = await this.pool.query(sql, values);
            return result.rows[0] || null;
        } catch (error) {
            throw new Error('Error getting user by ID');
        }
    }

    async getUserByEmail(email: string): Promise<User | null> {
        const sql = 'SELECT id, email, password, p_salt, role FROM users WHERE email = $1';
        const values = [email];

        try{
            const result  = await this.pool.query(sql,values);
            return result.rows[0] || null;
        } catch (error) {
            throw new Error('Error getting user by Login');
        }
    }

    async createUser(user: User): Promise<boolean>  {
        let query = 'INSERT INTO users (email, f_name, l_name, password, p_salt, role) VALUES ($1, $2, $3, $4, $5, $6::user_role)';
        const values = [user.email, user.f_name, user.l_name, user.password, user.p_salt, user.role];

        try {
            const result: QueryResult<User> = await this.pool.query(query, values);
            return true;
            
        } catch(error) {
            throw new  Error(`Failed to create user: ${error}`);
        }
    }

}