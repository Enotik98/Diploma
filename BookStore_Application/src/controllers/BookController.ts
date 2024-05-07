import {Request, Response} from 'express'
import pool from '../db';
import { BookRepositorie } from '../repositories/BookRepositorie';
import { Book } from '../models/Book';
import { Genre } from '../models/Genre';
import {CustomError} from '../helpers/CustomError';

const bookRepositorie = new BookRepositorie(pool);

export const getAllBooks = async (req: Request, res:Response) =>{
    try{
        const books = await bookRepositorie.getAllBooks();

        return res.status(200).json({books});
    } catch (err)  {
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const getBookById = async (req: Request, res: Response) => {
    try {
        const id: number = Number(req.params.id);
        
        if (!Number.isInteger(id)) throw "Invalid parameter";

        const book = await bookRepositorie.getBookById(id);
        return res.status(200).json(book);
    }  catch (err)  {
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const createBook = async (req: Request, res: Response) => {
    try{
        const newBook: Book = req.body;

        const result = await bookRepositorie.createBook(newBook);

        if (result) {
            return res.status(200).json("OK");
        }
        return res.status(409).send({message:"Error creating book"})


    } catch (err) {
        if (err instanceof CustomError) {
            return res.status(err.statusCode).send({message: err.message});
        }
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const updateBook = async (req: Request,res: Response) => {
    try {
        const book: Book = req.body;

        const exBook = await bookRepositorie.getBookById(book.id);
        if (exBook == null ) return res.status(404).send('Not found');

        const  updatedBook = await bookRepositorie.updateBook(book);
        return res.status(200).send('OK');
    } catch (err) {
        return res.status(400).send('Bad request ' + err);
    }   
}

export const addBookGenre = async (req: Request, res: Response) => {
    try {
        let newGenres: Genre[] = req.body.genres;
        const book_id: number = Number(req.params.book_id);
        if (isNaN(book_id)) {
            return res.status(400).send('Invalid parameters');
        }

        for  (let i=0;i<newGenres.length;i++) {
           await bookRepositorie.addBookGenre(newGenres[i], book_id);
        }

        const book = await bookRepositorie.getBookById(book_id);
        if (book == null) {
            return res.status(404).json({ message: "Book not found!" });
        }
        return res.status(200).json({book});

    }  catch (err) {
        return res.status(400).send('Bad request ' + err);
    } 
}

export const updateBookGenre = async (req: Request, res: Response) => {
    try {
        const book_id: number = Number(req.params.book_id);
        if (isNaN(book_id)) {
            return res.status(400).send('Invalid parameters');
        }

        const updateList = req.body.genres;
        const oldGenre = await bookRepositorie.getBookGenre(book_id);
        if (oldGenre.length <  updateList.length){
            return res.status(409).json({message: "The list of genres must have the less size as the existing one"});
        }

        let dublicate = "";
        for(let i=0; i < updateList.length ;i++){  
            const exists = oldGenre.find(elem => elem.id === updateList[i].new)
            if (exists) {
                dublicate += `'${exists.genre}', `;
            }else{
                await bookRepositorie.updateBookGenre(updateList[i].new, updateList[i].old, book_id);
            }
        }
        
        const book = await bookRepositorie.getBookById(book_id);
        if (book == null) {
            return res.status(404).json({ message: "Book not found!" });
        }
        return res.status(200).json({book, message: (dublicate === "" ? "OK" : `The genres ${dublicate} is already in the list.`)});

    } catch (err) {
        return res.status(400).send('Bad request ' + err);
    } 
}

export const deleteBookGenre = async (req: Request, res: Response) => {
    try {
        const book_id: number = Number(req.params.book_id);
        const genre_ids: number[] = req.body.genre_ids;
        console.log(genre_ids);
        
        const result = await  bookRepositorie.deleteBookGenre(genre_ids, book_id);
        return res.status(200).send("ok");

    }  catch (err) {
        return res.status(400).send('Bad request ' + err);
    } 
}

