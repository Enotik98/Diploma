import { Pool, QueryResult} from 'pg';
import {Book} from '../models/Book';
import { Genre } from '../models/Genre';
import { CustomError } from '../helpers/CustomError';

export class BookRepositorie {
    constructor(private pool: Pool) {}

    async getAllBooks(): Promise<Book[]> {
        const sql = "SELECT b.id, b.title, b.author, b.pub_year, b.price, b.quantity, " +
                    " ARRAY_AGG(jsonb_build_object('id', ge.id, 'gemre', ge.genre)) AS genres, b.about , b.isbn" +
                    " FROM books b LEFT JOIN books_genres bg ON b.id = bg.book_id " +
                    " LEFT JOIN genres ge ON bg.genre_id = ge.id"+
                    " GROUP BY b.id, b.title, b.author, b.pub_year, b.price, b.quantity, b.about, b.isbn; ";
    
        try {
            const result: QueryResult<Book> = await this.pool.query(sql);
            if (result.rowCount === 0) throw new Error('No books found');
            else return result.rows;

        } catch (err) {
            throw new CustomError(String(err), 500);
        }
    }
    async getBooks(page: number = 1, pageSize: number = 10): Promise<{ books: Book[], totalCount: number }> {
        const offset = (page - 1) * pageSize;
        const sql = `
            SELECT b.id, b.title, b.author, b.pub_year, b.price, b.quantity, 
                   ARRAY_AGG(jsonb_build_object('id', ge.id, 'genre', ge.genre)) AS genres,
                   b.about AS about, b.isbn
            FROM books b 
            LEFT JOIN books_genres bg ON b.id = bg.book_id 
            LEFT JOIN genres ge ON bg.genre_id = ge.id
            GROUP BY b.id, b.title, b.author, b.pub_year, b.price, b.quantity, b.about, b.isbn
            ORDER BY b.id
            LIMIT $1
            OFFSET $2;
        `;
    
        const countSql = `SELECT COUNT(*) FROM books;`;
    
        try {
            const result: QueryResult<Book> = await this.pool.query(sql, [pageSize, offset]);
            const countResult: QueryResult<{ count: number }> = await this.pool.query(countSql);
            const totalCount = countResult.rows[0].count;
    
            return { books: result.rows, totalCount };
    
        } catch (err) {
            throw new CustomError(String(err), 500);
        }
    }
    

    async getBookById(id: number): Promise<Book | null> {
        const sql = "SELECT b.id, b.title, b.author, b.pub_year, b.price, b.quantity, ARRAY_AGG(jsonb_build_object('id', ge.id, 'genre', ge.genre)) AS genres, b.about, b.isbn" +
        " FROM books b LEFT JOIN books_genres bg ON b.id = bg.book_id " +
        " LEFT JOIN genres ge ON bg.genre_id = ge.id" +
        " WHERE b.id = $1" +
        " GROUP BY b.id, b.title, b.author, b.pub_year, b.price, b.quantity, b.about, b.isbn; ";
        const values = [id];

        try {
            const result: QueryResult<Book> = await this.pool.query(sql,  values);
            if (result.rowCount === 0) return null;
            else return result.rows[0];
        } catch (err) {
            throw new Error('Error get book: ' + err);
        }

    }

    async createBook(book: Book): Promise<boolean> {
        const insertBookSql = "INSERT INTO books (title, author, pub_year, price, quantity, about, isbn) VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING id;";
        const values = [book.title, book.author, book.pub_year, book.price, book.quantity, book.about, book.isbn];
    
        try {
            const bookResult = await this.pool.query(insertBookSql, values);
            const bookId = bookResult.rows[0].id;
    
            if (bookId) {
                for (const genre of book.genre) {
                    await this.pool.query("INSERT INTO books_genres (book_id, genre_id) VALUES ($1, $2);", [bookId, genre.id]);
                }
                return true;
            } else {
                return false;
            }
        } catch (error) {
            throw new CustomError("Error creating book: " + String(error), 500);

            // throw new Error('Error creating book: ' + error);
        }
    }

    async updateBook(book: Book): Promise<boolean> {
        const sql = "UPDATE books SET title = $1, author = $2, pub_year = $3, price = $4, quantity = $5, about = $6 , isbn = $7 WHERE id = $8";
        const values = [book.title, book.author, book.pub_year, book.price, book.quantity, book.about, book.isbn, book.id];

        try {
            const result = await this.pool.query(sql, values);
            return true;
        } catch (error) {
            throw new Error('Error updating book: ' + error);
        }
    }

    async getBookGenre(bookId: number): Promise<Genre[]> {
        const sql = "SELECT ge.id, genre From genres ge INNER JOIN books_genres bg ON bg.genre_id = ge.id WHERE bg.book_id = $1;";
        
        try {
            const result: QueryResult<Genre> = await this.pool.query(sql, [bookId]);
            return result.rows;
        } catch (error) {
            throw new Error('Error adding genre to book: ' + error);
        }
    }

    async addBookGenre(genre: Genre, book_id: number): Promise<boolean> {
        const sql = "INSERT INTO books_genres (book_id, genre_id) VALUES ($1, $2);";
        const values = [book_id, genre.id];

        try {
            const result = await this.pool.query(sql, values);
            return true;
        } catch (error) {
            throw new Error('Error adding genre to book: ' + error);
        }
    }

    async updateBookGenre(newGenre: number, genre_id: number, book_id: number): Promise<boolean> {
        const sql = "UPDATE books_genres SET genre_id = $1 WHERE genre_id = $2 AND  book_id = $3;";
        const values = [newGenre, genre_id, book_id];

        try {
            const result = await this.pool.query(sql, values);
            return true;
        }catch (error) {
            throw new Error('Error updating genre to book: ' + error);
        }
    }

    async deleteBookGenre(genre_id: number[], book_id: number): Promise<boolean> {
        const sql = "DELETE FROM books_genres WHERE book_id = $1 AND genre_id IN ($2);";
        const values = [book_id, genre_id.join(',')]

        try {
            const result = await this.pool.query(sql, values);
            return true;
        }catch (error) {
            throw new Error('Error deleting genre to book: ' + error);
        }
    }
}