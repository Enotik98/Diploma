import {Request, Response} from 'express'
import pool from '../db';
import { OrderRepositorie } from '../repositories/OrderRepositorie';
import { Order } from '../models/Order';
import { OrderStatus } from '../models/OrderStatus';
import { SalesType } from '../models/SalesType';
import { UserRepositorie } from '../repositories/UserRepositorie';

const orderRepositorie = new OrderRepositorie(pool);
const userRepositorie = new UserRepositorie(pool);

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
