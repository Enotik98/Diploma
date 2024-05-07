import { Genre } from "./Genre";

export interface Book {
    id: number, 
    title: string,
    author: string,
    pub_year: string,
    price: number,
    quantity: number,
    genre: Genre[],
    about: string
}