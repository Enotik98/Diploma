import { User } from './User';
import { OrderStatus } from './OrderStatus';
import { OrderBook } from './OrderBook';
import { SalesType } from './SalesType';

export interface Order {
    id: number,
    user: User | number | null,
    orderDate: Date,
    status: OrderStatus | null,
    seller: string | null,
    salesDate: Date,
    salesType: SalesType,
    amount: number,
    item: OrderBook[]
}