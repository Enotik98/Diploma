import { User } from './User';
import { OrderStatus } from './OrderStatus';
import { OrderBook } from './OrderBook';
import { SalesType } from './SalesType';

export interface Order {
    id: number | null,
    user: User | number | null,
    orderDate: Date,
    status: OrderStatus | null,
    seller: string | null,
    salesDate: Date | null,
    salesType: SalesType,
    amount: number,
    item: OrderBook[]
}