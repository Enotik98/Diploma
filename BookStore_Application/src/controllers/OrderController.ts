import {Request, Response} from 'express'
import pool from '../db';
import { OrderRepositorie } from '../repositories/OrderRepositorie';
import { Order } from '../models/Order';
import { Book } from '../models/Book';
import { OrderStatus } from '../models/OrderStatus';
import { OrderBook } from '../models/OrderBook';
import { SalesType } from '../models/SalesType';
import { UserRepositorie } from '../repositories/UserRepositorie';
import { BookRepositorie } from '../repositories/BookRepositorie';

const orderRepositorie = new OrderRepositorie(pool);
const userRepositorie = new UserRepositorie(pool);
const bookRepositorie = new BookRepositorie(pool);

export const createOrder = async (req: Request, res: Response) => {
    try {
        let order: Order = req.body;
        const userId = Number(req.user?.userId);
        console.log(SalesType[order.salesType as keyof typeof SalesType]);
        
        if (order.salesType === String(SalesType.ONLINE)) {
            order.user = userId;
            order.orderDate = new Date();
            
            order.status = OrderStatus.NEW;
        } else if (order.salesType === String(SalesType.SHOP)) {
            order.status = null;
            const seller = await userRepositorie.getUserById(userId);
            order.seller = (seller !== null) ? seller.email : null ;
            order.salesDate = new Date();
        } 
        
        order.amount *= 100;
        console.log(order);
        
        if (order.item.length === 0 ) {
            return res.status(400).json({message: 'The order must include books!'});
        }
        
        await orderRepositorie.createOrder(order);
        return res.status(200).send("ok");

    }  catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const addToOrder = async (req: Request, res: Response) => {
    try {
        const userId = Number(req.user?.userId) || -1;
        const bookId = Number(req.body.book_id);
        const quntity = Number(req.body.quantity);
        console.log(userId);
        

        const orderId = await orderRepositorie.getBasketId(userId);
        console.log(orderId);
        
        const book: Book | null = await bookRepositorie.getBookById(bookId);
        console.log(book);
        

        if (!book) { 
            return res.status(400).json({message: 'The book does not exist!'});
        }
        if (book.quantity < quntity) {
            return res.status(400).json({message: 'The book quantity is not enough!'});
        }

        if (orderId && bookId && quntity) {
            //update basket

            // add update amount !!!
            const result = await orderRepositorie.addBookToOrder(orderId, bookId, quntity, book.quantity, book.price);
            if (!result) {
                return res.status(400).json({message: 'The book is not in the basket!'});
            }
            return res.status(200).json({message: 'Ok'});


        } else if (bookId && quntity){
            //create basket
            
            let order: Order = {
                id: null,
                user: userId,
                orderDate: new Date(),
                status: OrderStatus.BASKET,
                seller: null,
                salesDate: null,
                salesType: SalesType.ONLINE,
                amount: Number(book.price),
                item: [{
                    id: null,
                    book: bookId,
                    quantity: quntity
                }]
            }
            console.log(order);
            
            const result = await orderRepositorie.createOrder(order);
            if (result ) {
                return res.status(200).json({message: "ok"});
            }
            return res.status(400).json({message: "error"});
        }
        
    }  catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const getOrderById = async (req: Request, res: Response) => {
    try {
        const orderId: number = Number(req.params.order_id);
        const result = await orderRepositorie.getOrderById(orderId);
        return res.status(200).json({result});
    }  catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const getAllOrder = async (req: Request, res: Response) => {
    try {
        const result = await orderRepositorie.getOrders();
        return res.status(200).json({result})
    }  catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
}

export const getUserBasket = async (req: Request, res: Response) => {
    try {
        console.log("1");
        
        const userId = Number(req.user?.userId) || -1;
        console.log(userId);
        
        const result = await orderRepositorie.getUserOrder(userId, OrderStatus.BASKET);
        if (result?.length === 1) {
            return res.status(200).json(result[0]);
        }
        if (!result) {
            return res.status(200).json([]);
        }
        return res.status(400).json('Error');
    }  catch(err){
        return res.status(500).send(`Server error\n ${err}`);
    }
}