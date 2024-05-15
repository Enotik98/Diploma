import { Pool, Query, QueryResult} from 'pg';
import { Order } from '../models/Order';
import { OrderStatus } from '../models/OrderStatus';

export class OrderRepositorie {
    constructor(private pool: Pool) {}

    async createOrder(order: Order): Promise<boolean> {
        const client = await this.pool.connect();

        try {
            // need update amount !!
            await client.query('BEGIN');

            let sql = "INSERT INTO order_list (user_id, order_date, status, seller, sales_date, type_sales, amount) VALUES ($1, $2, $3::order_status, $4, $5, $6::sales_type, $7) RETURNING id";
            let values = [(typeof order.user === 'number') ? order.user : order.user?.id, order.orderDate, order.status, order.seller, order.salesDate, order.salesType, order.amount];

            let result = await client.query(sql, values);
            const orderId = result.rows[0].id;
            let amount: number = 0;

            if (orderId && order.item.length !== 0){
                let bookSql = "SELECT b.id, b.quantity, b.price FROM books b WHERE b.id IN ($1);";
                let booksId = order.item.map(book => book.book)
                values = [booksId.join(', ')]
                
                //get books from db
                const result = await this.pool.query(bookSql, values);
                const bookArr = result.rows;

                if (bookArr.length === 0 || bookArr.length !== order.item.length) {
                    throw new Error("Book not found");
                }

                sql = "INSERT INTO order_books (order_id, book_id, quantity) VALUES ($1, $2, $3);";
                bookSql = "UPDATE books SET quantity = $1 WHERE id = $2;";

                //add book to order
                const insertPromise = order.item.map(async (book) => {
                    let index = bookArr.findIndex(elem => elem.id === book.book);
                    
                    if (book.book == null || index === -1) {
                        await client.query('ROLLBACK');
                        throw new Error("Book is not valid");
                    }
                    if (bookArr[index].quantity == 0 && bookArr[index].quantity < book.quantity) {
                        await client.query('ROLLBACK');
                        throw Error("Books hasn't in the shop");
                    }

                    values = [orderId, book.book, (book.quantity == null ? 0 : book.quantity)];
                    await client.query(sql, values);

                    //update quantity book
                    await client.query(bookSql, [bookArr[index].quantity - book.quantity, book.book ]);

                    amount += bookArr[index].price;

                });
                await Promise.all(insertPromise);

                //update amount order
                const amountSql = "UPDATE order_list SET amount = amount + $1 WHERE id = $2 ;";
                await client.query(amountSql, [amount, orderId]);

            } else {
                await client.query('ROLLBACK');
            }
            await client.query('COMMIT');

            return true;
        } catch(error) {
            await client.query('ROLLBACK');
            console.log(error);
            
            throw new  Error(`Failed to create order: ${error}`);
        } finally {
            client.release();
        }
    }

    async getOrderById(orderId: number): Promise<Order | null> {
        const sql = "SELECT o.id, " +
                "json_build_object('id', u.id, 'f_name', u.f_name, 'l_name', u.l_name) AS user, " +
                "o.order_date, " +
                "o.status, " +
                "o.seller, " +
                "o.sales_date, " +
                "o.type_sales, " +
                "o.amount, " +
                "ARRAY_AGG(json_build_object('book', json_build_object('id', b.id, 'title', b.title, 'author', b.author), 'quantity', ob.quantity)) AS item " +
                "FROM order_list o LEFT JOIN users u ON o.user_id = u.id " +
                "LEFT JOIN order_books ob ON ob.order_id = o.id " +
                "LEFT JOIN books b ON ob.book_id = b.id " +
                "WHERE o.id = $1 " +
                "GROUP BY o.id, o.order_date, o.status, o.seller, o.sales_date, o.type_sales, o.amount, u.id " ;
        // const sql = "SELECT o.id, ARRAY_AGG(json_build_object('id', u.id, 'f_name', u.f_name, 'l_name', u.l_name)) AS user, o.order_date, o.status " +
        //             "FROM order_list o LEFT JOIN users u ON o.user_id = u.id "+
        //             "WHERE o.id = $1" +
        //             "GROUP BY o.id, o.order_date, o.status;"

        try {

            const res: QueryResult<Order> = await this.pool.query(sql, [orderId]);
            if (res.rowCount == 0) {
                return null;
            }
            let order: Order = res.rows[0];
            return order;
            
        } catch(error) {
            throw new  Error(`Failed get order by id: ${error}`);
        }
    }

    async getOrders(): Promise<Order[]> {
        const sql = "SELECT o.id, " +
                    "json_build_object('id', u.id, 'f_name', u.f_name, 'l_name', u.l_name) AS user, " +
                    "o.order_date, " +
                    "o.status, " +
                    "o.seller, " +
                    "o.sales_date, " +
                    "o.type_sales, " +
                    "o.amount, " +
                    "ARRAY_AGG(json_build_object('book', json_build_object('id', b.id, 'title', b.title, 'author', b.author), 'quantity', ob.quantity)) AS item " +
                    "FROM order_list o LEFT JOIN users u ON o.user_id = u.id " +
                    "LEFT JOIN order_books ob ON ob.order_id = o.id " +
                    "LEFT JOIN books b ON ob.book_id = b.id " +
                    "GROUP BY o.id, o.order_date, o.status, o.seller, o.sales_date, o.type_sales, o.amount, u.id " ;

        try {
            const result: QueryResult<Order> = await this.pool.query(sql);
            return result.rows;

        } catch(error) {
            throw new  Error(`Failed to create order: ${error}`);
        }
    }

    async getBasketId(userId: Number): Promise<number | null> {
        const sql = "SELECT id FROM order_list WHERE user_id = $1 AND status = 'BASKET'::order_status"
        try {
            const result = await this.pool.query(sql, [userId]);
            return result.rows[0].id || null;
        } catch(error) {
            throw new  Error(`Failed to get order: ${error}`);
        }
    }

    async getUserOrder(userId: number, orderSatus: OrderStatus | null): Promise<Order[] | null>{
        const sql = "SELECT o.id, " +
                "json_build_object('id', u.id, 'f_name', u.f_name, 'l_name', u.l_name) AS user, " +
                "o.order_date, " +
                "o.status, " +
                "o.seller, " +
                "o.sales_date, " +
                "o.type_sales, " +
                "o.amount, " +
                "ARRAY_AGG(json_build_object('book', json_build_object('id', b.id, 'title', b.title, 'author', b.author), 'quantity', ob.quantity)) AS item " +
                "FROM order_list o LEFT JOIN users u ON o.user_id = u.id " +
                "LEFT JOIN order_books ob ON ob.order_id = o.id " +
                "LEFT JOIN books b ON ob.book_id = b.id " +
                "WHERE o.user_id = $1 " +
                (orderSatus ? " AND o.status = $2::order_status " : "" ) +
                "GROUP BY o.id, u.id, o.order_date, o.status, o.seller, o.sales_date, o.type_sales, o.amount, u.id " ;
        // const sql = "SELECT o.id, ARRAY_AGG(json_build_object('id', u.id, 'f_name', u.f_name, 'l_name', u.l_name)) AS user, o.order_date, o.status " +
        //             "FROM order_list o LEFT JOIN users u ON o.user_id = u.id "+
        //             "WHERE o.id = $1" +
        //             "GROUP BY o.id, o.order_date, o.status;"

        try {
            const values = (orderSatus) ? [userId, orderSatus] : [userId];
            const res: QueryResult<Order> = await this.pool.query(sql, values);
            if (res.rowCount == 0) {
                return null;
            }
            return res.rows;
        } catch(error) {
            console.log(error);
            
            throw new  Error(`Failed get order by user: ${error}`);
        }
    } 
    
    async addBookToOrder(orderId: number, book_id: number, quantity: number, bookQunatity: number, bookPrice: number): Promise<boolean | null> {
        const sql = "INSERT INTO order_books (order_id, book_id, quantity) VALUES ($1, $2, $3);";
        const bookSql = "UPDATE books SET quantity = $1 WHERE id = $2 RETURNING quantity;";
        const client = await this.pool.connect();

        try {
            await client.query('BEGIN');

            await client.query(sql, [orderId, book_id, quantity]);
            const res = await client.query(bookSql, [bookQunatity - quantity, book_id]);

            if (res.rows[0].quantity < 0) {
                await client.query('ROLLBACK');
                throw new Error ('Not qountity');
            }

            //update amount order
            const amountSql = "UPDATE order_list SET amount = amount + $1 WHERE id = $2 ;";
            await client.query(amountSql, [bookPrice, orderId]);
            await client.query('COMMIT');
            return true
        } catch(error) {
            await client.query('ROLLBACK');
            throw new  Error(`Failed to create order: ${error}`);
        } finally {
            client.release();
        }
    }
}