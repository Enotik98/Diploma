import { Book } from './Book';

export interface OrderBook {
    id: number,
    book: Book,
    quantity: number
}