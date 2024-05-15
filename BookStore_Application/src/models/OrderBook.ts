import { Book } from './Book';

export interface OrderBook {
    id: number | null,
    book: Book | number,
    quantity: number
}