import {Request, Response} from 'express'
import pool from '../db';
import {GenreRepositorie} from '../repositories/GenreRepositorie';
import { Genre } from '../models/Genre';

const genreRepositorie = new GenreRepositorie(pool);

export const getAllGenres = async (req: Request, res:Response) => {
    try {
        const  genres = await genreRepositorie.getAllGenres();
        return res.status(200).json({genres});
    } catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const createGenre = async (req: Request, res: Response) => {
    try {
        const newGenre: Genre = req.body;
        const result: Genre[]  = await genreRepositorie.createGenre(newGenre);
        
        return res.status(200).json({result});
    } catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const updateGenre = async (req: Request, res: Response) => {
    try {
        const genre: Genre = req.body;
        const result = await genreRepositorie.updateGenre(genre.id, genre.genre);
        
        if (!result) 
            return res.status(404).send("Not Found");
            
        return res.status(200).send("Ok");
    } catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
}