import { Pool, QueryResult} from 'pg';
import { Genre } from '../models/Genre';

export class GenreRepositorie {
    constructor(private pool: Pool) {}

    async getAllGenres():  Promise<Genre[]> {
        try {
            const sql = "SELECT * FROM genres";
            const result: QueryResult<Genre> = await this.pool.query(sql);
            return result.rows;
        } catch (error) {
            throw new Error('Error getting all genres');   
        }
    }

    async getGenreById(id: number): Promise<Genre | null> {
        try {
            const sql = "SELECT * FROM genres WHERE id=$1";
            const values = [id];

            const result: QueryResult<Genre> = await  this.pool.query(sql, values);
            
            if (!result.rowCount) {
                return null;
            } else {
               return result.rows[0]  
            }
        } catch (error) {
            throw new Error('Error getting genre by id');   
        }
    }

    async createGenre(genre: Genre): Promise<Genre[]> {
        try {
            const sql = "INSERT INTO genres (genre) VALUES ($1);";
            const values = [genre.genre];
            await this.pool.query(sql, values);

            const result: QueryResult<Genre> = await this.pool.query("SELECT * FROM genres");
            return result.rows;
        } catch (error) {
            throw new Error('Error creating genre: ' + error);   
        }
    }

    async  updateGenre(id:number, genre: string): Promise<boolean>{
        try {
            const sql = "UPDATE genres SET genre = $2 WHERE id = $1";
            const values = [id, genre];
            await this.pool.query(sql,values);
            return true;
        }catch (error) {
            throw new Error('Error updating genre');
        }
    }
}