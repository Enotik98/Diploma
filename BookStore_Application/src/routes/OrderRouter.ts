import express from 'express';
import { addToOrder, createOrder, getAllOrder, getOrderById, getUserBasket } from '../controllers/OrderController';

const router = express.Router();

router.get('/:order_id', getOrderById);
router.get('/', getAllOrder);
router.get('/user/basket', getUserBasket);

router.post('/', createOrder);
router.post('/book', addToOrder);

export default router;
